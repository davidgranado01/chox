package idas.chox.service.workflow.scheduleActivities;

import idas.chox.core.model.*;
import idas.chox.core.security.SecurityInfoProvider;
import idas.chox.core.services.*;
import idas.chox.core.util.DateHelper;
import idas.chox.service.workflow.ActivityFactory;
import idas.chox.service.workflow.activities.AssignOwner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static idas.chox.core.model.ClaimType.INSURER_INVOICE;

public class ClaimReassignment extends BaseScheduleActivity {

    private static final Logger LOG = LoggerFactory.getLogger(ClaimReassignment.class);

    private UserService userService;
    private WorkgroupService workgroupService;
    private ActivityFactory activityFactory;
    private XlsFileParser xlsFileParser;

    public static final int CLAIM_HEADER_ROW = 0;
    public static final int CLAIM_NUMBER_INDEX = 0;
    public static final int CHO_REFERENCE_INDEX = 1;
    public static final int NEW_WORKGROUP_INDEX = 2;
    public static final int NEW_OWNER_WITHOUT_WORKGROUP_INDEX = 2;
    public static final int NEW_OWNER_WITH_WORKGROUP_INDEX = 3;

    public static final String ASSIGN_OWNER_NAME = "assignOwner";
    public static final String NEW_LINE = "\n";
    public static final String CELL_TEMPLATE = "%s\t\t%22s\t\t%22s";

    public static final int CELL_SIZE_WITHOUT_WORKGROUP_AND_STATUS = 4;
    public static final int CELL_SIZE_WITH_WORKGROUP_AND_STATUS = 5;
    public static final int STATUS_INDEX_WITHOUT_WORKGROUP_AND_STATUS = 3;
    public static final int STATUS_INDEX_WITH_WORKGROUP_AND_STATUS = 4;

    private static final int NEW_OWNER_WITHOUT_WORKGROUP_SIZE = 3;
    private static final int NEW_OWNER_WITH_WORKGROUP_SIZE = 4;

    private boolean claimOwnershipEnabled;
    private boolean claimWorkgroupEnabled;
    private boolean invoiceOwnershipEnabled;
    private boolean invoiceWorkgroupEnabled;

    private Map<Integer, List<String>> xlsDataMap;
    private Insurer loggedInUserInsurer;

    public void setXlsFileParser(XlsFileParser xlsFileParser) {
        this.xlsFileParser = xlsFileParser;
    }

    public void setActivityFactory(ActivityFactory activityFactory) {
        this.activityFactory = activityFactory;
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

        Optional<WebUser> webUserOptional = Optional.ofNullable(securityInfoProvider.getCurrentUser());

        if (webUserOptional.isPresent()) {

            // Get the current user
            WebUser loggedInUser = webUserOptional.get();

            boolean insurerPresent = loggedInUser.isAnInsurer();

            boolean choxAdminPresent = loggedInUser.isCHOXAdmin();

            if (insurerPresent || choxAdminPresent) {

                // Get the current insurer
                loggedInUserInsurer = loggedInUser.getInsurer();

                // Invoice configuration
                invoiceOwnershipEnabled = loggedInUserInsurer.isEnableManualInvoiceOwnership();
                invoiceWorkgroupEnabled = loggedInUserInsurer.isEnableManualInvoiceWorkgroups();

                // Claim configuration
                claimOwnershipEnabled = loggedInUserInsurer.isClaimOwnershipEnable();
                claimWorkgroupEnabled = loggedInUserInsurer.isWorkgroupEnable();

            } else {
                status.append("Failed: Logged in user is not an insurer or CHOX admin");
                LOG.warn("Logged in user is not an insurer");
            }

        } else {
            status.append("Failed: Logged in user not found");
            LOG.warn("Logged in user not found");
        }

        // Find the xls claim attachment
        Optional<Map<Integer, List<String>>> xlsDataMapOptional = attachments.stream()
                // Is there a xls attachment
                .filter(attachment -> attachment.getName().toLowerCase().endsWith("xls"))
                // Get the first xls attachment available
                .map(attachment -> xlsFileParser.processExcelFile(attachment.getContent())).findFirst();

        // Is there a xls claim attachment present
        if (xlsDataMapOptional.isPresent()) {

            xlsDataMap = xlsDataMapOptional.get();

            // If the validation passes then process the claim
            if (isStatusEmpty(status)) {

                Set<Integer> rowNumbers = xlsDataMap.keySet();

                // Skip the claim headers
                rowNumbers.stream().filter(rowNumber -> (rowNumber != CLAIM_HEADER_ROW))
                        // Get all cells in each row
                        .map(xlsDataMap::get)
                        // Remove any rows which have cells which are completely empty
                        .filter(cells -> cells.stream().noneMatch(String::isEmpty)).forEach(cells -> {

                    //reset status for each claim
                    status.setLength(0);

                    // Get all the mandatory fields
                    Optional<String> claimNumberOptional = getClaimNumber(cells);
                    Optional<String> choReferenceOptional = getChoReference(cells);

                    AssignOwner activity = (AssignOwner) activityFactory.getActivity(ASSIGN_OWNER_NAME);

                    if (claimNumberOptional.isPresent()) {

                        if (choReferenceOptional.isPresent()) {

                            String claimNumber = claimNumberOptional.get();
                            String choReference = choReferenceOptional.get();

                            // Find the claims that will be reassigned.
                            List<Claim> claims = validateClaimReferenceNumber(choReference, claimNumber, status);

                            // A list will always be returned
                            if (claims.size() > 0) {

                                claims.forEach(claim -> {

                                    if (claim.getClaimType() == INSURER_INVOICE) {
                                        if (invoiceOwnershipEnabled) {
                                            setNewOwnerId(status, cells, activity);
                                        }

                                        if (invoiceWorkgroupEnabled) {
                                            int loggedInsurerId = loggedInUserInsurer.getId();
                                            setNewWorkgroupId(status, cells, loggedInsurerId, activity);
                                        }

                                    } else {
                                        if (claimOwnershipEnabled) {
                                            setNewOwnerId(status, cells, activity);
                                        }

                                        if (claimWorkgroupEnabled) {
                                            int loggedInsurerId = loggedInUserInsurer.getId();
                                            setNewWorkgroupId(status, cells, loggedInsurerId, activity);
                                        }

                                    }

                                    // If the validation passed then reassign the claim
                                    if (isStatusEmpty(status)) {

                                        try {
                                            activity.process(claim);
                                            status.append("Success: Updated.");
                                        } catch (AccessDeniedException ex) {
                                            status.append("Failed: Cannot complete assignment for claim '" + claim.getClaimType().name() + "' in status '" + claim.getStatus() + "'");
                                            LOG.warn("AccessDenied Exception thrown when reassigning owner via email scheduler job");
                                        } catch (Exception ex) {
                                            status.append("Failed: ").append(ex.getMessage());
                                            LOG.warn("Exception occurred when reassigning owner via email scheduler job: {}", ex.getMessage());
                                        }
                                    }

                                });

                            } else {
                                status.append("Failed: Claim not found");
                                LOG.warn("Claim not found");
                            }

                        } else {
                            status.append("Failed: Claim Reference not found");
                            LOG.warn("Claim Reference not found");
                        }

                    } else {
                        status.append("Failed: Claim Number not found");
                        LOG.warn("Claim Number not found");
                    }

                    // Add status to the next available cell in the current row
                    cells.add(status.toString());

                });
            }

        } else {
            throw new Exception("A configuration error has occurred: " + status.toString());
        }

        return true;
    }

    private boolean isStatusEmpty(StringBuilder status) {
        return status.length() == 0;
    }

    private void setNewWorkgroupId(StringBuilder status, List<String> cells, int loggedInsurerId, AssignOwner activity) {

        Optional<String> newWorkgroupOptional = getNewWorkgroup(cells);

        if (newWorkgroupOptional.isPresent()) {
            String newWorkgroupName = newWorkgroupOptional.get();
            // Find the new workgroup
            Optional<Workgroup> optionalNewWorkgroup = Optional.ofNullable(workgroupService.getWorkgroupByName(loggedInsurerId, newWorkgroupName));

            if (optionalNewWorkgroup.isPresent()) {
                Workgroup newWorkgroup = optionalNewWorkgroup.get();
                boolean workgroupActive = newWorkgroup.isStatus();
                if (workgroupActive) {
                    Integer newWorkgroupId = newWorkgroup.getId();
                    activity.setWorkgroupId(newWorkgroupId);
                } else {
                    status.append("Failed: Workgroup is inactive");
                    LOG.warn("Workgroup is inactive");
                }
            } else {
                status.append("Failed: Workgroup not found");
                LOG.warn("Workgroup not found");
            }
        } else {
            status.append("Failed: New Workgroup was not provided");
            LOG.warn("New Workgroup was not provided");
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
            LOG.warn("New Owner Username was not provided");
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
        if (cells.size() == NEW_OWNER_WITHOUT_WORKGROUP_SIZE) {
            String claimNumber = cells.get(NEW_OWNER_WITHOUT_WORKGROUP_INDEX).trim();
            claimNumberOptional = Optional.of(claimNumber);
        } else if (cells.size() == NEW_OWNER_WITH_WORKGROUP_SIZE) {
            String claimNumber = cells.get(NEW_OWNER_WITH_WORKGROUP_INDEX).trim();
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

            emailMsg.append(String.format(CELL_TEMPLATE, "Claim Number", "Supplier Reference", "Message"));
            emailMsg.append(NEW_LINE);
            emailMsg.append("-----------------------------------------------------------------------------------------------");
            emailMsg.append(NEW_LINE);
            Set<Integer> rowNumbers = xlsDataMap.keySet();

            rowNumbers.stream().filter((row) -> (row != CLAIM_HEADER_ROW)).map(xlsDataMap::get).filter((cells) -> (cells.size() >= CELL_SIZE_WITHOUT_WORKGROUP_AND_STATUS)).forEachOrdered((cells) -> {

                // Get the status message
                if (cells.size() == CELL_SIZE_WITHOUT_WORKGROUP_AND_STATUS) {
                    emailMsg.append(String.format(CELL_TEMPLATE, cells.get(CLAIM_NUMBER_INDEX).trim(), cells.get(CHO_REFERENCE_INDEX).trim(), cells.get(STATUS_INDEX_WITHOUT_WORKGROUP_AND_STATUS).trim()));
                } else if (cells.size() == CELL_SIZE_WITH_WORKGROUP_AND_STATUS) {
                    emailMsg.append(String.format(CELL_TEMPLATE, cells.get(CLAIM_NUMBER_INDEX).trim(), cells.get(CHO_REFERENCE_INDEX).trim(), cells.get(STATUS_INDEX_WITH_WORKGROUP_AND_STATUS).trim()));
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
