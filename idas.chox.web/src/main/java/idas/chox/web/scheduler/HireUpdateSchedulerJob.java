package idas.chox.web.scheduler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.annotation.Secured;

import idas.chox.core.model.Claim;
import idas.chox.core.model.SchedulerJob;
import idas.chox.core.model.VehicleClass;
import idas.chox.core.services.VehicleClassService;
import idas.chox.core.util.DateHelper;
import idas.chox.core.workflow.Activity;
import idas.chox.service.workflow.ActivityFactory;
import idas.chox.service.workflow.activities.HireUpdate;


public class HireUpdateSchedulerJob extends ExcelEmailSchedulerJob {

    private static final Logger LOG = LoggerFactory.getLogger(HireUpdateSchedulerJob.class);
    
    private ActivityFactory activityFactory;
    private String REG_TIME = "^(([0-1]?[0-9])|([2][0-3])):([0-5]?[0-9])?$";
//    private String REG_TIME = "^(([0-1]?[0-9])|([2][0-3])):([0-5]?[0-9])(:([0-5]?[0-9]))?$";
    public static final String JOB_NAME = "HIRE_UPDATE";
    private VehicleClassService vehicleClassService;

    public void setVehicleClassService(VehicleClassService vehicleClassService) {
        this.vehicleClassService = vehicleClassService;
    }

    @Secured({"ROLE_CHO", "ROLE_CHOX_ADMIN"})
    @Override
    protected Map<Integer, List<String>> doJob(Map<Integer, List<String>> xlsDataMap, String sender) {
        Set<Integer> rowNumbers = xlsDataMap.keySet();

        for (Integer row : rowNumbers) {
            // first row is header
            if (row != 0) { // ignore first row - should contain header
                List<String> cells = xlsDataMap.get(row);

                if (cells.size() < 4) {
                    //ignore row
                    LOG.debug("Ignoring row {} - only has {} cells.", row, cells.size());
                    continue;
                }
                
                StringBuilder statusString = new StringBuilder();

                /* Check is valid referenceNumber provided and claim is in valid status.*/
                String referenceNumber = cells.get(0).trim();
                Claim claim = validateClaimReferenceNumber(referenceNumber, statusString);
                
                /* Check vehicle class is valid */
                String vehicleClassString = null;
                if (cells.size() > 1) {
                    vehicleClassString = cells.get(1).trim();
                }
                VehicleClass vehicleClass = null;
                if (vehicleClassString != null && !vehicleClassString.isEmpty()) {
                    vehicleClass = validateVehicleClass(vehicleClassString, statusString);
                }
                
                /* Check Hire Start date provided is valid and parse the string date to java date.*/
                String hireStartString = null;
                if (cells.size() > 2) {
                    hireStartString = cells.get(2).trim();
                }
                Date hireStartDate = validateHireStartDate(hireStartString, statusString);

                /* Check Hire Start time provided is valid */
                String hireStartTime = null;
                if (cells.size() > 3) {
                    hireStartTime = cells.get(3).trim();
                }
                if (hireStartTime != null && hireStartTime.isEmpty()) {
                    hireStartTime = null;
                } else if (hireStartTime != null) {
                    hireStartTime = validateHireStartTime(hireStartTime, statusString);
                }


                /* Check update insurer column is valid, if present */
                boolean updateInsurer = false;
                if (cells.size() > 4) {
                    updateInsurer = validateUpdateInsurer(cells.get(4).trim(), statusString);
                }

                /* If validation passed add the new hire monitoring ECD.*/
                if (statusString.toString().isEmpty()) {
                    try {
                        Activity activity = (HireUpdate) activityFactory.getActivity("hireUpdate");
                        ((HireUpdate)activity).setVehicleClass(vehicleClass);
                        ((HireUpdate)activity).setHireStartDate(hireStartDate);
                        ((HireUpdate)activity).setHireStartTime(hireStartTime);
                        ((HireUpdate)activity).setUpdateInsurer(updateInsurer);
                        activity.process(claim);
                        statusString.append("Success: Updated.");
                    } catch (AccessDeniedException ex) {
                        statusString.append("Failed: No Access to Hire Update Activity (Invalid Claim Status '")
                                .append(claim.getStatus()).append("')");
                        LOG.warn("AccessDenied Exception thrown when updating Hire Start via email scheduler job");
                    } catch (Exception ex) {
                        statusString.append("Failed: An Internal Error Occurred.");
                        LOG.warn("Exception occurred when updating hire start via email scheduler job", ex);
                    }
                } else {
                    statusString.insert(0, "Failed:");
                }

                /* update the result message into column 5 for each row.*/
                if (xlsDataMap.get(row).size() < 5) {
                    xlsDataMap.get(row).add("dummy column");
                }
                if (xlsDataMap.get(row).size() < 6) {
                    xlsDataMap.get(row).add(statusString.toString());
                } else {
                    xlsDataMap.get(row).set(5, statusString.toString());
                }
            }
        }
        return xlsDataMap;
    }

    @Override
    protected String buildMessage(String email, String subject, Map<Integer, List<String>> xlsDataMap) {
        
        StringBuilder emailMsg = new StringBuilder();
        emailMsg.append("======================================================================\n");
        emailMsg.append("Submitted By Email: ").append(email).append("\n");
        emailMsg.append("Date: ").append(DateHelper.getCurrentDateWithFormat(email_date_format)).append("\n");
        emailMsg.append("Subject: ").append(subject).append("\n");
        emailMsg.append("======================================================================\n\n");
        if (xlsDataMap != null) {
            emailMsg.append("Reference Number           Message\n");
            emailMsg.append("-----------------------------------------------------------------------------------------------\n");
            Set<Integer> rowNumbers = xlsDataMap.keySet();

            for (Integer row : rowNumbers) {
                if (row != 0) {
                    List<String> cells = xlsDataMap.get(row);
                    if (cells.size() >= 5) { // We expect at least three columns
                        emailMsg.append(String.format("%-22s", cells.get(0).trim()));
                        emailMsg.append("\t\t");
                        emailMsg.append(cells.get(5).trim());
                        emailMsg.append("\n");
                    }
                }
            }
  
        } else {
            emailMsg.append("No xls attachement found in email, please check and re-submit.\n");
            emailMsg.append("-----------------------------------------------------------------------------------------------\n");
        }
        LOG.debug("Message to send is: \n*********\n{}\n*********", emailMsg.toString());
        return emailMsg.toString();
    }

    
    private Date validateHireStartDate(String hireStartDateString, StringBuilder statusString) {
        Date hireStartDate = null;
        SimpleDateFormat sdf = DateHelper.getLocalDateFormat();
//        SimpleDateFormat sdf1 = DateHelper.getLocalDateTimeFormat();
        sdf.setLenient(false);
        if (hireStartDateString.isEmpty()) {
            statusString.append(" No Hire Start (Date) Provided.");
        } else if (hireStartDateString.length() != sdf.toPattern().length()) {
//                && hireStartDateString.length() != sdf1.toPattern().length()) {
            statusString.append(" Invalid Format For Hire Start (Date).");
        } else {
            try {
                hireStartDate = sdf.parse(hireStartDateString);
                LOG.debug("Date parsed - {} as {}", hireStartDateString, hireStartDate);
            } catch (ParseException ex) {
                statusString.append(" Invalid Format For Hire Start (Date).");
                LOG.warn("Parse exception thrown for hire-start date {}", hireStartDateString);

            }
        }
        return hireStartDate;
    }
    
    private VehicleClass validateVehicleClass(String vehicleClassString, StringBuilder statusString) {
        VehicleClass vehicleclass = vehicleClassService.getVehicleClassByName(vehicleClassString);
        if (vehicleclass == null) {
            statusString.append(" Invalid Replacement Vehicle Class Provided.");
        }
        
        return vehicleclass;
    }

    private String validateHireStartTime(String hireStartTime, StringBuilder statusString) {
        if (!regexExpressionChecker(REG_TIME, hireStartTime)) {
            statusString.append("  Invalid Format for Hire Start (Time).");
        } 
        
        return hireStartTime;
    }

    private boolean validateUpdateInsurer(String updateInsurerString, StringBuilder statusString) {
        boolean updateInsurer = false;
        
        if (updateInsurerString != null && !updateInsurerString.isEmpty()) {
            if (updateInsurerString.trim().equalsIgnoreCase("y") || updateInsurerString.trim().equalsIgnoreCase("yes")) {
                updateInsurer = true;
            } else if (!updateInsurerString.trim().equalsIgnoreCase("n") && !updateInsurerString.trim().equalsIgnoreCase("no")) {
                statusString.append(" Invalid Format for Update Insurer.");
            }
        }
        return updateInsurer;
    }

    public void setActivityFactory(ActivityFactory activityFactory) {
        this.activityFactory = activityFactory;
    }
    
    @Override
    protected List<SchedulerJob> getSchedulerJobs() {
        return getSchedulerJobService().getSchedulerJobs(JOB_NAME);
    }


}
