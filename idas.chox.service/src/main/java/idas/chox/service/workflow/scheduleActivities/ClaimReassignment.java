package idas.chox.service.workflow.scheduleActivities;

import idas.chox.core.model.*;
import idas.chox.core.security.SecurityInfoProvider;
import idas.chox.core.services.*;
import idas.chox.core.util.DateHelper;
import idas.chox.service.workflow.ActivityFactory;
import idas.chox.service.workflow.activities.AssignOwner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.nio.file.AccessDeniedException;
import java.util.*;

import static idas.chox.core.model.ClaimType.INSURER_CLAIM;
import static idas.chox.core.model.ClaimType.INSURER_INVOICE;

public class ClaimReassignment extends BaseScheduleActivity {

    private static final Logger LOG = LoggerFactory.getLogger(ClaimReassignment.class);
    private AttachmentService attachmentService;
    private AttachmentTypeService attachmentTypeService;
    private Map<Integer, List<String>> xlsDataMap;
    private ActivityFactory activityFactory;
    private XlsFileParser xlsFileParser;
    private UserService userService;
    private WorkgroupService workgroupService;

    public static final String NEW_LINE = "\n";
    public static final int CELL_SIZE_WITHOUT_WORKGROUP_AND_STATUS = 4;
    public static final int CELL_SIZE_WITH_WORKGROUP_AND_STATUS = 5;
    public static final int ERROR_INDEX_WITHOUT_WORKGROUP_AND_STATUS = 3;
    public static final int ERROR_INDEX_WITH_WORKGROUP_AND_STATUS = 4;
    public static final String CELL_TEMPLATE = "%s\t\t%22s";
    public static final int CLAIM_HEADER_ROW = 0;
    public static final int CLAIM_NUMBER_INDEX = 0;
    public static final int CHO_REFERENCE_INDEX = 1;
    public static final int NEW_WORKGROUP_INDEX = 2;
    public static final int NEW_OWNER_WITHOUT_WORKGROUP_INDEX = 2;
    public static final int NEW_OWNER_WITH_WORKGROUP_INDEX = 3;

    private boolean claimOwnershipEnabled;
    private boolean claimWorkgroupEnabled;
    private boolean invoiceOwnershipEnabled;
    private boolean invoiceWorkgroupEnabled;

    @Autowired
    protected InsurerService insurerService;
    private Insurer loggedInUserInsurer;


    public void setXlsFileParser(XlsFileParser xlsFileParser) {
        this.xlsFileParser = xlsFileParser;
    }

    public void setActivityFactory(ActivityFactory activityFactory) {
        this.activityFactory = activityFactory;
    }

    public void setAttachmentService(AttachmentService attachmentService) {
        this.attachmentService = attachmentService;
    }

    public void setAttachmentTypeService(AttachmentTypeService attachmentTypeService) {
        this.attachmentTypeService = attachmentTypeService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setWorkgroupService(WorkgroupService workgroupService) {
        this.workgroupService = workgroupService;
    }

    @Override
    public boolean process(String body, List<EmailAttachment> attachments, String from, String subject) throws Exception {

        StringBuilder status = new StringBuilder();

        // Get the security provider
        SecurityInfoProvider securityInfoProvider = getWorkflowContext().getSecurityInfoProvider();

        // Get the current user
        WebUser loggedInUser = securityInfoProvider.getCurrentUser();

        boolean insurerPresent = loggedInUser.isAnInsurer();

        if (insurerPresent) {

            // Get the current insurer
            loggedInUserInsurer = loggedInUser.getInsurer();

            // Claim configuration
            claimOwnershipEnabled = loggedInUserInsurer.isClaimOwnershipEnable();

            claimWorkgroupEnabled = loggedInUserInsurer.isEnableManualInvoiceWorkgroups();

            // Invoice configuration
            invoiceOwnershipEnabled = loggedInUserInsurer.isEnableManualInvoiceOwnership();

            invoiceWorkgroupEnabled = loggedInUserInsurer.isEnableManualInvoiceWorkgroups();

        } else {
            status.append("Failed: Claim does not belong to an insurer");
            return true;
        }

        attachments.stream().filter(attachment -> attachment.getName().toLowerCase().endsWith("xls")).forEach(attachment -> {

            xlsDataMap = xlsFileParser.processExcelFile(attachment.getContent());

            Set<Integer> rowNumbers = xlsDataMap.keySet();

            // Skip the headers
            rowNumbers.stream().filter(rowNumber -> (rowNumber != CLAIM_HEADER_ROW))
                    // Get all cells in each
                    .map(xlsDataMap::get).forEach(cells -> {

                // Get all the mandatory fields
                Optional<String> claimNumberOptional = getClaimNumber(cells);
                Optional<String> choReferenceOptional = getChoReference(cells);

                // Reset status
                status.setLength(0);

                AssignOwner activity = (AssignOwner) activityFactory.getActivity("assignOwner");

                if (claimNumberOptional.isPresent()) {

                    if (choReferenceOptional.isPresent()) {

                        String claimNumber = claimNumberOptional.get();
                        String choReference = choReferenceOptional.get();

                        // Find the claims that will be reassigned
                        Optional<List<Claim>> optionalClaim = Optional.ofNullable(validateClaimReferenceNumber(choReference, claimNumber, status));

                        if (optionalClaim.isPresent()) {

                            List<Claim> claims = optionalClaim.get();

                            if (claims.size() > 0) {

                                claims.forEach(claim -> {

                                    if (claim.getClaimType() == INSURER_INVOICE) {
                                        if (invoiceOwnershipEnabled) {
                                            setNewOwnerId(status, cells, activity);
                                        }

                                        if (invoiceWorkgroupEnabled) {
                                            setNewWorkgroupId(status, cells, loggedInUserInsurer, activity);
                                        }

                                    } else if (claim.getClaimType() == INSURER_CLAIM) {
                                        if (claimOwnershipEnabled) {
                                            setNewOwnerId(status, cells, activity);
                                        }

                                        if (claimWorkgroupEnabled) {
                                            setNewWorkgroupId(status, cells, loggedInUserInsurer, activity);
                                        }

                                    } else {
                                        status.append("Failed: Invalid claim type");
                                    }

                                    // If validation passed, process AssignOwner activity
                                    if (status.toString().isEmpty()) {

                                        try {
                                            activity.process(claim);
                                            status.append("Success: Updated.");
                                        } catch (AccessDeniedException ex) {
                                            status.append("Failed: No Access to AssignOwner Activity (Invalid Claim Status '").append(claim.getStatus()).append("')");
                                            LOG.warn("AccessDenied Exception thrown when reassigning owner via email scheduler job");
                                        } catch (Exception ex) {
                                            status.append("Failed: ").append(ex.getMessage());
                                            LOG.warn("Exception occurred when reassigning owner via email scheduler job: {}", ex.getMessage());
                                        }

                                    }

                                });

                            } else {
                                status.append("Failed: Claim not found");
                            }

                        }

                    } else {
                        status.append("Failed: Claim Reference not found");
                    }

                } else {
                    status.append("Failed: Claim Number not found");
                }

                cells.add(status.toString());

            });

        });

        return true;
    }

    private void setNewWorkgroupId(StringBuilder status, List<String> cells, Insurer loggedInUserInsurer, AssignOwner activity) {

        Optional<String> newWorkgroupOptional = getNewWorkgroup(cells);

        if (newWorkgroupOptional.isPresent()) {
            String newWorkgroupName = newWorkgroupOptional.get();
            // Find the new workgroup
            int loggedInsurerId = loggedInUserInsurer.getId();
            Optional<Workgroup> optionalNewWorkgroup = Optional.ofNullable(workgroupService.getWorkgroupByName(loggedInsurerId, newWorkgroupName));

            if (optionalNewWorkgroup.isPresent()) {
                Workgroup newWorkgroup = optionalNewWorkgroup.get();
                Integer newWorkgroupId = newWorkgroup.getId();
                activity.setWorkgroupId(newWorkgroupId);
            } else {
                status.append("Failed: Workgroup not found");
                LOG.warn("Web user not found");
            }
        } else {
            status.append("Failed: New Workgroup was not provided");
            LOG.warn("Web user not found");
        }
    }

    private void setNewOwnerId(StringBuilder status, List<String> cells, AssignOwner activity) {

        Optional<String> newOwnerOptional = getNewOwner(cells);

        if (newOwnerOptional.isPresent()) {
            String newOwnerUsername = newOwnerOptional.get();
            // Find the new owner
            Optional<WebUser> optionalNewOwnerWebUser = Optional.ofNullable(userService.findByUserName(newOwnerUsername));

            if (optionalNewOwnerWebUser.isPresent()) {
                WebUser newOwnerWebUser = optionalNewOwnerWebUser.get();
                Integer newOwnerWebUserId = newOwnerWebUser.getId();
                activity.setClaimOwnerId(newOwnerWebUserId);
            } else {
                status.append("Failed: Web user not found");
                LOG.warn("Web user not found");
            }
        } else {
            status.append("Failed: New Owner Username was not provided");
            LOG.warn("Web user not found");
        }
    }

    public Optional<String> getClaimNumber(List<String> cells) {
        Optional<String> claimNumberOptional = Optional.empty();
        String claimNumber = cells.get(CLAIM_NUMBER_INDEX).trim();
        if (!claimNumber.isEmpty()) {
            claimNumberOptional = Optional.of(claimNumber);
        }
        return claimNumberOptional;
    }

    public Optional<String> getChoReference(List<String> cells) {
        Optional<String> claimNumberOptional = Optional.empty();
        String claimNumber = cells.get(CHO_REFERENCE_INDEX).trim();
        if (!claimNumber.isEmpty()) {
            claimNumberOptional = Optional.of(claimNumber);
        }
        return claimNumberOptional;
    }

    public Optional<String> getNewOwner(List<String> cells) {
        Optional<String> claimNumberOptional = Optional.empty();
        String claimNumber = "";
        if (cells.size() == 3) {
            claimNumber = cells.get(NEW_OWNER_WITHOUT_WORKGROUP_INDEX).trim();
        } else if (cells.size() == 4) {
            claimNumber = cells.get(NEW_OWNER_WITH_WORKGROUP_INDEX).trim();
        }
        if (!claimNumber.isEmpty()) {
            claimNumberOptional = Optional.of(claimNumber);
        }
        return claimNumberOptional;
    }

    public Optional<String> getNewWorkgroup(List<String> cells) {
        Optional<String> claimNumberOptional = Optional.empty();
        String claimNumber = cells.get(NEW_WORKGROUP_INDEX).trim();
        if (!claimNumber.isEmpty()) {
            claimNumberOptional = Optional.of(claimNumber);
        }
        return claimNumberOptional;
    }

    @Override
    public String getResponse(String subject, String from) {
        StringBuilder emailMsg = new StringBuilder();

        emailMsg.append("======================================================================");
        emailMsg.append(NEW_LINE);
        emailMsg.append("Submitted By Email: ").append(from);
        emailMsg.append(NEW_LINE);
        emailMsg.append("Date: ").append(DateHelper.getCurrentDateWithFormat(EMAIL_DATE_FORMAT));
        emailMsg.append(NEW_LINE);
        emailMsg.append("Subject: ").append(subject);
        emailMsg.append(NEW_LINE);
        emailMsg.append("======================================================================");
        emailMsg.append(NEW_LINE);
        emailMsg.append(NEW_LINE);

        Optional<Map<Integer, List<String>>> optionalXmlData = Optional.ofNullable(xlsDataMap);

        if (optionalXmlData.isPresent()) {

            emailMsg.append(String.format(CELL_TEMPLATE, "ReferenceNumber", "Message"));
            emailMsg.append(NEW_LINE);
            emailMsg.append("-----------------------------------------------------------------------------------------------");
            emailMsg.append(NEW_LINE);
            Set<Integer> rowNumbers = xlsDataMap.keySet();

            rowNumbers.stream().filter((row) -> (row != CLAIM_HEADER_ROW)).map(xlsDataMap::get).filter((cells) -> (cells.size() >= CELL_SIZE_WITHOUT_WORKGROUP_AND_STATUS)).forEachOrdered((cells) -> {

                // Get the status message
                if (cells.size() == CELL_SIZE_WITHOUT_WORKGROUP_AND_STATUS) {
                    emailMsg.append(String.format(CELL_TEMPLATE, cells.get(CLAIM_NUMBER_INDEX).trim(), cells.get(ERROR_INDEX_WITHOUT_WORKGROUP_AND_STATUS).trim()));
                } else if (cells.size() == CELL_SIZE_WITH_WORKGROUP_AND_STATUS) {
                    emailMsg.append(String.format(CELL_TEMPLATE, cells.get(CLAIM_NUMBER_INDEX).trim(), cells.get(ERROR_INDEX_WITH_WORKGROUP_AND_STATUS).trim()));
                }

                // Append new line
                emailMsg.append(NEW_LINE);
            });

        } else {
            emailMsg.append("No xls attachment found in email, please check and re-submit.");
            emailMsg.append(NEW_LINE);
            emailMsg.append("-----------------------------------------------------------------------------------------------");
            emailMsg.append(NEW_LINE);
        }

        LOG.debug("Message to send is:" + NEW_LINE + "*********" + NEW_LINE + "{}" + NEW_LINE + "*********", emailMsg.toString());
        return emailMsg.toString();
    }

}
