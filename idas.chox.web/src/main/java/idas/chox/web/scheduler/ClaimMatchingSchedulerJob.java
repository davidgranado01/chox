package idas.chox.web.scheduler;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimMatchingEntry;
import idas.chox.core.model.Insurer;
import idas.chox.core.model.SchedulerJob;
import idas.chox.core.services.ClaimMatchingService;
import idas.chox.core.util.DateHelper;
import idas.chox.core.util.Xlsx2csvUtility;
import idas.chox.service.workflow.ActivityFactory;
import idas.chox.service.workflow.activities.ClaimMatching;

public class ClaimMatchingSchedulerJob extends DbSchedulerJob {

    private static final Logger LOG = LoggerFactory.getLogger(ClaimMatchingSchedulerJob.class);
    private static final int POSITION_CLAIM_NUMBER = 0;
    private static final int POSITION_INSURER_NAME = 1;
    private static final int POSITION_TP_REGISTRATION = 2;
    private static final int POSITION_INCIDENT_DATE = 3;
    private static final int POSITION_INDEMNITY_STANCE = 4;
    private static final int POSITION_LIABILITY_STANCE = 5;
    private static final int POSITION_INSURER_LIABILITY = 6;
    private ActivityFactory activityFactory;
    public static final String JOB_NAME = "CLAIM_MATCHING";
    @Autowired
    private ClaimMatchingService claimMatchingService;
    private String inboundDirectoryBase;
    private String processedDirectory;
    private String xlsx2csvLocation;

    public void setInboundDirectoryBase(String inboundDirectory) {
        this.inboundDirectoryBase = inboundDirectory;
    }

    public void setProcessedDirectory(String processedDirectory) {
        this.processedDirectory = processedDirectory;
    }

    public void setXlsx2csvLocation(String xlsx2csvLocation) {
        this.xlsx2csvLocation = xlsx2csvLocation;
    }

    @Override
    public final boolean doJob() {
        int matchStatus;
        Insurer ins = getSecurityInfoProvider().getCurrentUser().getInsurer();
        if (ins == null) {
            LOG.warn("Claim Matching user '{}' is not associated to an Insurer organisation", getSecurityInfoProvider().getCurrentUser().getUserName());
            return false;
        }

        String insurerName = ins.getName();

        if (!ins.isEnableClaimMatching()) {
            LOG.warn("Claim Matching not enabled for insurer '{}' but scheduler job is active", insurerName);
            return false;
        }

        // First, convert any xlsx files to csv?
        try {
            boolean result;
            File folder = new File(inboundDirectoryBase + "/" + getInboundDirectory(insurerName));
            Collection<File> fileNames = FileUtils.listFiles(folder, new WildcardFileFilter("CHOX_*.xlsx"), null);
            for (File xlsxFile : fileNames) {
                try {
                    result = Xlsx2csvUtility.convert(xlsxFile.getCanonicalPath(), xlsx2csvLocation);
                    LOG.debug("Result of converting file '{}' to csv: {}", xlsxFile.getCanonicalPath(), result);
                } catch (Exception ex) {
                    LOG.error("Exception converting xlsx file '{}' to csv: {}", xlsxFile.getCanonicalPath(), ex.getMessage());
                } finally {
                    FileUtils.moveFileToDirectory(xlsxFile, FileUtils.getFile(processedDirectory), false);
                }
            }
        } catch (IOException ex) {
            LOG.error("Exception converting xlsx files to csv: {}", ex.getMessage());
        }

        try {
            CSVReader reader;
            String[] line;
            // Check mounted inbound directory for *.xlsx/*.csv files
            File folder = new File(inboundDirectoryBase + "/" + getInboundDirectory(insurerName));
            // Check file matches format CHOX_YYYY_MM_DD_HH_MM_SS.csv
            Collection<File> fileNames = FileUtils.listFiles(folder, new WildcardFileFilter("CHOX_*.csv"), null);
            for (File csvFile : fileNames) {
                try {
                    LOG.info("Processing Claim Matching CSV file '{}'", csvFile.getCanonicalPath());
                    int lineNo = 0;
                    // For each file found
                    reader = new CSVReaderBuilder(new FileReader(csvFile))
                            //                                .withFieldAsNull(CSVReaderNullFieldIndicator.EMPTY_SEPARATORS)
                            // Skip the header
                            .withSkipLines(1)
                            .build();
                    while ((line = reader.readNext()) != null) {
                        // Start transaction
                        lineNo++;
                        // Check no of columns
                        if (line.length < 7) {
                            LOG.error("Error processing entry {}: expecting 7 columns but only found '{}'", lineNo, line.length);
                            continue;
                        }
                        // Validate content: all columns must be non-empty, incident date a valid date, and insurer liability between 0 & 100
                        String claimNumber = line[POSITION_CLAIM_NUMBER].trim();
                        if (claimNumber.isEmpty()) {
                            LOG.error("Error processing entry {}: Insurer Claim Number cannot be empty", lineNo);
                            continue;
                        }

                        String insurerName2 = line[POSITION_INSURER_NAME].trim();
                        if (insurerName2.isEmpty()) {
                            LOG.error("Error processing entry {}: Insurer name cannot be empty", lineNo);
                            continue;
                        }
                        if (!insurerName.equals(insurerName2)) {
                            LOG.error("Error processing entry {}: Insurer name '{}' does not match user organisation '{}'", lineNo, insurerName, insurerName2);
                            continue;
                        }

                        String thirdPartyRegistration = line[POSITION_TP_REGISTRATION].trim().replace(" ", "");
                        if (thirdPartyRegistration.isEmpty()) {
                            LOG.error("Error processing entry {}: Insurer name cannot be empty", lineNo);
                            continue;
                        }

                        String incidentDateStr = line[POSITION_INCIDENT_DATE].trim();
                        if (incidentDateStr.isEmpty()) {
                            LOG.error("Error processing entry {}: Incident date cannot be empty", lineNo);
                            continue;
                        }
                        // Check Date Valid
                        Date incidentDate = DateHelper.parseDBDateTime(incidentDateStr);
                        if (incidentDate == null) {
                            LOG.error("Error processing entry {}: Incident date '{}' is not a valid date", lineNo, incidentDateStr);
                            continue;
                        }
                        String indemnityStance = line[POSITION_INDEMNITY_STANCE].trim();
                        if (indemnityStance.isEmpty()) {
                            LOG.error("Error processing entry {}: Incident date cannot be empty", lineNo);
                            continue;
                        }
                        // Check Indemnity Stance is valid
                        String indemnityStanceLwr = indemnityStance.toLowerCase();
                        if (!indemnityStanceLwr.equals("dealing under article 75") && !indemnityStanceLwr.equals("dealing under road traffic act")
                                && !indemnityStanceLwr.equals("no involvement") && !indemnityStanceLwr.equals("not indemnifying")
                                && !indemnityStanceLwr.equals("pending indemnity") && !indemnityStanceLwr.equals("providing indemnity")) {
                            LOG.error("Error processing entry {}: Invalid indemnity stance provided: '{}'", lineNo, indemnityStance);
                            continue;
                        }

                        String liabilityStance = line[POSITION_LIABILITY_STANCE].trim();
                        if (liabilityStance.isEmpty()) {
                            LOG.error("Error processing entry {}: Liability Stance cannot be empty", lineNo);
                            continue;
                        }
                        // Check Liability Stance is valid
                        String liabilityStanceLwr = liabilityStance.toLowerCase();
                        if (!liabilityStanceLwr.equals("liability accepted") && !liabilityStanceLwr.equals("insured at fault")
                                && !liabilityStanceLwr.equals("insured not at fault") && !liabilityStanceLwr.equals("insured partially at fault")) {
                            LOG.error("Error processing entry {}: Invalid liability stance provided: '{}'", lineNo, liabilityStance);
                            continue;
                        }

                        String insurerLiabilityStr = line[POSITION_INSURER_LIABILITY].trim();
                        if (insurerLiabilityStr.isEmpty()) {
                            LOG.error("Error processing entry {}: Insurer liability cannot be empty", lineNo);
                            continue;
                        }
                        BigDecimal insurerLiability;
                        // Check Insurer Liability  is valid
                        try {
                            insurerLiability = new BigDecimal(line[POSITION_INSURER_LIABILITY].trim()).setScale(2);
                            if (insurerLiability.compareTo(BigDecimal.ZERO) < 0 || insurerLiability.compareTo(new BigDecimal("100.00")) > 0) {
                                LOG.error("Error processing entry {}: insurer liability %age must be between 0 and 100: '{}'", lineNo, insurerLiabilityStr);
                                continue;
                            }
                        } catch (Exception ex) {
                            LOG.error("Error processing entry {}: insurer liability %age contains invalid value: '{}'", lineNo, insurerLiabilityStr);
                            continue;
                        }

                        handleHibernateTransactionIntricacies();
                        try {
                            matchStatus = 0;
                            // if already matched, remove
                            ClaimMatchingEntry entry = claimMatchingService.getClaimMatchingEntry(claimNumber);
                            if (entry != null && entry.getMatchStatus() != 0) {
                                continue;
                            }
                            // Find matching claim
                            Claim matchedClaim = null;
                            try {
                                matchedClaim = claimMatchingService.getClaimMatch(incidentDate, thirdPartyRegistration);
                                if (matchedClaim != null) {
                                    LOG.debug("Match found for import entry with insurer number {}: id={}, choRef='{}'", new Object[]{
                                        claimNumber, matchedClaim.getId(), matchedClaim.getChoReference()});
                                    // Claim Matched - start claim matching process
                                    ClaimMatching activity = (ClaimMatching) activityFactory.getActivity("claimMatching");
                                    activity.setClaimNumber(claimNumber);
                                    activity.setIncidentDate(incidentDate);
                                    activity.setIndemnityStance(indemnityStance);
                                    activity.setLiabilityInsurer(insurerLiability);
                                    activity.setLiabilityStance(liabilityStance);
                                    activity.setThirdPartyVehicleRegistration(thirdPartyRegistration);

                                    activity.process(matchedClaim);
                                    matchStatus = matchedClaim.getMatchStatus();
                                } else if (LOG.isDebugEnabled()) {
                                    LOG.debug("No Match found for import entry with insurer number {}", claimNumber);
                                }
                            } catch (Exception ex) {
                                LOG.error("Exception thrown trying to match claim with insurer number {}: ",
                                        new Object[]{claimNumber, ex});
                            }
                            // Progress import entry to claim matching entry
                            // If a claim matching entry already exists, then update
                            if (entry == null) {
                                LOG.debug("Creating new ClaimMatchingEntry");
                                entry = new ClaimMatchingEntry();
                                entry.setClaimNumber(claimNumber);
                                entry.setCreatedDate(new Date());
                                entry.setCreatedBy(getSecurityInfoProvider().getCurrentUser());
                            }
                            entry.setClaim(matchedClaim);
                            entry.setIncidentDate(incidentDate);
                            entry.setIndemnityStance(indemnityStance);
                            entry.setInsurer(ins);
                            entry.setInsurerName(insurerName);
                            entry.setLiabilityInsurer(insurerLiability);
                            entry.setLiabilityStance(liabilityStance);
                            entry.setThirdPartyVehicleRegistration(thirdPartyRegistration);
                            entry.setMatchStatus(matchStatus);
                            entry.setLastModifiedDate(new Date());
                            entry.setLastModifiedBy(getSecurityInfoProvider().getCurrentUser());
                            LOG.debug("Saving ClaimMatchingEntry for claim '{}' with match status={}", claimNumber, matchStatus);
                            claimMatchingService.save(entry);
                        } catch (Exception ex) {
                            LOG.error("IOException thrown processing claim matching file at line {}: {}", lineNo, ex.getMessage());
                        } finally {
                            releaseHibernateSessionConditionally();
                        }
                    }
                } catch (IOException ex) {
                    LOG.error("IOException thrown processing unacknowledged claim file '{}': {}", csvFile.getCanonicalPath(), ex.getMessage());
                } finally {
                    // Move file to 'processed' directory
                    FileUtils.moveFileToDirectory(csvFile, FileUtils.getFile(processedDirectory), false);
                }
            }
        } catch (Exception ex) {
            LOG.error("Exception thrown checking for unacknowledged claim bulk processing: {}", ex.getMessage(), ex);
        }


        // Finally, remove all unmatched claim matching entries over 30 days old
        LocalDate now = LocalDate.now();
        LocalDate thirtyDaysAgo = now.minusDays(30);
        handleHibernateTransactionIntricacies();
        List<ClaimMatchingEntry> claimMatchingEntries = claimMatchingService.getClaimMatchingEntries(insurerName);
        claimMatchingEntries.stream().filter((entry) -> (entry.getMatchStatus() == 0)).forEachOrdered((entry) -> {
            LocalDate lastModifiedDate = entry.getLastModifiedDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            if (lastModifiedDate.isBefore(thirtyDaysAgo)) {
                claimMatchingService.delete(entry);
            }
        });
        releaseHibernateSessionConditionally();

        return true;
    }

    @Override
    protected List<SchedulerJob> getSchedulerJobs() {
        return getSchedulerJobService().getSchedulerJobs(JOB_NAME);
    }

    public void setClaimMatchingService(ClaimMatchingService claimMatchingService) {
        this.claimMatchingService = claimMatchingService;
    }

    public void setActivityFactory(ActivityFactory activityFactory) {
        this.activityFactory = activityFactory;
    }
}
