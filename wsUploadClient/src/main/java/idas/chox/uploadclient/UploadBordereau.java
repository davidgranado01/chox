package idas.chox.uploadclient;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import com.idaschox.services.chox.Chox;
import com.idaschox.services.chox.SubmissionResult;
import com.idaschox.services.chox.UploadService;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import java.io.IOException;
import java.io.File;
import java.io.FilenameFilter;
import javax.xml.bind.JAXBContext;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import org.kohsuke.args4j.CmdLineException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.InputStream;
import java.util.List;
import org.apache.cxf.ws.security.wss4j.WSS4JOutInterceptor;
import org.kohsuke.args4j.CmdLineParser;

/**
 * John
 *
 */
public class UploadBordereau {

    private static final String[] LOCATIONS = {"client.xml"};
    private static final Logger LOG = LoggerFactory.getLogger(UploadBordereau.class);
    private static final String DEFAULT_USER = "op@cho.com";
    private static final String DEFAULT_PASSWORD = "C0mpliance";

    private static void printUsageAndExit() {
        System.out.println("Usage: java -jar uploadClient.jar [-u <username>] [-p <password>] [-v] <XML bordereau file>");
        System.exit(-1);
    }

    public static void main(String[] args) {

        ApplicationContext ctx = new ClassPathXmlApplicationContext(LOCATIONS);
        WSS4JOutInterceptor interceptor = (WSS4JOutInterceptor) ctx.getBean("wss4jOutInterceptor");
        PasswordHolder passwordHolder = (PasswordHolder) ctx.getBean("PasswordHolder");
        SubmissionResult result = null;

        String splitXsl = "split.xsl";
        String output = "output.xml";

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
        UploadService uploadService = (UploadService) ctx.getBean("uploadBordereau");

        if (optionsBean.isVerbose()) {
            // Add Logging Interceptors for verbose messaging
            Client client = ClientProxy.getClient(uploadService);
            client.getInInterceptors().add(new LoggingInInterceptor());
            client.getOutInterceptors().add(new LoggingOutInterceptor());
        }

        InputStream splitXslStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(splitXsl);

        File dir = new File(".");
        FilenameFilter filter = new MyFilter("splitInput-", "xml");
        File[] filenames = dir.listFiles(filter);
        for (int i = 0; i < filenames.length; i++) {
            filenames[i].delete();
        }

        for (String fileName : fileNames) {

            if (!(new File(fileName)).exists()) {
                LOG.error("Input file '{}' does not exist.", fileName);
                continue;
            } else {

                LOG.info("Splitting input file '{}' into individual claims", fileName);
                try {
                    transform(fileName, splitXslStream, output);
                } catch (TransformerConfigurationException ex) {
                    LOG.error("Error transforming XML: " + ex.getMessage());
                    continue;
                } catch (TransformerException ex) {
                    LOG.error("Error transforming XML: " + ex.getMessage());
                    continue;
                }

                filenames = dir.listFiles(filter);

                LOG.info("Found {} claims in file '{}'.", filenames.length, fileName);
                for (int i = 0; i < filenames.length; i++) {
                    String filename = null;

                    try {
                        filename = filenames[i].getCanonicalPath();
                    } catch (IOException ex) {
                        LOG.error("Error processing claim file '{}': {}", filename, ex.getMessage());
                        if (ex.getCause() != null) {
                            LOG.error("Caused by: {}", ex.getCause().getMessage());
                        }
                        continue;
                    }
                    LOG.debug("Processing claim file: '" + filename + "'");


                    File tmpFile = filenames[i];

                    JAXBContext jaxbContext;
                    Chox chox = null;

                    if (tmpFile.exists()) {

                        LOG.debug("Upload file created: '" + tmpFile.getPath() + "'");
                        String xml2Upload = ReadTextFile.getContents(tmpFile);
                        LOG.trace("    Contents of file to be uploaded:\n<<<<<<<<<<<<< start >>>>>>>>>>>>>\n{}"
                                + "\n<<<<<<<<<<<<<  End  >>>>>>>>>>>>>", xml2Upload);

                        try {

                            jaxbContext = JAXBContext.newInstance("com.idaschox.services.chox");
                            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

                            JAXBElement<Chox> choxElement = (JAXBElement<Chox>) unmarshaller.unmarshal(new StreamSource(tmpFile), Chox.class);
                            chox = choxElement.getValue();
                            LOG.trace("Got Chox element: {}", chox);


                        } catch (JAXBException ex) {
                            LOG.error("Error loading xml: '{}'", ex.getMessage());
                            if (ex.getCause() != null) {
                                LOG.error("Caused by: {}", ex.getCause().getMessage());
                            }
                            continue;
                        }

                    }

                    LOG.info("Calling uploadBordereau Web Service for claim with CHO reference '{}'...", chox.getRental().getSupplierReference());
                    try {
                        result = uploadService.uploadBordereau(chox);
                    } catch (Exception ex) {
                        LOG.error("Error calling uploadBordereau web service: {}", ex.getMessage());
                        if (ex.getCause() != null) {
                            LOG.error("Caused by: {}", ex.getCause().getMessage());
                        }
                        continue;
                    }
                    finally {
                        tmpFile.delete();
                    }

                    LOG.info("Returned with status: {}", result.isStatus());
                    LOG.info("    Process Status: {}", result.getProcessStatus());
                    LOG.info("    Claim Status: {}", result.getClaimStatus());
                    LOG.info("    Upload Status: {}", result.getUploadStatus());
                    if (result.getMessages() != null) {
                        List<String> messages = result.getMessages().getMessages();
                        for (String m : messages) {
                            LOG.info("    Message: {}", m);
                        }
                    }

//                    System.out.println("\n");
//                    System.out.println("Result for supplier reference : " + chox.getRental().getSupplierReference());
//                    System.out.println("\n");
//                    System.out.println("process status : " + result.getProcessStatus());
//                    System.out.println("  claim status : " + result.getClaimStatus());
//                    System.out.println(" upload status : " + result.getUploadStatus());
//                    System.out.println(" Error Message : " + result.getMessages().getMessages().get(0));
//                    System.out.println("\n");
                }

            }

        }
        
        File f = new File(output);
        if (f.exists())
            f.delete();
    }

    private static void transform(String inXML, InputStream inXSL, String outTXT)
            throws TransformerConfigurationException,
            TransformerException {

        TransformerFactory factory = TransformerFactory.newInstance();

        StreamSource xslStream = new StreamSource(inXSL);
        Transformer transformer = factory.newTransformer(xslStream);
        transformer.setErrorListener(new MyErrorListener());

        StreamSource in = new StreamSource(inXML);
        StreamResult out = new StreamResult(outTXT);
        transformer.transform(in, out);
    }
}
