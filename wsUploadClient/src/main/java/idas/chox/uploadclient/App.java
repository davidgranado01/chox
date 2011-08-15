package idas.chox.uploadclient;

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
import org.apache.cxf.ws.security.wss4j.WSS4JOutInterceptor;
import org.kohsuke.args4j.CmdLineParser;

/**
 * John
 *
 */
public class App {

    private static final String[] LOCATIONS = {"client.xml"};
    static final Logger LOG = LoggerFactory.getLogger(App.class);

    private static void printUsage() {
        System.err.println("Usage: [-u] UserName [-p] Password [-w] WSDL_Location [-f] bordereau-XML-file_Location");
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
            System.err.println("Usage: java -jar uploadClient.jar [-u] [-p] [-w] [-f]");
            parser.printUsage(System.err);
            return;
        }


        String userName = optionsBean.getUserName();
        String password = optionsBean.getPassword();
        String wsdlLocation = optionsBean.getWsdlLocation();
        String fileName = optionsBean.getFilename();
        
        if (userName == null || password == null) {
            
            userName = "op@cho.com";
            password = "C0mpliance";
            
            LOG.debug("user name and password is not provided, Using the default userName = \"op@cho.com\", password = \"C0mpliance\".");
            System.err.println("user name and password is not provided, Using the default userName = \"op@cho.com\", password = \"C0mpliance\". It can be given, for Example -u \"op@cho.com\" -p \"Password\" -f \"test.xml\".");
            printUsage();
        }
        
        if (fileName == null) {
            LOG.debug("file name is not provided, Please provide file name using , Example -f \"test.xml\".");
            System.err.println("file name is not provided, Please provide file name using, for Example -u \"op@cho.com\" -p \"Password\" -f \"test.xml\".");
            printUsage();
            return;
        }

        interceptor.setProperty("user", userName);
        passwordHolder.setPassword(password);
        passwordHolder.setUserName(userName);
        
        LOG.debug("setting username :{}",userName);
        LOG.debug("setting password :{}",password);

        LOG.debug("Getting WS bean...");
        UploadService uploadService = (UploadService) ctx.getBean("uploadBordereau");

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

        if (!(new File(fileName)).exists()) {
            LOG.error("Input file '{}' does not exist.", fileName);
            System.err.println("Usage: java -jar uploadClient.jar [-u] [-p] [-w] [-f]");
            parser.printUsage(System.err);
            return;
        }

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
                LOG.error("Error processing claim file '{}': {}", filename, ex.getMessage());
                continue;
            }
            LOG.info("Processing claim file: '" + filename + "'");


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
            SubmissionResult result = uploadService.uploadBordereau(chox);

            LOG.info("process status: {}",result.getProcessStatus());
            LOG.info("claim status: {}",result.getClaimStatus());
            LOG.info("upload status: {}",result.getUploadStatus());
            LOG.info("Error Message: {}",result.getMessages().getMessages().get(0));

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
