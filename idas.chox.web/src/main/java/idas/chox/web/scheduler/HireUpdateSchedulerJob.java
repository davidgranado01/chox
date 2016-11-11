package idas.chox.web.scheduler;

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
import idas.chox.core.model.VehicleHire;
import idas.chox.core.services.VehicleClassService;
import idas.chox.core.util.DateHelper;
import idas.chox.core.workflow.Activity;
import idas.chox.service.workflow.ActivityFactory;
import idas.chox.service.workflow.activities.HireUpdate;

public class HireUpdateSchedulerJob extends ExcelEmailSchedulerJob {

    private static final Logger LOG = LoggerFactory.getLogger(HireUpdateSchedulerJob.class);

    private ActivityFactory activityFactory;
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
                Date hireStartDate = validateDate(hireStartString, statusString, "Hire Start (Date)");

                /* Check Hire Start time provided is valid */
                String hireStartTime = null;
                if (cells.size() > 3) {
                    hireStartTime = cells.get(3).trim();
                }
                if (hireStartTime != null && hireStartTime.isEmpty()) {
                    hireStartTime = null;
                } else if (hireStartTime != null) {
                    hireStartTime = validateTime(hireStartTime, statusString);
                }


                /* Check update insurer column is valid, if present */
                boolean updateInsurer = false;
                if (cells.size() > 4) {
                    updateInsurer = validateUpdateInsurer(cells.get(4).trim(), statusString);
                }

                // Check values have changed, otherwise do not update
                if (claim != null) {
                    VehicleHire vh = claim.getVehicleHire();
                    Date hireStartDateTime;
                    // Merge date and time
                    if (hireStartTime != null) {
                        try {
                            Date time = DateHelper.getTimeFormat().parse(hireStartTime);
                            hireStartDateTime = DateHelper.mergeTimeToDate(hireStartDate, time);
                        } catch (Exception ex) {
                            LOG.error("Exception thrown merging time '{}' into date '{}': {}", new Object[]{hireStartTime, hireStartDate, ex.getMessage()});
                            hireStartDateTime = hireStartDate;
                        }
                    } else {
                        hireStartDateTime = hireStartDate;
                    }

                    if (vh != null && vh.getVehicleClass() != null && vehicleClass != null  && vh.getRentalStart() != null
                            && vh.getVehicleClass().getName().equals(vehicleClass.getName())
                            && vh.getRentalStart().compareTo(hireStartDateTime) == 0) {
                        statusString.append("Failed: No change from existing Vehicle Class or Hire Start details");
                    }
                }
                /* If validation passed add the new hire monitoring ECD.*/
                if (statusString.toString().isEmpty()) {
                    try {
                        Activity activity = (HireUpdate) activityFactory.getActivity("hireUpdate");
                        ((HireUpdate) activity).setVehicleClass(vehicleClass);
                        ((HireUpdate) activity).setHireStartDate(hireStartDate);
                        ((HireUpdate) activity).setHireStartTime(hireStartTime);
                        ((HireUpdate) activity).setUpdateInsurer(updateInsurer);
                        activity.process(claim);
                        statusString.append("Success: Updated.");
                        LOG.debug("Updated claim '{}' ('{}') [row:{}]", new Object[]{referenceNumber, claim.getChoReference(), row});
                    } catch (AccessDeniedException ex) {
                        statusString.append("Failed: No Access to Hire Update Activity (Invalid Claim Status '")
                                .append(claim.getStatus()).append("')");
                        LOG.warn("AccessDenied Exception thrown when updating Hire Start via email scheduler job for claim '{}' [row:{}]", claim.getChoReference(), row);
                    } catch (Exception ex) {
                        statusString.append("Failed: An Internal Error Occurred.");
                        LOG.warn("Exception occurred when updating hire start via email scheduler job job for claim '{}' [row:{}]", claim.getChoReference(), row, ex);
                    }
                } else {
                    LOG.debug("Failed Update of claim '{}' - {} [row:{}]", new Object[]{referenceNumber, statusString, row});
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

    private VehicleClass validateVehicleClass(String vehicleClassString, StringBuilder statusString) {
        VehicleClass vehicleclass = vehicleClassService.getVehicleClassByName(vehicleClassString);
        if (vehicleclass == null) {
            statusString.append(" Invalid Replacement Vehicle Class Provided.");
        }

        return vehicleclass;
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
