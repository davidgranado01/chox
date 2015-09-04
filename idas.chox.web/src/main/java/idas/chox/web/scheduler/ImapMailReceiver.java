package idas.chox.web.scheduler;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.search.SearchTerm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImapMailReceiver {

    private static final Logger LOG = LoggerFactory.getLogger(ImapMailReceiver.class);
    private String mailHost;
    private String mailStoreProtocol;
    private Folder folder;
    private Store store;
    private String mailPort;
    private String mailFolder;
    private String emailAccount;
    private String emailAccountPassword;

    /**
     * Retrieves mail from given mail account. This function retrieves only unseen mail with the given email subject.
     *
     * @param emailSubject - used as a search criteria for emails.
     * @return List<> - list of mails
     */
    public List<Message> receiveMailsWithSubject(final String emailSubject) {

        List<Message> listOfMails = null;

        Properties props = System.getProperties();
        props.setProperty("mail.imaps.port", mailPort);
        props.setProperty("mail.store.protocol", mailStoreProtocol);
        LOG.debug("Properties are not set. Properties will be set by default to: {}:{}", mailStoreProtocol, mailPort);

        try {
            Session session = Session.getDefaultInstance(props, null);
            store = session.getStore();
            LOG.debug("Connecting to store: mailHost={}, fromAddress={}, fromPersonal={}", new Object[]{mailHost, emailAccount, emailAccountPassword});
            store.connect(mailHost, emailAccount, emailAccountPassword);
            LOG.trace("Getting folder '{}'", mailFolder);
            folder = store.getFolder(mailFolder);

            if (folder == null || folder.getName() == null) {
                folder = store.getFolder("INBOX");
                LOG.debug("Mail Folder is not set. Folder will be set to 'INBOX'.");
            }

            folder.open(Folder.READ_WRITE);
            LOG.trace("Folder '{}' is open", folder.getFullName());

            SearchTerm searchTerm = new SearchTerm() {

                private static final long serialVersionUID = -6675113729056193548L;

                @Override
                public boolean match(Message message) {
                    if (message != null) {
                        try {
                            //we search for all unseen mails starting with given subject
                            if (!message.isSet(Flags.Flag.SEEN)) {
                                if (emailSubject == null ? true : message.getSubject() != null ? message.getSubject().trim().replace(" ", "").toLowerCase().startsWith(emailSubject.trim().replace(" ", "").toLowerCase()) : false) {
                                    return true;
                                }
                            }
                        } catch (MessagingException ex) {
                            ex.printStackTrace();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                    return false;
                }
            };

            LOG.debug("Searching for messages....");
            Message[] messages = folder.search(searchTerm);
            LOG.debug("Found {} unseen messages with subject '{}'", messages.length, emailSubject);

            listOfMails = Arrays.asList(messages);

        } catch (NoSuchProviderException e) {
            LOG.warn("Given mail properties are not correct: {}\n", e.getMessage(), e);
        } catch (MessagingException e) {
            LOG.warn("Cannot make connecection to the given host: {}\n", e.getMessage(), e);
        } catch (Exception e) {
            LOG.warn("Cannot retrieve attachments: {}\n", e.getMessage(), e);
        }
        return listOfMails == null ? new ArrayList<Message>() : listOfMails;
    }

    public List<EmailAttachment> fetchAttachments(Object content,
            String fileFormat) {

        List<EmailAttachment> listOfAttachements = new ArrayList<>();
        try {
            if (content instanceof Multipart) {
                Multipart mp = (Multipart) content;
//                MimeBodyPart mp2 = (MimeBodyPart) content;
                
                for (int i = 0, n = mp.getCount(); i < n; i++) {
                    BodyPart part = mp.getBodyPart(i);
                    LOG.debug("Disposition is {}", part.getDisposition());
                    
                    if (part.getContent() instanceof Multipart) {
                        // part-within-a-part - recurse
                        LOG.trace("Found part-within-a-part - recursing.....");
                        listOfAttachements.addAll(fetchAttachments(part.getContent(), fileFormat));
                    } else {
                        String fileName = part.getFileName();
                        LOG.debug("Found file '{}' with contentType='{}' - matching to format '{}'",
                                new Object[]{fileName, mp.getContentType(), fileFormat});
                        if (fileName != null && fileName.endsWith(fileFormat)) {
                            LOG.debug("Adding attachment {}", fileName);
                            listOfAttachements.add(new EmailAttachment(fileName, (InputStream) part.getInputStream(), part.getSize()));
                        }
                    }
                }
            }
        } catch (MessagingException e) {
            LOG.warn("Error fetching attachment - cannot make connection to the given host: {} ",
                    e.getMessage(), e);
        } catch (IOException e) {
            LOG.warn("Error fetching attachment - cannot retrive attachment: {} ", e.getMessage(), e);
        }

        LOG.debug("Found {} attachments of format '{}'", listOfAttachements.size(), fileFormat);

        return listOfAttachements;
    }

    public void clean() {
        try {
            if (folder != null && folder.isOpen()) {
                folder.close(true);
            }
            if (store != null && store.isConnected()) {
                store.close();
            }
        } catch (MessagingException e) {
            LOG.warn("Cannot close the mail folder: {} ", e.getMessage(), e);
        }

    }

    public void setMailHost(String mailHost) {
        this.mailHost = mailHost;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public void setMailStoreProtocol(String mailStoreProtocol) {
        this.mailStoreProtocol = mailStoreProtocol;
    }

    public void setMailPort(String mailPort) {
        this.mailPort = mailPort;
    }

    public void setMailFolder(String mailFolder) {
        this.mailFolder = mailFolder;
    }

    public void setEmailAccount(String emailAccount) {
        this.emailAccount = emailAccount;
    }

    public void setEmailAccountPassword(String emailAccountPassword) {
        this.emailAccountPassword = emailAccountPassword;
    }
}
