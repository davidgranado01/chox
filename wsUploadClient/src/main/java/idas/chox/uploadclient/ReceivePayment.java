package idas.chox.uploadclient;

import com.idaschox.services.chox.Chox;
import com.idaschox.services.chox.Result;
import com.idaschox.services.chox.UploadService;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
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
        System.err.println("Usage: [-u] UserName [-p] Password [-f] bordereau-XML-file_Location [-ref] supplier reference number");
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
        String wsdlLocation = optionsBean.getWsdlLocation();
        String fileName = optionsBean.getFilename();
        String suppReferences = optionsBean.getSuppRef();

        if (userName == null || password == null) {
            LOG.debug("user name and password should be provided.");
            System.err.println("user name and password should be provided. Example -u \"op@cho.com\" -p \"Password\" -ref \"1234567,23433\"");
            printUsage();
            parser.printUsage(System.err);
            return;
        }

        if (fileName == null && suppReferences == null) {
            LOG.debug("Either suppler reference or file name should be provided");
            System.err.println("Either suppler reference or file name should be provided. Example -u \"op@cho.com\" -p \"Password\" -f \"test.xml\" or -ref \"1234567,23433\"");
            printUsage();
            parser.printUsage(System.err);
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

        if (suppReferences == null && !fileName.isEmpty()) {

            InputStream splitXslStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(splitXsl);

            File dir = new File(".");
            FilenameFilter filter = new MyFilter("splitInput-", "xml");
            File[] filenames = dir.listFiles(filter);
            for (int i = 0; i < filenames.length; i++) {
                filenames[i].delete();
            }

            if (!(new File(fileName)).exists()) {
                LOG.error("Input file '{}' does not exist.", fileName);
                parser.printUsage(System.err);
                return;
            }

            File temp1 = null;

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

            }
        } else if (!optionsBean.getSuppRef().isEmpty()) {

            String[] temp;
            String delimiter = ",";
            temp = optionsBean.getSuppRef().split(delimiter);

            for (String suppRef : temp) {

                LOG.info("Calling service for supplier references : {}", suppRef);
                Result result = uploadService.paymentReceived(suppRef.trim());
                LOG.info("Result is: {} - '{}'", result.isStatus(), result.getErrorMessage());

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
