package idas.chox.web.scheduler;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import idas.chox.core.model.Chorganisation;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.Insurer;
import idas.chox.core.model.LiabilityStatus;
import idas.chox.core.model.ReasonOfRejection;
import idas.chox.core.model.SchedulerJob;
import idas.chox.core.model.WebUser;
import idas.chox.core.model.Workgroup;
import idas.chox.core.services.ChorganisationService;
import idas.chox.core.services.ReasonOfRejectionService;
import idas.chox.core.services.UserService;
import idas.chox.core.services.WorkgroupService;
import idas.chox.core.util.DateHelper;
import idas.chox.core.util.Xlsx2csvUtility;
import idas.chox.service.workflow.ActivityFactory;
import idas.chox.service.workflow.activities.AcknowledgeClaim;
import idas.chox.service.workflow.activities.AssignOwner;
import idas.chox.service.workflow.activities.AssignWorkgroup;
import idas.chox.service.workflow.activities.ClaimPending;
import idas.chox.service.workflow.activities.ClaimRejection;

/**
 *
 * @author john
 */
public class UnacknowledgedClaimsBulkProcessing extends DbSchedulerJob {

    private static final Logger LOG = LoggerFactory.getLogger(UnacknowledgedClaimsBulkProcessing.class);
    private static final int POSITION_CHO_REFERENCE = 0;
    private static final int POSITION_CHO_NAME = 1;
    private static final int POSITION_CLAIM_NUMBER = 2;
    private static final int POSITION_WORKGROUP = 3;
    private static final int POSITION_OWNER = 4;
    private static final int POSITION_LIABILITY_STATUS = 5;
    private static final int POSITION_LIABILITY_NOTE = 6;
    private static final int POSITION_LIABILITY_INSURER = 7;
    private static final int POSITION_LIABILITY_AGREED_DATE = 8;
    private static final int POSITION_INDEMNITY_STANCE = 9;
    private static final int POSITION_COPLEY_FLAG = 10;
    private static final int POSITION_COPLEY_DATE = 11;
    private static final int POSITION_INVOICE_REVIEW_FLAG = 12;
    private static final int POSITION_INVOICE_REVIEW_REASON = 13;
    private static final int POSITION_ACTION = 14;
    private static final int POSITION_REJECTION_REASON = 15;
    private static final int POSITION_REJECTION_NOTE = 16;
    public static final String JOB_NAME = "UNACKNOWLEDGED_CLAIM_BULK_PROCESSING";
    private ActivityFactory activityFactory;
    private String inboundDirectoryBase;
    private String processedDirectory;
    private String xlsx2csvLocation;
    private WorkgroupService workgroupService;
    private UserService userService;
    private ChorganisationService chorganisationService;
    private ReasonOfRejectionService reasonOfRejectionService;

    public void setActivityFactory(ActivityFactory activityFactory) {
        this.activityFactory = activityFactory;
    }

    public void setWorkgroupService(WorkgroupService workgroupService) {
        this.workgroupService = workgroupService;
    }

    public void setReasonOfRejectionService(ReasonOfRejectionService reasonOfRejectionService) {
        this.reasonOfRejectionService = reasonOfRejectionService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setChorganisationService(ChorganisationService chorganisationService) {
        this.chorganisationService = chorganisationService;
    }

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
        Insurer ins = getSecurityInfoProvider().getCurrentUser().getInsurer();

        if (!ins.isUnacknowldegedClaimBulkProcessing()) {
            LOG.warn("Unacknowledged Claim Bulk Processing for insurer '{}' but scheduler job is active", ins.getName());
            return false;
        }

        // First, convert any xlsx files to csv?
        try {
            boolean result;
            File folder = new File(inboundDirectoryBase + "/" + getInboundDirectory(ins.getName()));
            Collection<File> fileNames = FileUtils.listFiles(folder, new WildcardFileFilter("CREDIT_HIRE_NEW_NOTIFICATION_ACTIONS_*.xlsx"), null);
            LOG.debug("Found {} files in folder '{}'", fileNames.size(), folder.getAbsolutePath());
            for (File xlsxFile : fileNames) {
                try {
                    result = Xlsx2csvUtility.convert(xlsxFile.getCanonicalPath(), xlsx2csvLocation);
                    LOG.debug("Result of converting file '{}' to csv: {}", xlsxFile.getCanonicalPath(), result);
                } catch (Exception ex) {
                    LOG.error("Exception converting xlsx file '{}' to csv: {}", xlsxFile.getCanonicalPath(), ex.getMessage());
                } finally {
                    try {
                        FileUtils.moveFileToDirectory(xlsxFile, FileUtils.getFile(processedDirectory), false);
                    } catch (IOException ex) {
                        LOG.error("Exception moving xlsx file '{}' to processed directory: {}", xlsxFile.getAbsolutePath(), ex.getMessage());
                        FileUtils.deleteQuietly(xlsxFile);
                    }
                }
            }
        } catch (IOException ex) {
            LOG.error("Exception converting xlsx files to csv: {}", ex.getMessage());
        }

        try {
            CSVReader reader;
            String[] line;
            // Check mounted inbound directory for *.xlsx/*.csv files
            File folder = new File(inboundDirectoryBase + "/" + getInboundDirectory(ins.getName()));
            // Check file matches format NEW_NOTICIATION_ACTIONS_<DDMMYYYYHHMM>.csv
            Collection<File> fileNames = FileUtils.listFiles(folder, new WildcardFileFilter("CREDIT_HIRE_NEW_NOTIFICATION_ACTIONS_*.csv"), null);
            for (File csvFile : fileNames) {
                try {
                    LOG.info("Processing Unacknowledged Claims CSV file '{}'", csvFile.getCanonicalPath());
                    int lineNo = 0;
                    // For each file found
                    reader = new CSVReaderBuilder(new FileReader(csvFile))
                            //                                .withFieldAsNull(CSVReaderNullFieldIndicator.EMPTY_SEPARATORS)
                            // Skip the header
                            .withSkipLines(1)
                            .build();
                    while ((line = reader.readNext()) != null) {
                        // Start transaction
                        handleHibernateTransactionIntricacies();
                        lineNo++;
                        // Check no of columns
                        if (line.length < 17) {
                            LOG.error("Error processing entry {}: expecting 17 columns but only found '{}'", lineNo, line.length);
                            releaseHibernateSessionConditionally();
                            continue;
                        }
                        // Get CHO
                        Chorganisation cho = chorganisationService.getChorgByName(line[POSITION_CHO_NAME].trim());
                        if (cho == null) {
                            LOG.error("Error processing entry {}: no such CHO '{}'", lineNo, line[POSITION_CHO_NAME]);
                            releaseHibernateSessionConditionally();
                            continue;
                        }

                        // Get Claim
                        Claim claim = claimService.getClaimByChoIdAndCHOReferenceNumber(cho.getId(), line[POSITION_CHO_REFERENCE].trim());
                        if (claim == null) {
                            LOG.error("Error processing entry {}: no such claim '{}'", lineNo, line[POSITION_CHO_REFERENCE]);
                            releaseHibernateSessionConditionally();
                            continue;
                        }
                        // Check Action
                        String action = line[POSITION_ACTION].trim().toLowerCase();
                        if (!action.equals("acknowledge") && !action.equals("reject") && !action.equals("pending")) {
                            LOG.error("Error processing entry {}: invalid action provided '{}'", lineNo, action);
                            releaseHibernateSessionConditionally();
                            continue;
                        }

                        // First, do we need to route?
                        if (claim.getStatus().equals(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNROUTED)) {
                            // Claim Needs To be routed: get workgroup from column 4
                            Workgroup workgroup = workgroupService.getWorkgroupByName(claim.getInsurer().getId(), line[POSITION_WORKGROUP]);
                            if (workgroup == null) {
                                LOG.error("Error processing entry {}: cannot acknowledge claim as no such workgroup '{}'", lineNo, line[POSITION_WORKGROUP]);
                                releaseHibernateSessionConditionally();
                                continue;
                            }
                            // Route Claim
                            AssignWorkgroup activity = (AssignWorkgroup) activityFactory.getActivity("assignWorkgroup");
                            activity.setWorkgroupId(workgroup.getId());
                            try {
                                activity.process(claim);
                            } catch (Exception ex) {
                                LOG.error("Exception processing entry {}: cannot assign workgroup: '{}'", lineNo, ex.getMessage());
                                releaseHibernateSessionConditionally();
                                continue;
                            }
                        }

                        // Do we need to assign?
                        if (claim.getStatus().equals(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNASSIGNED)) {
                            // Claim Needs To be routed: get workgroup from column 5
                            WebUser owner = userService.findByUserName(line[POSITION_OWNER].trim());
                            if (owner == null) {
                                LOG.error("Error processing entry {}: cannot assign claim as no such username '{}'", lineNo, line[POSITION_OWNER]);
                                releaseHibernateSessionConditionally();
                                continue;
                            }
                            AssignOwner activity = (AssignOwner) activityFactory.getActivity("assignOwner");
                            activity.setClaimOwnerId(owner.getId());
                            activity.setOasWorkgroupId(claim.getWorkgroup().getId());
                            try {
                                activity.process(claim);
                            } catch (Exception ex) {
                                LOG.error("Exception processing entry {}: cannot assign owner: '{}'", lineNo, ex.getMessage());
                                releaseHibernateSessionConditionally();
                                continue;
                            }
                        }

                        // Now process if status of claim is ClaimUnacknowledgedRouted
                        if (claim.getStatus().equals(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED)) {
                            try {
                                // Check Mandatory Fields for all actions first
                                // Check Claim Number
                                String claimNumber = line[POSITION_CLAIM_NUMBER].trim();
                                if (claimNumber.isEmpty()) {
                                    LOG.error("Error processing entry {}: Insurer Claim Number cannot be empty", lineNo);
                                    releaseHibernateSessionConditionally();
                                    continue;
                                }

                                // Check Liability
                                LiabilityStatus liabilityStatus;
                                try {
                                    liabilityStatus = LiabilityStatus.getLiabilityStatus(line[POSITION_LIABILITY_STATUS].trim());
                                    if (liabilityStatus == null || liabilityStatus == LiabilityStatus.LIABILITY_NULL)
                                        throw new Exception("No Liability stance Provided");
                                } catch (Exception ex) {
                                    LOG.error("Error processing entry {}: invalid liability stance provided: '{}'", lineNo, line[POSITION_LIABILITY_STATUS].trim());
                                    releaseHibernateSessionConditionally();
                                    continue;
                                }

                                Date liabilityAgreedDate;
                                if (line[POSITION_LIABILITY_AGREED_DATE].trim().isEmpty()) {
                                    liabilityAgreedDate = DateHelper.getCurrentDate();
                                } else {
                                    liabilityAgreedDate = DateHelper.parse(line[POSITION_LIABILITY_AGREED_DATE].trim());
                                }
                                if (liabilityAgreedDate == null && (liabilityStatus == LiabilityStatus.LIABILITY_ACCEPTED
                                        || liabilityStatus == LiabilityStatus.PROCEED_WITHOUT_PREJUDICE || liabilityStatus == LiabilityStatus.LIABILITY_SPLIT)) {
                                    LOG.error("Error processing entry {}: liability date field contains invalid value: '{}'", lineNo, line[POSITION_LIABILITY_AGREED_DATE].trim());
                                    releaseHibernateSessionConditionally();
                                    continue;
                                }
                                String indemnityStance = line[POSITION_INDEMNITY_STANCE].trim();

                                switch (action) {
                                    case "acknowledge":
                                        // Check Indemnity
                                        if (indemnityStance.isEmpty()) {
                                            LOG.error("Error processing entry {}: no indemnity stance provided", lineNo);
                                            releaseHibernateSessionConditionally();
                                            continue;
                                        }
                                        if (!indemnityStance.equals("Dealing Under Article 75") && !indemnityStance.equals("Dealing Under Road Traffic Act")
                                                && !indemnityStance.equals("No Involvement") && !indemnityStance.equals("Not Indemnifying")
                                                && !indemnityStance.equals("Pending Indemnity") && !indemnityStance.equals("Providing Indemnity")) {
                                            LOG.error("Error processing entry {}: invalid indemnity stance provided: '{}'", lineNo, indemnityStance);
                                            releaseHibernateSessionConditionally();
                                            continue;
                                        }

                                        // Acknowledge Claim
                                        AcknowledgeClaim acknowledgeActivity = (AcknowledgeClaim) activityFactory.getActivity("acknowledgeClaim");
                                        acknowledgeActivity.setClaimNumber(claimNumber);
                                        acknowledgeActivity.setIndemnityStance(indemnityStance);
//                                      acknowledgeActivity.setIndemnityAmount(indemnityAmount);
                                        acknowledgeActivity.setLiabilityStatus(liabilityStatus);
                                        acknowledgeActivity.setLiabilityAgreedDate(liabilityAgreedDate);

                                        // Check Copley field
                                        if (claim.getInsurer().isCopleyQuestion()) {
                                            String copleyStr = line[POSITION_COPLEY_FLAG].toLowerCase().trim();
                                            Boolean copleyQuestion = null;
                                            if (copleyStr.equals("yes") || copleyStr.equals("true")) {
                                                copleyQuestion = Boolean.TRUE;
                                            }
                                            if (copleyStr.equals("no") || copleyStr.equals("false")) {
                                                copleyQuestion = Boolean.FALSE;
                                            }
                                            if (copleyQuestion == null) {
                                                LOG.error("Error processing entry {}: copley field contains invalid value: '{}'", lineNo, line[POSITION_COPLEY_FLAG]);
                                                releaseHibernateSessionConditionally();
                                                continue;
                                            }
                                            acknowledgeActivity.setCopleyOfferMade(copleyQuestion);
                                            if (copleyQuestion) {
                                                Date copleyDate;
                                                if (line[POSITION_COPLEY_DATE].trim().isEmpty()) {
                                                    copleyDate = DateHelper.getCurrentDate();
                                                } else {
                                                    copleyDate = DateHelper.parse(line[POSITION_COPLEY_DATE].trim());
                                                }
                                                if (copleyDate == null) {
                                                    LOG.error("Error processing entry {}: copley date field contains invalid value: '{}'", lineNo, line[POSITION_COPLEY_DATE]);
                                                    releaseHibernateSessionConditionally();
                                                    continue;
                                                }
                                                acknowledgeActivity.setCopleyOfferMadeDate(copleyDate);
                                            }
                                        }
                                        BigDecimal insurerLiability;
                                        if (liabilityStatus == LiabilityStatus.LIABILITY_SPLIT) {
                                            try {
                                                insurerLiability = new BigDecimal(line[POSITION_LIABILITY_INSURER].trim()).setScale(2);
                                            } catch (Exception ex) {
                                                LOG.error("Error processing entry {}: insurer liability %age contains invalid value: '{}'", lineNo, line[POSITION_LIABILITY_INSURER].trim());
                                                releaseHibernateSessionConditionally();
                                                continue;
                                            }
                                            // Need Supporting Liability Notes
                                            if (line[POSITION_LIABILITY_NOTE].trim().isEmpty()) {
                                                LOG.error("Error processing entry {}: must provide supporting notes when liability split", lineNo);
                                                releaseHibernateSessionConditionally();
                                                continue;
                                            }
                                            acknowledgeActivity.setSupportingLiabilityNotes(line[POSITION_LIABILITY_NOTE].trim());
                                        } else if (liabilityStatus == LiabilityStatus.LIABILITY_ACCEPTED || liabilityStatus == LiabilityStatus.PROCEED_WITHOUT_PREJUDICE) {
                                            insurerLiability = new BigDecimal("100.00");
                                            if (!line[6].trim().isEmpty()) {
                                                acknowledgeActivity.setSupportingLiabilityNotes(line[POSITION_LIABILITY_NOTE].trim());
                                            }
                                        } else {
                                            if (line[POSITION_LIABILITY_NOTE].trim().isEmpty()) {
                                                LOG.error("Error processing entry {}: must provide supporting notes when liability is repudiated, in negotiation or unknown", lineNo);
                                                releaseHibernateSessionConditionally();
                                                continue;
                                            }
                                            insurerLiability = BigDecimal.ZERO;
                                            acknowledgeActivity.setSupportingLiabilityNotes(line[POSITION_LIABILITY_NOTE].trim());
                                        }
                                        acknowledgeActivity.setPercentageLiabilityAccepted(insurerLiability);
                                        acknowledgeActivity.setPercentageLiabilityCho((new BigDecimal("100.00")).subtract(insurerLiability));

                                        // Invoice Review
                                        String invoiceReviewStr = line[POSITION_INVOICE_REVIEW_FLAG].toLowerCase().trim();
                                        Boolean invoiceReview = null;
                                        if (invoiceReviewStr.equals("yes") || invoiceReviewStr.equals("true")) {
                                            invoiceReview = Boolean.TRUE;
                                        }
                                        if (invoiceReviewStr.equals("no") || invoiceReviewStr.equals("false")) {
                                            invoiceReview = Boolean.FALSE;
                                        }
                                        if (invoiceReview != null) {
                                            acknowledgeActivity.setIsInvoiceReviewRequired(invoiceReview);
                                            if (invoiceReview) { // must have reason
                                                String invoiceReviewReason = line[POSITION_INVOICE_REVIEW_REASON].trim();
                                                if (invoiceReviewReason.isEmpty()) {
                                                    LOG.error("Error processing entry {}: no invoice review reason provded", lineNo);
                                                    releaseHibernateSessionConditionally();
                                                    continue;
                                                }
                                                if (!invoiceReviewReason.equals("Intervention") && !invoiceReviewReason.equals("Claims Investigation")
                                                        && !invoiceReviewReason.equals("Indemnity") && !invoiceReviewReason.equals("Quantum/causation issue")
                                                        && !invoiceReviewReason.equals("Handler concerns")) {
                                                    LOG.error("Error processing entry {}: invalid invoice review reason provded: '{}'", lineNo, invoiceReviewReason);
                                                    releaseHibernateSessionConditionally();
                                                    continue;
                                                }
                                                acknowledgeActivity.setInvoiceReviewReason(invoiceReviewReason);
                                            }
                                        }

                                        try {
                                            acknowledgeActivity.process(claim);
                                        } catch (Exception ex) {
                                            LOG.error("Error acknowledging entry {}: {}", lineNo, ex.getMessage());
                                        } finally {
                                            releaseHibernateSessionConditionally();
                                        }
                                        break;

                                    case "reject":
                                        ClaimRejection rejectActivity = (ClaimRejection) activityFactory.getActivity("rejectClaim");
                                        // Check Indemnity
                                        if (!indemnityStance.isEmpty()) {
                                            if (!indemnityStance.equals("Dealing Under Article 75") && !indemnityStance.equals("Dealing Under Road Traffic Act")
                                                    && !indemnityStance.equals("No Involvement") && !indemnityStance.equals("Not Indemnifying")
                                                    && !indemnityStance.equals("Pending Indemnity") && !indemnityStance.equals("Providing Indemnity")) {
                                                LOG.error("Error processing entry {}: invalid indemnity stance provided: '{}'", lineNo, indemnityStance);
                                                releaseHibernateSessionConditionally();
                                                continue;
                                            }
                                            rejectActivity.setIndemnityStance(indemnityStance);
                                        }
                                        // Reject Claim
                                        rejectActivity.setClaimNumber(claimNumber);
//                                      rejectActivity.setIndemnityAmount(indemnityAmount);
                                        rejectActivity.setLiabilityStatus(liabilityStatus);
                                        rejectActivity.setLiabilityAgreedDate(liabilityAgreedDate);
                                        if (liabilityStatus == LiabilityStatus.LIABILITY_SPLIT) {
                                            try {
                                                insurerLiability = new BigDecimal(line[POSITION_LIABILITY_INSURER].trim()).setScale(2);
                                            } catch (Exception ex) {
                                                LOG.error("Error processing entry {}: insurer liability %age contains invalid value: '{}'", lineNo, line[POSITION_LIABILITY_INSURER].trim());
                                                releaseHibernateSessionConditionally();
                                                continue;
                                            }
                                            // Need Supporting Liability Notes
                                            if (line[POSITION_LIABILITY_NOTE].trim().isEmpty()) {
                                                LOG.error("Error processing entry {}: must provide supporting notes when liability split", lineNo);
                                                releaseHibernateSessionConditionally();
                                                continue;
                                            }
                                            rejectActivity.setSupportingLiabilityNotes(line[POSITION_LIABILITY_NOTE].trim());
                                        } else if (liabilityStatus == LiabilityStatus.LIABILITY_ACCEPTED || liabilityStatus == LiabilityStatus.PROCEED_WITHOUT_PREJUDICE) {
                                            insurerLiability = new BigDecimal("100.00");
                                            if (!line[POSITION_LIABILITY_NOTE].trim().isEmpty()) {
                                                rejectActivity.setSupportingLiabilityNotes(line[POSITION_LIABILITY_NOTE].trim());
                                            }
                                        } else {
                                            if (line[POSITION_LIABILITY_NOTE].trim().isEmpty()) {
                                                LOG.error("Error processing entry {}: must provide supporting notes when liability is repudiated, in negotiation or unknown", lineNo);
                                                releaseHibernateSessionConditionally();
                                                continue;
                                            }
                                            insurerLiability = BigDecimal.ZERO;
                                            rejectActivity.setSupportingLiabilityNotes(line[POSITION_LIABILITY_NOTE].trim());
                                        }
                                        rejectActivity.setPercentageLiabilityAccepted(insurerLiability);
                                        rejectActivity.setPercentageLiabilityCho((new BigDecimal("100.00")).subtract(insurerLiability));
                                        // Invoice Review
                                        invoiceReviewStr = line[POSITION_INVOICE_REVIEW_FLAG].toLowerCase().trim();
                                        invoiceReview = null;
                                        if (invoiceReviewStr.equals("yes") || invoiceReviewStr.equals("true")) {
                                            invoiceReview = Boolean.TRUE;
                                        }
                                        if (invoiceReviewStr.equals("no") || invoiceReviewStr.equals("false")) {
                                            invoiceReview = Boolean.FALSE;
                                        }
                                        if (invoiceReview != null) {
                                            rejectActivity.setIsInvoiceReviewRequired(invoiceReview);
                                            if (invoiceReview) { // must have reason
                                                String invoiceReviewReason = line[POSITION_INVOICE_REVIEW_REASON].trim();
                                                if (invoiceReviewReason.isEmpty()) {
                                                    LOG.warn("Error processing entry {}: no invoice review reason provded", lineNo);
                                                } else if (!invoiceReviewReason.equals("Intervention") && !invoiceReviewReason.equals("Claims Investigation")
                                                        && !invoiceReviewReason.equals("Indemnity") && !invoiceReviewReason.equals("Quantum/causation issue")
                                                        && !invoiceReviewReason.equals("Handler concerns")) {
                                                    LOG.warn("Error processing entry {}: invalid invoice review reason provded: '{}'", lineNo, invoiceReviewReason);
                                                } else {
                                                    rejectActivity.setInvoiceReviewReason(invoiceReviewReason);
                                                }
                                            }
                                        }

                                        // Rejection
                                        String rejectionReason = line[POSITION_REJECTION_REASON].trim();
                                        if (rejectionReason.isEmpty()) {
                                            LOG.error("Error processing entry {}: no rejection reason provided", lineNo);
                                            releaseHibernateSessionConditionally();
                                            continue;
                                        }
                                        List<ReasonOfRejection> rejectionReasons = reasonOfRejectionService.getInsurerReasons(
                                                claim.getInsurer().getId(), ReasonOfRejection.TYPE_CLAIM,
                                                claim.getClaimType(), true, null);
                                        Integer rejectionReasonId = null;
                                        for (ReasonOfRejection reason : rejectionReasons) {
                                            if (reason.getRorName().equals(rejectionReason)) {
                                                rejectionReasonId = reason.getId();
                                                break;
                                            }
                                        }
                                        if (rejectionReasonId == null) {
                                            LOG.error("Error processing entry {}: invalid rejection reason provided '{}'", lineNo, rejectionReason);
                                            releaseHibernateSessionConditionally();
                                            continue;
                                        }
                                        rejectActivity.setReasonOfRejectionId(rejectionReasonId);
                                        if (line[POSITION_REJECTION_NOTE].trim().isEmpty()) {
                                            LOG.error("Error processing entry {}: no supporting rejection notes provided", lineNo);
                                            releaseHibernateSessionConditionally();
                                            continue;
                                        }
                                        rejectActivity.setRejectionDescription(line[POSITION_REJECTION_NOTE].trim());
                                        try {
                                            rejectActivity.process(claim);
                                        } catch (Exception ex) {
                                            LOG.error("Error rejecting entry {}: {}", lineNo, ex.getMessage());
                                        } finally {
                                            releaseHibernateSessionConditionally();
                                        }
                                        break;

                                    case "pending":
                                        // Pending Claim
                                        ClaimPending pendingActivity = (ClaimPending) activityFactory.getActivity("pending");
                                        // Check Indemnity
                                        if (!indemnityStance.isEmpty()) {
                                            if (!indemnityStance.equals("Dealing Under Article 75") && !indemnityStance.equals("Dealing Under Road Traffic Act")
                                                    && !indemnityStance.equals("No Involvement") && !indemnityStance.equals("Not Indemnifying")
                                                    && !indemnityStance.equals("Pending Indemnity") && !indemnityStance.equals("Providing Indemnity")) {
                                                LOG.error("Error processing entry {}: invalid indemnity stance provided: '{}'", lineNo, indemnityStance);
                                                releaseHibernateSessionConditionally();
                                                continue;
                                            }
                                            pendingActivity.setIndemnityStance(indemnityStance);
                                        }
                                        pendingActivity.setClaimNumber(claimNumber);
//                                      pendingActivity.setIndemnityAmount(indemnityAmount);
                                        pendingActivity.setLiabilityStatus(liabilityStatus);
                                        pendingActivity.setLiabilityAgreedDate(liabilityAgreedDate);
                                        if (liabilityStatus == LiabilityStatus.LIABILITY_SPLIT) {
                                            try {
                                                insurerLiability = new BigDecimal(line[POSITION_LIABILITY_INSURER].trim()).setScale(2);
                                            } catch (Exception ex) {
                                                LOG.error("Error processing entry {}: insurer liability %age contains invalid value: '{}'", lineNo, line[POSITION_LIABILITY_INSURER].trim());
                                                releaseHibernateSessionConditionally();
                                                continue;
                                            }
                                            // Need Supporting Liability Notes
                                            if (line[POSITION_LIABILITY_NOTE].trim().isEmpty()) {
                                                LOG.error("Error processing entry {}: must provide supporting notes when liability split", lineNo);
                                                releaseHibernateSessionConditionally();
                                                continue;
                                            }
                                            pendingActivity.setSupportingLiabilityNotes(line[POSITION_LIABILITY_NOTE].trim());
                                        } else if (liabilityStatus == LiabilityStatus.LIABILITY_ACCEPTED || liabilityStatus == LiabilityStatus.PROCEED_WITHOUT_PREJUDICE) {
                                            insurerLiability = new BigDecimal("100.00");
                                            if (!line[POSITION_LIABILITY_NOTE].trim().isEmpty()) {
                                                pendingActivity.setSupportingLiabilityNotes(line[POSITION_LIABILITY_NOTE].trim());
                                            }
                                        } else {
                                            if (line[POSITION_LIABILITY_NOTE].trim().isEmpty()) {
                                                LOG.error("Error processing entry {}: must provide supporting notes when liability is repudiated, in negotiation or unknown", lineNo);
                                                releaseHibernateSessionConditionally();
                                                continue;
                                            }
                                            insurerLiability = BigDecimal.ZERO;
                                            pendingActivity.setSupportingLiabilityNotes(line[POSITION_LIABILITY_NOTE].trim());
                                        }
                                        pendingActivity.setPercentageLiabilityAccepted(insurerLiability);
                                        pendingActivity.setPercentageLiabilityCho((new BigDecimal("100.00")).subtract(insurerLiability));
                                        // Invoice Review
                                        invoiceReviewStr = line[POSITION_INVOICE_REVIEW_FLAG].toLowerCase().trim();
                                        invoiceReview = null;
                                        if (invoiceReviewStr.equals("yes") || invoiceReviewStr.equals("true")) {
                                            invoiceReview = Boolean.TRUE;
                                        }
                                        if (invoiceReviewStr.equals("no") || invoiceReviewStr.equals("false")) {
                                            invoiceReview = Boolean.FALSE;
                                        }
                                        if (invoiceReview != null) {
                                            pendingActivity.setIsInvoiceReviewRequired(invoiceReview);
                                            if (invoiceReview) { // must have reason
                                                String invoiceReviewReason = line[POSITION_INVOICE_REVIEW_REASON].trim();
                                                if (invoiceReviewReason.isEmpty()) {
                                                    LOG.warn("Error processing entry {}: no invoice review reason provded", lineNo);
                                                } else if (!invoiceReviewReason.equals("Intervention") && !invoiceReviewReason.equals("Claims Investigation")
                                                        && !invoiceReviewReason.equals("Indemnity") && !invoiceReviewReason.equals("Quantum/causation issue")
                                                        && !invoiceReviewReason.equals("Handler concerns")) {
                                                    LOG.warn("Error processing entry {}: invalid invoice review reason provded: '{}'", lineNo, invoiceReviewReason);
                                                } else {
                                                    pendingActivity.setInvoiceReviewReason(invoiceReviewReason);
                                                }
                                            }
                                        }

                                        try {
                                            pendingActivity.process(claim);
                                        } catch (Exception ex) {
                                            LOG.error("Error rejecting entry {}: {}", lineNo, ex.getMessage());
                                        } finally {
                                            releaseHibernateSessionConditionally();
                                        }
                                        break;

                                    default:
                                        LOG.error("Error processing entry {}: invalid action '{}'", lineNo, line[POSITION_ACTION]);
                                        break;
                                }
                            } catch (Exception ex) {
                                LOG.error("Exception thrown processing entry {} (claim '{}'): {}", lineNo, line[POSITION_CHO_REFERENCE], ex.getMessage());
                            }
                        } else {
                            releaseHibernateSessionConditionally();
                        }
                    }
                } catch (IOException ex) {
                    LOG.error("IOException thrown processing unacknowledged claim file '{}': {}", csvFile.getCanonicalPath(), ex.getMessage());
                } finally {
                    // Move file to 'processed' directory
                    try {
                        FileUtils.moveFileToDirectory(csvFile, FileUtils.getFile(processedDirectory), false);
                    } catch (Exception ex) {
                        LOG.error("Exception thrown moving csv file '{}': {}", csvFile.getAbsolutePath(), ex.getMessage());
                        String newFile = processedDirectory + "/" + csvFile.getName().concat(".1");
                        for (int i=1; i<10; i++) {
                            newFile = newFile.substring(0, newFile.length() - 1) + (char)(i + '0');
                            try {
                                FileUtils.moveFile(csvFile, new File(newFile));
                                LOG.info("CSV file '{}' renamed to '{}'", csvFile.getAbsolutePath(), newFile);
                                break;
                            } catch (Exception ex2) {
//                                LOG.error("Exception thrown moving csv file '{}' to '{}': {}", csvFile.getAbsolutePath(), newFile, ex2.getMessage());
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            LOG.error("Exception thrown checking for unacknowledged claim bulk processing: {}", ex.getMessage(), ex);
        }

        return true;
    }

    @Override
    protected List<SchedulerJob> getSchedulerJobs() {
        return getSchedulerJobService().getSchedulerJobs(JOB_NAME);
    }
}
