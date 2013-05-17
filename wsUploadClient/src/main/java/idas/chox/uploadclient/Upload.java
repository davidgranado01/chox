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

import idas.chox.uploadclient.activity.CloseClaim;
import idas.chox.uploadclient.activity.ECDUpdate;
import idas.chox.uploadclient.activity.PaymentReceived;
import idas.chox.uploadclient.activity.ReopenClaim;
import idas.chox.uploadclient.activity.UploadBordereau;

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
        System.out.println("Usage: java -jar uploadClient.jar [-close|reopen|paymentreceived|ecdupdate] [-u <username>] [-p <password>] [-v] (<XML bordereau file> | <CHO ref file> | <CHO reference number> | <ECD Update Excel File>)...");
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
            Client client = ClientProxy.getClient(uploadService);
            client.getInInterceptors().add(new LoggingInInterceptor());
            client.getOutInterceptors().add(new LoggingOutInterceptor());
        }


        for (String fileName : fileNames) {

            if (!(new File(fileName)).exists()) {
                // Treat as CHO reference 
                if (optionsBean.isClose()) {
                    CloseClaim.process(uploadService, fileName);
                }
                else if (optionsBean.isReopen()) {
                    ReopenClaim.process(uploadService, fileName);
                }
                else if(optionsBean.isPaymentReceived()) {
                    PaymentReceived.process(uploadService, fileName);
                }
                
            } else {
                if (optionsBean.isUpdateECD() && fileName.endsWith("xls")) {
                    ECDUpdate.process(uploadService, fileName);
                }
                else if (optionsBean.isReopen()) {
                    ReopenClaim.process(uploadService, fileName);
                }
                else if (optionsBean.isClose()) {
                    CloseClaim.process(uploadService, fileName);
                }
                else if(optionsBean.isPaymentReceived()) {
                    PaymentReceived.process(uploadService, fileName);
                }
                else if (isXmlBordereau(fileName)) {
                    UploadBordereau.process(uploadService, fileName);
                }
            }
        }
    }

    
    private static boolean isXmlBordereau(String filename) {
        // Read first line to see if XML file
        try {
            BufferedReader input =  new BufferedReader(new FileReader(new File(filename)));
            String line = input.readLine();
            input.close();

            if (line != null && line.startsWith("<?xml")) {
                return true;
            }
        } catch (FileNotFoundException ex) {
        } catch (IOException ex) {
        }
        
        return false;
    }
}
