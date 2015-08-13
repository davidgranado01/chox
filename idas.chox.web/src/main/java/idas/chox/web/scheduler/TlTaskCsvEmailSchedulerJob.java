package idas.chox.web.scheduler;

import java.util.List;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;

import idas.chox.core.model.Claim;
import idas.chox.core.model.SchedulerJob;
import idas.chox.core.util.DateHelper;
import idas.chox.core.workflow.Activity;
import idas.chox.service.workflow.ActivityFactory;
import idas.chox.service.workflow.activities.TlTaskCreation;
import static idas.chox.web.scheduler.SchedulerJobBase.email_date_format;

/**
 *
 * @author John
 */
public class TlTaskCsvEmailSchedulerJob extends CsvEmailSchedulerJob {
    private static final Logger LOG = LoggerFactory.getLogger(TlTaskCsvEmailSchedulerJob.class);
    
    private ActivityFactory activityFactory;
    public static final String JOB_NAME = "TL_TASK";

    public void setActivityFactory(ActivityFactory activityFactory) {
        this.activityFactory = activityFactory;
    }

    @Override
    protected List<String[]> doJob(List<String[]> jobInput, String sender) {
        List <String[]> results = new ArrayList<>();
        for (String[] row : jobInput) {
            if (row.length > 0 && !row[0].equalsIgnoreCase("Client Reference")) { // ignore first row - should contain header
                String[] line = new String[2];
                // Copy CHO Reference from 1st input cell to output
                line[0] = row[0].trim();
                boolean update = false;


                
                Activity activity = (TlTaskCreation) activityFactory.getActivity("tlTaskCreation");
                StringBuilder statusString = new StringBuilder();

                /* Check is valid referenceNumber provided */
                Claim claim = validateClaimReferenceNumber(line[0], statusString);
                
                if (row.length >= 2 && !row[1].isEmpty()) {
                    ((TlTaskCreation)activity).setImsReference(row[1].trim());
                    update = true;
                }
                if (row.length >= 3 && !row[2].isEmpty()) {
                    ((TlTaskCreation)activity).setRegistrationNumber(row[2].trim());
                    update = true;
                }
                if (row.length >= 4 && !row[3].isEmpty()) {
                    ((TlTaskCreation)activity).setMake(row[3].trim());
                    update = true;
                }
                if (row.length >= 5 && !row[4].isEmpty()) {
                    ((TlTaskCreation)activity).setModel(row[4].trim());
                    update = true;
                }
                if (row.length >= 6 && !row[5].isEmpty()) {
                    ((TlTaskCreation)activity).setChassisNumber(row[5].trim());
                    update = true;
                }
                if (row.length >= 7 && !row[6].isEmpty()) {
                    ((TlTaskCreation)activity).setPreAccidentValue(row[6].trim());
                    update = true;
                }
                if (row.length >= 8 && !row[7].isEmpty()) {
                    ((TlTaskCreation)activity).setSalvageAmmount(row[7].trim());
                    update = true;
                }
                if (row.length >= 9 && !row[8].isEmpty()) {
                    ((TlTaskCreation)activity).setSalvageCategory(row[8].trim());
                    update = true;
                }
                if (row.length >= 10 && !row[9].isEmpty()) {
                    ((TlTaskCreation)activity).setAmountToPay(row[9].trim());
                    update = true;
                }
                if (row.length >= 11 && !row[10].isEmpty()) {
                    ((TlTaskCreation)activity).setThirdPartyName(row[10].trim());
                    update = true;
                }
                if (row.length >= 12 && !row[11].isEmpty()) {
                    ((TlTaskCreation)activity).setThirdPartyReg(row[11].trim());
                    update = true;
                }
                if (row.length >= 13 && !row[12].isEmpty()) {
                    ((TlTaskCreation)activity).setThirdPartyClaimNumber(row[12].trim());
                    update = true;
                }
                if (row.length >= 14 && !row[13].isEmpty()) {
                    ((TlTaskCreation)activity).setThirdPartyAgentName(row[13].trim());
                    update = true;
                }
                if (row.length >= 15 && !row[14].isEmpty()) {
                    ((TlTaskCreation)activity).setTitle(row[14].trim());
                    update = true;
                }
                if (row.length >= 16 && !row[15].isEmpty()) {
                    ((TlTaskCreation)activity).setDriverFirstName(row[15].trim());
                    update = true;
                }
                if (row.length >= 17 && !row[16].isEmpty()) {
                    ((TlTaskCreation)activity).setDriverLastName(row[16].trim());
                    update = true;
                }
                if (row.length >= 18 && !row[17].isEmpty()) {
                    ((TlTaskCreation)activity).setPayee(row[17].trim());
                    update = true;
                }
                if (row.length >= 19 && !row[18].isEmpty()) {
                    ((TlTaskCreation)activity).setLine1(row[18].trim());
                    update = true;
                }
                if (row.length >= 20 && !row[19].isEmpty()) {
                    ((TlTaskCreation)activity).setLine2(row[19].trim());
                    update = true;
                }
                if (row.length >= 21 && !row[20].isEmpty()) {
                    ((TlTaskCreation)activity).setTown(row[20].trim());
                    update = true;
                }
                if (row.length >= 22 && !row[21].isEmpty()) {
                    ((TlTaskCreation)activity).setPostcode(row[21].trim());
                    update = true;
                }
                if (row.length >= 23 && !row[22].isEmpty()) {
                    ((TlTaskCreation)activity).setVehicleStatus(row[22].trim());
                    update = true;
                }
                if (row.length >= 24 && !row[23].isEmpty()) {
                    ((TlTaskCreation)activity).setArea1Severity(row[23].trim());
                    update = true;
                }
                if (row.length >= 25 && !row[24].isEmpty()) {
                    ((TlTaskCreation)activity).setArea1Damage(row[24].trim());
                    update = true;
                }
                if (row.length >= 26 && !row[25].isEmpty()) {
                    ((TlTaskCreation)activity).setArea2Severity(row[25].trim());
                    update = true;
                }
                if (row.length >= 27 && !row[26].isEmpty()) {
                    ((TlTaskCreation)activity).setArea2Damage(row[26].trim());
                    update = true;
                }
                if (row.length >= 28 && !row[27].isEmpty()) {
                    ((TlTaskCreation)activity).setTotalLossDate(row[27].trim());
                    update = true;
                }
                
                
                 /* If validation passed, process TlTaskCreation activity.*/
                if (statusString.toString().isEmpty() && update) {
                    try {
                        activity.process(claim);
                        statusString.append("Success: Updated.");
                    } catch (AccessDeniedException ex) {
                        statusString.append("Failed: No Access to TlTaskCreation Activity (Invalid Claim Status '")
                                .append(claim.getStatus()).append("')");
                        LOG.warn("AccessDenied Exception thrown when creating TL Task via email scheduler job");
                    } catch (Exception ex) {
                        statusString.append("Failed: An Internal Error Occurred.");
                        LOG.warn("Exception occurred when creating TL Task via email scheduler job: {}", ex.getMessage());
                    }
                } else if (statusString.toString().isEmpty() && !update) {
                    statusString.insert(0, "Nothing to update.");
                } else {
                    statusString.insert(0, "Failed: ");
                }
                line[1] = statusString.toString();
                results.add(line);
            } else {
                //ignore row
                LOG.info("Ignoring row {} - only has {} cells.", row, row.length);
            }
        }
        return results;
    }

    @Override
    protected String buildMessage(String email, String subject, List<String[]> xlsDataMap) {
        StringBuilder emailMsg = new StringBuilder();
        emailMsg.append("======================================================================\n");
        emailMsg.append("Submitted By Email: ").append(email).append("\n");
        emailMsg.append("Date: ").append(DateHelper.getCurrentDateWithFormat(email_date_format)).append("\n");
        emailMsg.append("Subject: ").append(subject).append("\n");
        emailMsg.append("======================================================================\n\n");
        if (xlsDataMap != null) {
            emailMsg.append("Reference Number           Message\n");
            emailMsg.append("-----------------------------------------------------------------------------------------------\n");

            for (String[] row : xlsDataMap) {
                if (row.length==2) {
                    emailMsg.append(String.format("%-22s", row[0].trim()));
                    emailMsg.append("\t\t");
                    emailMsg.append(row[1].trim());
                    emailMsg.append("\n");
                }
            }
  
        } else {
            emailMsg.append("No CSV attachement found in email, please check and re-submit.\n");
            emailMsg.append("-----------------------------------------------------------------------------------------------\n");
        }
        LOG.debug("Message to send is: \n*********\n{}\n*********", emailMsg.toString());
        return emailMsg.toString();
    }

    @Override
    protected List<SchedulerJob> getSchedulerJobs() {
        return getSchedulerJobService().getSchedulerJobs(JOB_NAME);
    }
    
}
