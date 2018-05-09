package idas.chox.core.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.api.client.repackaged.org.apache.commons.codec.binary.Base64;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.Label;
import com.google.api.services.gmail.model.ListLabelsResponse;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.MessagePart;
import com.google.api.services.gmail.model.MessagePartBody;
import com.google.api.services.gmail.model.MessagePartHeader;
import com.google.api.services.gmail.model.ModifyThreadRequest;
import com.google.api.services.gmail.model.Thread;

/**
 *
 * @author john
 */
public class GmailUtils {

    private static final Logger LOG = LoggerFactory.getLogger(GmailUtils.class);
    private static final String APPLICATION_NAME = "CHOX";
    private static final String SENDER = "chox.automation@idaschox.com";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String CREDENTIALS_FOLDER = "credentials"; // Directory to store user credentials.
    private static final List<String> SCOPES = Arrays.asList(GmailScopes.GMAIL_READONLY, GmailScopes.GMAIL_LABELS,
            GmailScopes.GMAIL_SEND, GmailScopes.GMAIL_INSERT, GmailScopes.GMAIL_COMPOSE, GmailScopes.GMAIL_MODIFY);
    private static final String CLIENT_SECRET_DIR = "client_secret.json";
    private static Gmail service = null;
    private static final Map<String, String> LABEL_MAP = new HashMap<>();
  
    static { // create gmail service
        try {
            // Build a new authorized API client service.
            final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();

            // Creates a new Gmail API client.
            service = new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                    .setApplicationName(APPLICATION_NAME)
                    .build();
            LOG.debug("Gmail service created");
            
            // Load labels and store name/id for later use
            ListLabelsResponse response = service.users().labels().list("me").execute();
            List<Label> labels = response.getLabels();
            labels.stream().filter((label) -> (label.getType().equals("user"))).forEachOrdered((label) -> {
                LABEL_MAP.put(label.getName(), label.getId());
            });


        } catch (FileNotFoundException ex) {
            LOG.error("Gmail API: Gmail Credentials not found: {}", ex.getMessage(), ex);
        } catch (IOException ex) {
            LOG.error("Gmail API: Exception accessing Gmail: {}", ex.getMessage(), ex);
        } catch (GeneralSecurityException ex) {
            LOG.error("Gmail API: GeneralSecurityException accessing Gmail: {}", ex.getMessage(), ex);
        } catch (URISyntaxException ex) {
            LOG.error("Gmail API: URISyntaxException accessing Gmail: {}", ex.getMessage(), ex);
        }
    }

    public static String getLabelId(String description) {
        return LABEL_MAP.get(description);
    }
    
    /**
     * Creates an authorized Credential object.
     *
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If there is no client_secret.
     */
    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException, URISyntaxException {
        // Load client secrets.
        InputStream in = java.lang.Thread.currentThread().getContextClassLoader().getResourceAsStream(CLIENT_SECRET_DIR);
        if (in == null) {
            LOG.error("Gmail API: Cannot open client secret file '{}'", CLIENT_SECRET_DIR);
            throw new IOException("Cannot open client secret file");
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
        // Load the directory as a resource
        URL dir_url = java.lang.Thread.currentThread().getContextClassLoader().getResource(CREDENTIALS_FOLDER);
        // Turn the resource into a File object
        File credentialsDir = new File(dir_url.toURI());

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(credentialsDir))
                .setAccessType("offline")
                .build();

//        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(credentialResourceLocation);
//        FileInputStream fi = new FileInputStream(credentialResourceLocation);
//        if (is == null) {
//            LOG.error("Cannot open credentials from file '{}'", credentialResourceLocation);
//            throw new IOException("Cannot open credentials");
//        }
//        return GoogleCredential.fromStream(is, HTTP_TRANSPORT, JSON_FACTORY).createScoped(SCOPES);
        return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
    }

    synchronized public List<Message> getEmails(String subjectPrefix) {
        List<Message> listOfMails = new ArrayList<>();

        if (service != null) {
            List<Message> unreadMessageIDs = ListMessages("me", "in:inbox is:unread");
            LOG.debug("Gmail API: Found {} unread messages in inbox", unreadMessageIDs.size());

            unreadMessageIDs.stream().map((message) -> GetMessage("me", message.getId())).filter((fullMessage) -> (fullMessage != null)).forEachOrdered((fullMessage) -> {
                if (subjectPrefix != null) {
                    String subject = null;
                    List<MessagePartHeader> headers = fullMessage.getPayload().getHeaders();
                    if (!headers.isEmpty()) {
                        for (MessagePartHeader header : headers) {
                            String name = header.getName();
                            switch (name) {
                                case "Subject":
                                    subject = header.getValue();
                                    break;
                                default:
                                    break;
                            }
                        }
                        if (subject != null && subject.startsWith(subjectPrefix)) {
                            listOfMails.add(fullMessage);
                            LOG.debug("Got message with prefix '{}': {}", subjectPrefix, subject);
                        }
                    }
                } else {
                    listOfMails.add(fullMessage);
                    LOG.debug("Got message: {}", fullMessage.getSnippet());
                }
            });
        }

        return listOfMails;
    }

    synchronized public List<idas.chox.core.model.EmailAttachment> fetchAttachments(Message message) {
        List<idas.chox.core.model.EmailAttachment> listOfAttachements = new ArrayList<>();
        try {
            List<MessagePart> messageParts = message.getPayload().getParts();

            for (MessagePart part : messageParts) {
                if (part.getFilename() != null && part.getFilename().length() > 0) {
                    String filename = part.getFilename();
                    String attId = part.getBody().getAttachmentId();
                    MessagePartBody attachPart = service.users().messages().attachments().
                            get("me", message.getId(), attId).execute();

                    Base64 base64Url = new Base64(true);
                    byte[] fileByteArray = Base64.decodeBase64(attachPart.getData());
                    idas.chox.core.model.EmailAttachment attachment = new idas.chox.core.model.EmailAttachment(filename, fileByteArray);
                    listOfAttachements.add(attachment);
                }
            }
        } catch (IOException e) {
            LOG.warn("Exception fetching attachment: {}", e.getMessage(), e);
        }

        LOG.debug("Found {} attachments", listOfAttachements.size());

        return listOfAttachements;
    }

    private static List<Message> ListMessages(String userId, String query) {
        List<Message> messages = new ArrayList<>();
        ListMessagesResponse response = null;

        try {
            response = service.users().messages().list(userId).setQ(query).execute();
        } catch (IOException ex) {
            LOG.error("Gmail API: IOException accessing Gmail: {}", ex.getMessage(), ex);
        }

        while (response != null && response.getMessages() != null) {
            messages.addAll(response.getMessages());
            if (response.getNextPageToken() != null) {
                String pageToken = response.getNextPageToken();
                try {
                    response = service.users().messages().list(userId).setQ(query)
                            .setPageToken(pageToken).execute();
                } catch (IOException ex) {
                    LOG.error("Gmail API: IOException accessing Gmail: {}", ex.getMessage(), ex);
                }
            } else {
                break;
            }
        }

        return messages;
    }

    private static Message GetMessage(String userId, String messageId) {
        try {
            return service.users().messages().get(userId, messageId).execute();
        } catch (IOException ex) {
            LOG.error("Gmail API: Exception getting message: {}", ex.getMessage(), ex);
        }

        return null;
    }

    /**
     * Modify the Labels applied to a Thread..
     *
     * @param userId User's email address. The special value "me" can be used to indicate the authenticated user.
     * @param threadId Id of the thread within the user's account.
     * @param labelsToAdd List of label ids to add.
     * @param labelsToRemove List of label ids to remove.
     * @throws IOException
     */
    synchronized public static void modifyThread(String userId, String threadId,
            List<String> labelsToAdd, List<String> labelsToRemove) throws IOException {
        ModifyThreadRequest mods = new ModifyThreadRequest().setAddLabelIds(labelsToAdd)
                .setRemoveLabelIds(labelsToRemove);
        Thread thread = service.users().threads().modify(userId, threadId, mods).execute();

        LOG.debug("Thread id={}: {}", thread.getId(), thread.toPrettyString());
    }
    
    private static MimeMessage createEmail(String to, String bccReceiver, String subject, String bodyText)
            throws MessagingException {
        String[] bccReceivers = bccReceiver != null ? bccReceiver.split(",") : null;
        String[] tos = to != null ? to.split(",") : null;
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        MimeMessage email = new MimeMessage(session);

        email.setFrom(new InternetAddress(SENDER));
        if (tos != null) {
            for (String recipient : tos) {
                email.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(recipient));
            }
        }
        if (bccReceivers != null) {
            for (String bcc : bccReceivers) {
                email.addRecipient(javax.mail.Message.RecipientType.BCC, new InternetAddress(bcc));
            }
        }
        email.setSubject(subject);
        email.setText(bodyText);
        return email;
    }

    /**
     * Create a message from an email.
     *
     * @param emailContent Email to be set to raw of message
     * @return a message containing a base64url encoded email
     * @throws IOException
     * @throws MessagingException
     */
    private static Message createMessageWithEmail(MimeMessage emailContent)
            throws MessagingException, IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        emailContent.writeTo(buffer);
        byte[] bytes = buffer.toByteArray();
        String encodedEmail = Base64.encodeBase64URLSafeString(bytes);
        Message message = new Message();
//        message.setRaw(encodedEmail.replaceAll("(?:\\r\\n|\\n\\r|\\n|\\r)", ""));
        message.setRaw(encodedEmail);
        return message;
    }


    synchronized public static Message sendMessage(String to, String bccReceivers, String subject, String bodyText)
            throws MessagingException, IOException {
        MimeMessage mimeMessage = createEmail(to, bccReceivers, subject, bodyText);
        Message message = createMessageWithEmail(mimeMessage);
        message = service.users().messages().send("me", message).execute();

        LOG.info("Message with id={} sent to '{}' (with cc to '{}')", new Object[]{message.getId(), to, bccReceivers});

        return message;
    }}
