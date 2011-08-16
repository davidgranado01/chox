package idas.chox.uploadclient;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import com.idaschox.services.chox.Chox;
import com.idaschox.services.chox.Result;
import com.idaschox.services.chox.UploadService;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
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
import javax.xml.bind.Unmarshaller;

/**
 *
 * @author John
 */
public class ReceivePayment {

    private static final String[] LOCATIONS = {"client.xml"};
    static final Logger LOG = LoggerFactory.getLogger(ReceivePayment.class);

    private static void printUsage() {
        System.err.println("Usage: [-u] UserName [-p] Password bordereau-XML-file_Location or supplier reference number");
    }

    public static void main(String[] args) {

        ApplicationContext ctx = new ClassPathXmlApplicationContext(LOCATIONS);
        WSS4JOutInterceptor interceptor = (WSS4JOutInterceptor) ctx.getBean("wss4jOutInterceptor");
        PasswordHolder passwordHolder = (PasswordHolder) ctx.getBean("PasswordHolder");


        String splitXsl = "split.xsl";
        String output = "output.xml";

        Options optionsBean = new Options();
        CmdLineParser parser = new CmdLineParser(optionsBean);

        try {
            parser.parseArgument(args);
        } catch (CmdLineException ex) {
            System.err.println(ex.getMessage());
            printUsage();
            parser.printUsage(System.err);
            return;
        }

        String userName = optionsBean.getUserName();
        String password = optionsBean.getPassword();
        List<String> arguments = optionsBean.getArguments();

        if (optionsBean.isVerbose()) {

            System.out.println("verbose activated");
            LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();

            try {
                JoranConfigurator configurator = new JoranConfigurator();
                configurator.setContext(lc);
                // the context was probably already configured by default configuration
                // rules
                lc.reset();
                InputStream verboseConfigFile = Thread.currentThread().getContextClassLoader().getResourceAsStream("logback-verbose.xml");
                configurator.doConfigure(verboseConfigFile);
            } catch (JoranException je) {
                je.printStackTrace();
            }
        }

        if (userName == null || password == null) {
            LOG.debug("user name and password should be provided.");
            System.err.println("user name and password should be provided. Example usage : -u op@cho.com -p Password  1234567 23433");
//            printUsage();
//            parser.printUsage(System.err);
            return;
        }

        if (arguments == null || arguments.isEmpty()) {
            LOG.debug("Either suppler reference or file name should be provided");
            System.err.println("Either suppler reference or file name should be provided. Example usage : -u op@cho.com -p Password test.xml or 1234567 23433");
//            printUsage();
//            parser.printUsage(System.err);
            return;
        }

        interceptor.setProperty("user", userName);
        passwordHolder.setPassword(password);
        passwordHolder.setUserName(userName);



        LOG.debug("Getting WS bean...");
        UploadService uploadService = (UploadService) ctx.getBean("peymentReceived");

        Client client = ClientProxy.getClient(uploadService);
        client.getInInterceptors().add(new LoggingInInterceptor());
        client.getOutInterceptors().add(new LoggingOutInterceptor());

        InputStream splitXslStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(splitXsl);

        File dir = new File(".");
        FilenameFilter filter = new MyFilter("splitInput-", "xml");
        File[] filenames = dir.listFiles(filter);
        for (int i = 0; i < filenames.length; i++) {
            filenames[i].delete();
        }

        for (String fileName : arguments) {

            if (fileName.substring(fileName.lastIndexOf(".") + 1).equalsIgnoreCase("xml")) {

                if (!(new File(fileName)).exists()) {
                    LOG.error("Input file '{}' does not exist.", fileName);
                    System.out.println("Input file " + fileName + " does not exist.");
//                    parser.printUsage(System.err);
                    return;
                } else {

                    try {
                        transform(fileName, splitXslStream, output);
                    } catch (TransformerConfigurationException ex) {
                        LOG.error("Error transforming XML: " + ex.getMessage());
                    } catch (TransformerException ex) {
                        LOG.error("Error transforming XML: " + ex.getMessage());
                    }
                    filenames = dir.listFiles(filter);

                    for (int i = 0; i < filenames.length; i++) {
                        String filename = null;

                        try {
                            filename = filenames[i].getCanonicalPath();
                        } catch (IOException ex) {
                            LOG.error("Error getting cho reference from claim file '{}': {}", filename, ex.getMessage());
                            continue;
                        }
                        LOG.info("getting cho reference from claim file: '" + filename + "'");


                        File tmpFile = filenames[i];

                        JAXBContext jaxbContext;
                        Chox chox = null;

                        if (tmpFile.exists()) {

                            LOG.trace("Upload file created: '" + tmpFile.getPath() + "'");
                            String xml2Upload = ReadTextFile.getContents(tmpFile);
                            LOG.trace("    Contents of file to be uploaded:\n    <<<<<<<<<<<<< start >>>>>>>>>>>>>\n"
                                    + xml2Upload + "    <<<<<<<<<<<<<  End  >>>>>>>>>>>>>");

                            try {

                                jaxbContext = JAXBContext.newInstance("com.idaschox.services.chox");
                                Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

                                JAXBElement<Chox> choxElement = (JAXBElement<Chox>) unmarshaller.unmarshal(new StreamSource(tmpFile), Chox.class);
                                chox = choxElement.getValue();
                                LOG.debug("Got Chox element: {}", chox);


                            } catch (JAXBException ex) {
                                LOG.error("Error load xml: '{}'", ex.getMessage());
                                System.exit(-1);
                            }

                        }

                        LOG.debug("Calling service...");
                        Result result = uploadService.paymentReceived(chox.getRental().getSupplierReference());

                        LOG.info("Result is: {} - '{}'", result.isStatus(), result.getErrorMessage());
                        System.out.println();
                        System.out.println("Result for supplier reference :" + chox.getRental().getSupplierReference());
                        System.out.println();
                        System.out.println("       Status : " + result.isStatus());
                        if (result.getErrorMessage() != null && !result.getErrorMessage().isEmpty()) {
                            System.out.println("Error Message : " + result.getErrorMessage());
                        }
                        System.out.println();

                    }
                }
            } else {
                String suppRef = fileName;
                LOG.info("Calling service for supplier references : {}", suppRef);
                Result result = uploadService.paymentReceived(suppRef.trim());
                LOG.info("Result is: {} - '{}'", result.isStatus(), result.getErrorMessage());
                System.out.println("\n");
                System.out.println("Result for supplier reference :" + suppRef);
                System.out.println("\n");
                System.out.println("       Status : " + result.isStatus());
                if (result.getErrorMessage() != null && !result.getErrorMessage().isEmpty()) {
                    System.out.println("Error Message : " + result.getErrorMessage());
                }
                System.out.println("\n");

            }

        }
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
