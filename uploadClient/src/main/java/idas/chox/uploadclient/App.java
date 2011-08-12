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
import org.kohsuke.args4j.CmdLineParser;

/**
 * John
 *
 */
public class App {

    private static final String[] LOCATIONS = {"META-INF/client.xml"};
    static final Logger LOG = LoggerFactory.getLogger(App.class);

    private static void printUsage() {
        System.err.println("Usage: [-u] UserName [-p] Password [-w] WSDL_Location [-f] bordereau-XML-file_Location");
    }

    public static void main(String[] args) {
        ApplicationContext ctx = new ClassPathXmlApplicationContext(LOCATIONS);

        String splitXsl = "split.xsl";
        String output = "output.xml";
        String convertXsl = null;

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


            File tmpFile = null;
            try {
                tmpFile = File.createTempFile("claim2Upload-", ".xml");
            } catch (IOException ex) {
                LOG.error("Error creating temp file: {}", ex.getMessage());
            }

            InputStream convertXslStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(convertXsl);


            try {
//                transform(filename, convertXslFile, tmpFile.getCanonicalPath());
                transform(filename, convertXslStream, tmpFile.getCanonicalPath());
            } catch (TransformerConfigurationException ex) {
                LOG.error("Error transforming XML: " + ex.getMessage());
            } catch (TransformerException ex) {
                LOG.error("Error transforming XML: " + ex.getMessage());
            } catch (IOException ex) {
                LOG.error("Error transforming XML: " + ex.getMessage());
            }

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

            LOG.info("Result is: '{}' - '{}'", result.getProcessStatus(), result.getUploadStatus());

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
