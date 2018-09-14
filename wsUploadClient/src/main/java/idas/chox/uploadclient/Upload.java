package idas.chox.uploadclient;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.ws.security.wss4j.WSS4JOutInterceptor;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.idaschox.services.chox.UploadService;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;

import idas.chox.uploadclient.activity.AddAttachment;
import idas.chox.uploadclient.activity.CloseClaim;
import idas.chox.uploadclient.activity.ECDUpdate;
import idas.chox.uploadclient.activity.PaymentReceived;
import idas.chox.uploadclient.activity.ReopenClaim;
import idas.chox.uploadclient.activity.UploadBordereau;
import idas.chox.uploadclient.activity.AddNote;
import org.apache.cxf.transport.http.HTTPConduit;

/**
 *
 * @author John
 */
public class Upload {

    private static final String[] LOCATIONS = {"client.xml"};
    private static final Logger LOG = LoggerFactory.getLogger(Upload.class);
    private static final String DEFAULT_USER = "op@cho.com";
    private static final String DEFAULT_PASSWORD = "C0mpliance";

    private static void printUsageAndExit() {
        System.out.println("Usage: java -jar uploadClient.jar [-close|reopen|paymentReceived|ecdUpdate|addNote|addAttachment] [-u <username>] [-p <password>] [-v] (<XML bordereau file> | <CHO ref file> | <CHO reference number> | <ECD Update Excel File> | <Notes Excel File>) | -choRef <choReference>  -category <Attachment Category> [-notify] [-remark <Attachment Remark>] <AttachmentFile>...");
        System.out.println("       Valid attachment categories are: PAYMENT_PACK, TOTAL_LOSS_INSPECTION_CHECK, CHO_S_CLIENT_ALLEGATIONS, INSURER_S_CLIENT_ALLEGATIONS, ENGINEER_S_REPORTS, INVESTIGATOR_REPORTS, REPAIR_DOCUMENTS, REPAIRER_STATEMENT, TOTAL_LOSS_PACK, TOTAL_LOSS_NOTIFICATION, WITNESS_STATEMENT, OTHER, MITIGATION_STATEMENT, INTERVENTION_LETTER, VIDEO_FOOTAGE");
        System.exit(-1);
    }

    public static void main(String[] args) {

        ApplicationContext ctx = new ClassPathXmlApplicationContext(LOCATIONS);
        WSS4JOutInterceptor interceptor = (WSS4JOutInterceptor) ctx.getBean("wss4jOutInterceptor");
        PasswordHolder passwordHolder = (PasswordHolder) ctx.getBean("PasswordHolder");

        Options optionsBean = new Options();
        CmdLineParser parser = new CmdLineParser(optionsBean);

        try {
            parser.parseArgument(args);
        } catch (CmdLineException ex) {
            LOG.error("Error processing command-line arguments: ", ex.getMessage());
            printUsageAndExit();
        }

        String username = optionsBean.getUserName();
        String password = optionsBean.getPassword();
        List<String> fileNames = optionsBean.getArguments();

        if (optionsBean.isVerbose()) {

            LOG.info("Verbose messaging has been activated.");
            LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();

            try {
                JoranConfigurator configurator = new JoranConfigurator();
                configurator.setContext(lc);
                // the context was probably already configured by default configuration rules
                lc.reset();
                InputStream verboseConfigFile = Thread.currentThread().getContextClassLoader().getResourceAsStream("logback-verbose.xml");
                configurator.doConfigure(verboseConfigFile);
            } catch (JoranException je) {
                LOG.error("Error activating verbose messaging: {}", je.getMessage());
                if (je.getCause() != null) {
                    LOG.error("Caused by: {}", je.getCause().getMessage());
                }
//                je.printStackTrace();
            }
        }

        if (username == null || password == null) {

            username = DEFAULT_USER;
            password = DEFAULT_PASSWORD;

            LOG.debug("Username and password were not provided, using  default username='{}'", username);
        }

        if (fileNames == null || fileNames.isEmpty()) {
            LOG.error("No filename or CHO reference has been provided - exiting.");
            printUsageAndExit();
        }

        interceptor.setProperty("user", username);
        passwordHolder.setPassword(password);
        passwordHolder.setUserName(username);

        LOG.debug("Setting username : {}", username);
        LOG.debug("Setting password : {}", password);

        LOG.debug("Getting WS bean...");
        UploadService uploadService = (UploadService) ctx.getBean("uploadService");

        if (optionsBean.isVerbose()) {
            // Add Logging Interceptors for verbose messaging
            LOG.info("Add Logging Interceptors for verbose messaging");
            Client client = ClientProxy.getClient(uploadService);
            client.getInInterceptors().add(new LoggingInInterceptor());
            client.getOutInterceptors().add(new LoggingOutInterceptor());
        }

        // Check if we need to set a proxy
        //   - will need to do this if http.proxyHost and http.proxyPort are defined
        String proxyHost = System.getProperty("http.proxyHost");
        String proxyPortString = System.getProperty("http.proxyPort");
        int proxyPort = -1;
        if (proxyPortString != null && !proxyPortString.isEmpty()) {
            try {
                proxyPort = Integer.valueOf(proxyPortString);
            } catch (NumberFormatException ex) {
                LOG.error("Invalid http.proxyPort setting: {}", proxyPortString);
            }
        }
        if (proxyHost != null && !proxyHost.isEmpty() && proxyPort > 0) {
            LOG.info("Setting proxy host to {} and port to {}", proxyHost, proxyPort);
            Client client = ClientProxy.getClient(uploadService);
            HTTPConduit http = (HTTPConduit) client.getConduit();
            http.getClient().setProxyServer(proxyHost);
            http.getClient().setProxyServerPort(proxyPort);
        }

        for (String fileName : fileNames) {
            LOG.debug("Processing file {}", fileName);
            if (!(new File(fileName)).exists()) {
                // Treat as CHO reference 
                if (optionsBean.isClose()) {
                    LOG.debug("Calling CloseClaim...");
                    CloseClaim.process(uploadService, fileName);
                } else if (optionsBean.isReopen()) {
                    LOG.debug("Calling ReopenClaim...");
                    ReopenClaim.process(uploadService, fileName);
                } else if (optionsBean.isPaymentReceived()) {
                    LOG.debug("Calling PaymentReceived...");
                    PaymentReceived.process(uploadService, fileName);
                } else {
                    printUsageAndExit();
                }

            } else {
                if (optionsBean.isUpdateECD() && fileName.endsWith("xls")) {
                    LOG.debug("Calling ECDUpdate...");
                    ECDUpdate.process(uploadService, fileName);
                } else if (optionsBean.isAddNote() && fileName.endsWith("xls")) {
                    LOG.debug("Calling AddNote...");
                    AddNote.process(uploadService, fileName);
                } else if (optionsBean.isAddAttachment()) {
                    LOG.debug("Calling AddAttachment...");
                    AddAttachment.process(uploadService, optionsBean.getChoRef(), fileName, optionsBean.getCategory(), optionsBean.isAttachmentNotification(), optionsBean.getRemark());
                } else if (optionsBean.isReopen()) {
                    LOG.debug("Calling Reopen Claim...");
                    ReopenClaim.process(uploadService, fileName);
                } else if (optionsBean.isClose()) {
                    LOG.debug("Calling CloseClaim...");
                    CloseClaim.process(uploadService, fileName);
                } else if (optionsBean.isPaymentReceived()) {
                    LOG.debug("Calling PaymentReceived...");
                    PaymentReceived.process(uploadService, fileName);
                } else if (isXmlBordereau(fileName)) {
                    LOG.debug("Calling UploadBordereau...");
                    UploadBordereau.process(uploadService, fileName);
                }
            }
        }
    }

    private static boolean isXmlBordereau(String filename) {
        // Read first line to see if XML file
        try {
            String line;
            try (BufferedReader input = new BufferedReader(new FileReader(new File(filename)))) {
                line = input.readLine();
            }

            if (line != null && line.startsWith("<?xml")) {
                return true;
            }
        } catch (FileNotFoundException ex) {
        } catch (IOException ex) {
        }

        return false;
    }
}
