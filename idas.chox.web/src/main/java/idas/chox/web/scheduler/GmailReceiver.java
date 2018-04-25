package idas.chox.web.scheduler;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

import org.slf4j.LoggerFactory;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;

/**
 *
 * @author john
 */
public class GmailReceiver {

    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(ImapMailReceiver.class);

    public List<Message> receiveMailsWithSubject(final String emailSubject) {
        List<Message> listOfMails = new ArrayList<>();

        try {
//            GoogleCredential credential = GoogleCredential.fromStream(getClass().getResourceAsStream("My Project-694b729fd768.json"))
//                    .createScoped(Collections.singleton(GmailScopes.GMAIL_READONLY));
            InputStream credentialsJSON = getClass().getResourceAsStream("My Project-694b729fd768.json");
            JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
            HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            GoogleCredential credential = GoogleCredential.fromStream(credentialsJSON, httpTransport, JSON_FACTORY);
            LOG.info("Service account id={}", credential.getServiceAccountId());

            // Creates a new Gmail API client.
            Gmail service = new Gmail.Builder(new NetHttpTransport(), new JacksonFactory(), credential).setApplicationName("CHOX").build();
            LOG.info("Gmail service created");
            
            List<Message> unreadMessageIDs = ListMessages(service, "me", "is:unread");
            LOG.info("Found {} messages matching subject '{}'", unreadMessageIDs.size(), emailSubject);

            for (Message message : unreadMessageIDs) {
                Message fullMessage = GetMessage(service, "me", message.getId());
                if (fullMessage != null) {
                    listOfMails.add(fullMessage);
                    LOG.info("Got message with subject '{}' from {}", emailSubject, "unknown");
                }
            }

        } catch (FileNotFoundException ex) {
            LOG.error("Gmail Credentials not found: {}", ex.getMessage());
        } catch (IOException ex) {
            LOG.error("Exception accessing Gmail: {}", ex.getMessage());
        } catch (GeneralSecurityException ex) {
            LOG.error("GeneralSecurityException accessing Gmail: {}", ex.getMessage());
        }

        return listOfMails;
    }

    public List<EmailAttachment> fetchAttachments(Object content,
            String fileFormat) {

        List<EmailAttachment> listOfAttachements = new ArrayList<>();
        return listOfAttachements;
    }

    public void clean() {
    }

    private static List<Message> ListMessages(Gmail service, String userId, String query) throws IOException {
        ListMessagesResponse response = service.users().messages().list(userId).setQ(query).execute();

        List<Message> messages = new ArrayList<>();
        while (response.getMessages() != null) {
            messages.addAll(response.getMessages());
            if (response.getNextPageToken() != null) {
                String pageToken = response.getNextPageToken();
                response = service.users().messages().list(userId).setQ(query)
                        .setPageToken(pageToken).execute();
            } else {
                break;
            }
        }

        return messages;
    }

    private static Message GetMessage(Gmail service, String userId, String messageId) {
        try {
            return service.users().messages().get(userId, messageId).execute();
        } catch (IOException ex) {
            LOG.error("Exception getting message: {}", ex.getMessage());
        }

        return null;
    }

}
