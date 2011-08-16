package idas.chox.uploadclient;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import com.idaschox.services.chox.Result;
import com.idaschox.services.chox.UploadService;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
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

/**
 *
 * @author John
 */
public class PaymentReceived {

    private static final String[] LOCATIONS = {"client.xml"};
    private static final Logger LOG = LoggerFactory.getLogger(PaymentReceived.class);
    private static final String DEFAULT_USER = "op@cho.com";
    private static final String DEFAULT_PASSWORD = "C0mpliance";
    private static UploadService uploadService;

    private static void printUsageAndExit() {
        System.err.println("Usage: java -jar paymentReceived.jar [-u <username>] [-p <password>] <supplierRef>|<filename>");
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
//            parser.printUsage(System.err);
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
            LOG.error("No filename has been provided - exiting.");
            printUsageAndExit();
        } else if (fileNames.size() > 1) {
            LOG.error("Too many arguments.");
            printUsageAndExit();
        }


        interceptor.setProperty("user", username);
        passwordHolder.setPassword(password);
        passwordHolder.setUserName(username);

        LOG.debug("Setting username : {}", username);
        LOG.debug("Setting password : {}", password);

        LOG.debug("Getting WS bean...");
        uploadService = (UploadService) ctx.getBean("uploadBordereau");

        if (optionsBean.isVerbose()) {
            // Add Logging Interceptors for verbose messaging
            Client client = ClientProxy.getClient(uploadService);
            client.getInInterceptors().add(new LoggingInInterceptor());
            client.getOutInterceptors().add(new LoggingOutInterceptor());
        }

        String file = fileNames.get(0);

        File filename = new File(file);

        if (filename.exists()) {
            // Argument is a file containing CHO reference numbers
            LOG.info("Processing file '{}'", file);
            try {
                FileInputStream fstream = new FileInputStream(file);
                // Get the object of DataInputStream
                DataInputStream in = new DataInputStream(fstream);
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                String choReference;
                //Read File Line By Line
                while ((choReference = br.readLine()) != null) {
                    updateClaimToPaymentReceived(choReference);
                }
                //Close the input stream
                in.close();
            } catch (Exception e) {//Catch exception if any
                LOG.error("Error processing input file '{}': ", file, e.getMessage());
            }

        } else {
            // Argument is a CHO Reference number
            LOG.info("Processing CHO reference '{}'", file);
            updateClaimToPaymentReceived(file);
        }

    }

    public static void updateClaimToPaymentReceived(String choReference) {
        Result result = null;

        LOG.debug("Calling paymentReceived Web Service for claim with CHO reference '{}'...", choReference);
        try {
            result = uploadService.paymentReceived(choReference);
        } catch (Exception ex) {
            LOG.error("Error calling paymentReceived web service: {}", ex.getMessage());
            if (ex.getCause() != null) {
                LOG.error("Caused by: {}", ex.getCause().getMessage());
            }
            return;
        }

        if (!result.isStatus()) {
            LOG.error("Error updating claim '{}' to payment received: {}", choReference, result.getErrorMessage());
        } else {
            LOG.info("Claim with CHO reference '{}' has been updated to 'PaymentReceived'.", choReference);
        }
    }
}
