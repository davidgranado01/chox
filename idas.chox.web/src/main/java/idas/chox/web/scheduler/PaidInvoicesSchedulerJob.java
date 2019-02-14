package idas.chox.web.scheduler;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;

import org.springframework.beans.factory.annotation.Autowired;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.ClaimType;
import idas.chox.core.model.Insurer;
import idas.chox.core.model.SchedulerJob;
import idas.chox.core.workflow.Activity;
import idas.chox.service.workflow.ActivityFactory;

public class PaidInvoicesSchedulerJob extends DbSchedulerJob {

    private static final Logger LOG = LoggerFactory.getLogger(PaidInvoicesSchedulerJob.class);
    private static final int POSITION_INSURER_NAME = 0;
    private static final int POSITION_CHO_REFERENCE = 1;
    private static final int POSITION_CLAIM_NUMBER = 2;
    private ActivityFactory activityFactory;
    public static final String JOB_NAME = "PAID_INVOICES";
    @Autowired
    private String inboundDirectory;
    private String processedDirectory;

    public void setInboundDirectory(String inboundDirectory) {
        this.inboundDirectory = inboundDirectory;
    }

    public void setProcessedDirectory(String processedDirectory) {
        this.processedDirectory = processedDirectory;
    }

    @Override
    public final boolean doJob() {
        Insurer ins = getSecurityInfoProvider().getCurrentUser().getInsurer();
        if (ins == null) {
            LOG.warn("PaidInvoices user '{}' is not associated to an Insurer organisation", getSecurityInfoProvider().getCurrentUser().getUserName());
            return false;
        }
        String insurerName = ins.getName();
        try {
            CSVReader reader;
            String[] line;
            // Check mounted inbound directory for *.xlsx/*.csv files
            File folder = new File(inboundDirectory + "/" + getInboundDirectory(insurerName));
            // Check file matches format LVCHOXInvoicepaid.csv
            Collection<File> fileNames = FileUtils.listFiles(folder, new WildcardFileFilter("LVCHOXInvoicepaid*.csv"), null);
            for (File csvFile : fileNames) {
                try {
                    LOG.info("Processing Paid Invoices CSV file '{}'", csvFile.getCanonicalPath());
                    int lineNo = 0;
                    // For each file found
                    reader = new CSVReaderBuilder(new FileReader(csvFile))
                            //                                .withFieldAsNull(CSVReaderNullFieldIndicator.EMPTY_SEPARATORS)
                            // Skip the header
                            .withSkipLines(1)
                            .build();
                    while ((line = reader.readNext()) != null) {
                        lineNo++;
                        // Check no of columns
                        if (line.length < 3) {
                            LOG.error("Error processing entry {}: expecting 3 columns but only found '{}'", lineNo, line.length);
                            continue;
                        }
                        // Validate content: all columns must be non-empty, insurer name must match
                        String insurerName2 = line[POSITION_INSURER_NAME].trim();
                        if (insurerName2.isEmpty()) {
                            LOG.error("Error processing entry {}: Insurer name cannot be empty", lineNo);
                            continue;
                        }
                        if (!insurerName.equals(insurerName2)) {
                            LOG.error("Error processing entry {}: Insurer name '{}' does not match user organisation '{}'", lineNo, insurerName, insurerName2);
                            continue;
                        }

                        String claimNumber = line[POSITION_CLAIM_NUMBER].trim();
                        if (claimNumber.isEmpty()) {
                            LOG.error("Error processing entry {}: Insurer Claim Number cannot be empty", lineNo);
                            continue;
                        }

                        String choReference = line[POSITION_CHO_REFERENCE].trim();
                        if (choReference.isEmpty()) {
                            LOG.error("Error processing entry {}: Supplier/CHO reference cannot be empty", lineNo);
                            continue;
                        }

                        // Add to Paid Invoices table if matching claim exists in correct status
                        // Start transaction
                        handleHibernateTransactionIntricacies();
                        try {
                            List<Claim> claims = claimService.getClaimByCHOReferenceAndClaimNumber(choReference, claimNumber);
                            if (claims.size() == 1 && claims.get(0).getStatus().equals(ClaimStatus.AWAITING_INVOICE_PAYMENT.toString())) {
                                Claim claim = claims.get(0);
                                LOG.debug("Match found for import entry with cho reference='{}' and claim number='{}': ", new Object[]{
                                    choReference, claimNumber});
                                Activity activity;
                                if (ClaimType.isInsurerUpload(claim.getClaimType())) {
                                    activity = activityFactory.getActivity("updateManualInvoicePaid");
                                } else {
                                    activity = activityFactory.getActivity("invoicePaymentLogged");
                                }
                                activity.process(claim);
                                LOG.debug("{}: Claim with cho reference = '{}' (id={}) processed to a paid state with activity {}",
                                        new Object[]{lineNo, claim.getChoReference(), claim.getId(), activity.getClass().toString()});
                            } else if (claims.isEmpty()) {
                                LOG.debug("No claim matches Paid invoices entry at line {} with cho ref '{}'", lineNo, choReference);
                            } else if (claims.size() > 1) {
                                LOG.debug("Multiple claims match Paid invoices entry at line {} with cho ref '{}' and claim number '{}'", lineNo, choReference, claimNumber);
                            } else {
                                LOG.debug("Paid invoices entry at line {} with cho ref '{}' and claim number '{}' in wrong status '{}'",
                                        new Object[]{lineNo, choReference, claimNumber, claims.get(0).getStatus()});
                            }
                        } catch (Exception ex) {
                            LOG.error("Exception thrown processing paid invoices entry {}: {}", lineNo, ex.getMessage());
                        } finally {
                            releaseHibernateSessionConditionally();
                        }
                    }
                } catch (IOException ex) {
                    LOG.error("IOException thrown processing paid invoices file '{}': {}", csvFile.getCanonicalPath(), ex.getMessage());
                } finally {
                    // Move file to 'processed' directory
                    FileUtils.moveFileToDirectory(csvFile, FileUtils.getFile(processedDirectory), false);
                }
            }
        } catch (Exception ex) {
            LOG.error("Exception thrown checking for paid invoice processing: {}", ex.getMessage(), ex);
        }


        return true;
    }

    @Override
    protected List<SchedulerJob> getSchedulerJobs() {
        return getSchedulerJobService().getSchedulerJobs(JOB_NAME);
    }

    public void setActivityFactory(ActivityFactory activityFactory) {
        this.activityFactory = activityFactory;
    }
}
