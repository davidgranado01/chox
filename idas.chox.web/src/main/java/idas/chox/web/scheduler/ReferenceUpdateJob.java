package idas.chox.web.scheduler;

import idas.chox.core.services.ClaimService;
import idas.chox.core.util.DateHelper;
import idas.chox.core.util.EmailHelper;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;

import org.hibernate.HibernateException;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.AccessDeniedException;
import org.springframework.security.annotation.Secured;

public class ReferenceUpdateJob {

    private static final Logger LOG = LoggerFactory.getLogger(ReferenceUpdateJob.class);
    private ImapMailReceiver imapMailReceiver;
    private XlsFileParser xlsFileParser;
    private ClaimService claimService;
    private String emailAccount;
    private String emailAccountPassword;
    private String updateUserName;
    private String updatePassword;
    private MailSecurityAthenticator mailSecurityAthenticator;
    private MailUtil mailUtil;
    private String privilegedUsers;
    private String bccReceivers;
    private String emailSubject;
    private String smtpHostName;
    private String smtpPort;
    private String smtpEmailUser;
    private String smtpEmailPassword;
    private static final String email_date_format = "dd MMMM yyyy";

    protected void execute() throws JobExecutionException {
        String sender = null;
        try {
            InternetAddress internetAddress = new InternetAddress();
            internetAddress.setAddress(emailAccount);
            internetAddress.setPersonal(emailAccountPassword);

            imapMailReceiver.setFrom(internetAddress);

            List<Message> listOfmails = imapMailReceiver.receiveMailsWithAttacment(emailSubject);

            for (Message message : listOfmails) {
                sender = mailUtil.getSender(message);
                if (mailSecurityAthenticator.isPrivilegedSender(mailUtil.parseStringToList(privilegedUsers, ","), sender)) {
                    mailSecurityAthenticator.authenticateSender(updateUserName, updatePassword);
                    readAndUpdateReferenceNumber(imapMailReceiver.fetchAtacchements(message, "xls"));
                } else {
                    sendMail(sender, "Supplier reference update request received from unauthorised user '" + sender + "'", null);
                }
            }

        } catch (UnsupportedEncodingException e) {
            LOG.error("Mail password cannot be decoded: {} ", e.getMessage());
        } catch (AccessDeniedException e) {
            LOG.error("The user is nor authorized to update cho_reference number: {} ", e.getMessage());
        } finally {
            imapMailReceiver.clean();
        }
    }

    @Secured({"ROLE_CHO", "ROLE_CHOX_ADMIN"})
    private void readAndUpdateReferenceNumber(List<InputStream> attachmets) {
        String referenceNumber = null;
        Map<Integer, List<String>> xlsDataMap = null;
        try {
            for (InputStream attachemt : attachmets) {
                xlsDataMap = xlsFileParser.readExcelFile(attachemt);
                Set<Integer> rowNumbers = xlsDataMap.keySet();
                // This is specific for the excel file with two columns and
                // first row is a header.
                // We don't do update on first line and we assume we will always
                // have only two columns.
                for (Integer row : rowNumbers) {
                    // first row is header
                    if (row.intValue() != 0) {
                        List<String> cells = xlsDataMap.get(row);
                        // this excel file should have only two columns and we
                        // iterate only through those two
                        String oldReference = cells.get(0).trim();
                        String newReference = cells.get(1).trim();

                        if (oldReference != null && !oldReference.equals("")) {
                            referenceNumber = oldReference;
                            boolean isUpdateSuccessful = claimService.updateChoReferenceNumber(oldReference, newReference);
                            if (isUpdateSuccessful) {
                                LOG.debug("CHO reference updated: {} -> {}", oldReference, newReference);
                                xlsDataMap.get(row).add("Updated");
                            } else {
                                LOG.debug("Error updating CHO reference: {} -> {}", oldReference, newReference);
                                xlsDataMap.get(row).add("Failed");
                            }

                        }
                    }
                }
            }
        } catch (HibernateException e) {
            LOG.error("Hibernate Exception thrown updating CHO Reference number '{}' : ", referenceNumber, e);
        } finally {
            LOG.debug("Finished updating CHO references - sending email");
            sendMail(imapMailReceiver.getFrom().getAddress(), "RE: " + emailSubject, xlsDataMap);
        }
    }

    String mailMessageConstructor(String email, String subject, Map<Integer, List<String>> xlsDataMap) {
        StringBuilder emailMsg = new StringBuilder();
        emailMsg.append("======================================================================\n");
        emailMsg.append("Submitted By Email: ").append(email);
        emailMsg.append("Date: ").append(DateHelper.getCurrentDateWithFormat(email_date_format));
        emailMsg.append("\n");
        emailMsg.append("======================================================================\n");
        emailMsg.append("Subject: ").append(emailSubject);
        emailMsg.append("\n");
        emailMsg.append("======================================================================\n");
        if (xlsDataMap != null) {
            emailMsg.append("Original CHO Reference     New CHO Reference       Status\n");
            Set<Integer> rowNumbers = xlsDataMap.keySet();
            // This is specific for the excel file with two columns and
            // first row is a header.
            // We don't do update on first line and we assume we will always
            // have only two columns.
            for (Integer row : rowNumbers) {
                if (row.intValue() != 0) {
                    List<String> cells = xlsDataMap.get(row);
                    if (cells.size() >= 1) {
                        emailMsg.append(cells.get(0).trim());
                    }
                    emailMsg.append("\t\t");
                    if (cells.size() >= 2) {
                        emailMsg.append(cells.get(1).trim());
                    }
                    emailMsg.append("\t\t");
                    if (cells.size() >= 3) {
                        emailMsg.append(cells.get(2).trim());
                    }
                    emailMsg.append("\n");
                }
            }
            emailMsg.append("======================================================================\n");
        }
        LOG.debug("Message to send is: \n*********\n{}\n*********", emailMsg.toString());
        return emailMsg.toString();
    }

    public void sendMail(String sender, String subject, Map<Integer, List<String>> xlsDataMap) {
        try {
            EmailHelper emailHelper = new EmailHelper(smtpHostName, smtpPort, smtpEmailUser, smtpEmailPassword);
            String emailMessage = mailMessageConstructor(imapMailReceiver.getFrom().getAddress(), emailSubject, xlsDataMap);
            emailHelper.postMail(subject, emailMessage, (String[]) mailUtil.parseStringToList(bccReceivers, ",").toArray());
        } catch (UnsupportedEncodingException e) {
            LOG.error("Encoding Exception thrown sending email with smtpHostName={}, smtpPort={}, smtpEmailUser={}, smtpEmailPassword={}: ",
                    new Object[]{smtpHostName, smtpPort, smtpEmailUser, smtpEmailPassword, e});
        } catch (MessagingException e) {
            LOG.error("Messaging Exception thrown sending email with smtpHostName={}, smtpPort={}, smtpEmailUser={}, smtpEmailPassword={}: ",
                    new Object[]{smtpHostName, smtpPort, smtpEmailUser, smtpEmailPassword, e});
        }
    }

    public void setImapMailReceiver(ImapMailReceiver imapMailReceiver) {
        this.imapMailReceiver = imapMailReceiver;
    }

    public void setXlsFileParser(XlsFileParser xlsFileParser) {
        this.xlsFileParser = xlsFileParser;
    }

    public void setClaimService(ClaimService claimService) {
        this.claimService = claimService;
    }

    public void setEmailAccount(String emailAccount) {
        this.emailAccount = emailAccount;
    }

    public void setEmailAccountPassword(String emailAccountPassword) {
        this.emailAccountPassword = emailAccountPassword;
    }

    public void setUpdateUserName(String updateUserName) {
        this.updateUserName = updateUserName;
    }

    public void setUpdatePassword(String updatePassword) {
        this.updatePassword = updatePassword;
    }

    public void setMailSecurityAthenticator(
            MailSecurityAthenticator mailSecurityAthenticator) {
        this.mailSecurityAthenticator = mailSecurityAthenticator;
    }

    public void setMailUtil(MailUtil mailUtil) {
        this.mailUtil = mailUtil;
    }

    public void setPrivilegedUsers(String privilegedUsers) {
        this.privilegedUsers = privilegedUsers;
    }

    public void setBccReceivers(String bccReceivers) {
        this.bccReceivers = bccReceivers;
    }

    public void setEmailSubject(String emailSubject) {
        this.emailSubject = emailSubject;
    }

    public void setSmtpHostName(String smtpHostName) {
        this.smtpHostName = smtpHostName;
    }

    public void setSmtpPort(String smtpPort) {
        this.smtpPort = smtpPort;
    }

    public void setSmtpEmailUser(String smtpEmailUser) {
        this.smtpEmailUser = smtpEmailUser;
    }

    public void setSmtpEmailPassword(String smtpEmailPassword) {
        this.smtpEmailPassword = smtpEmailPassword;
    }
}