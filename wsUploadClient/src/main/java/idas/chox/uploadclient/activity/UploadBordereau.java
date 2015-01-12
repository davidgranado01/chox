package idas.chox.uploadclient.activity;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.idaschox.services.chox.Chox;
import com.idaschox.services.chox.SubmissionResult;
import com.idaschox.services.chox.UploadService;
import idas.chox.uploadclient.MyErrorListener;
import idas.chox.uploadclient.MyFilter;
import idas.chox.uploadclient.utility.ReadTextFile;


/**
 * John
 *
 */
public class UploadBordereau {

    private static final Logger LOG = LoggerFactory.getLogger(UploadBordereau.class);

    public static void process(UploadService uploadService, String fileName) {

        String splitXsl = "split.xsl";
        String output = "output.xml";
        SubmissionResult result = null;

        InputStream splitXslStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(splitXsl);

        File dir = new File(".");
        FilenameFilter filter = new MyFilter("splitInput-", "xml");
        File[] filenames = dir.listFiles(filter);
        for (File filename : filenames) {
            filename.delete();
        }


        if (!(new File(fileName)).exists()) {
            LOG.error("Input file '{}' does not exist.", fileName);
            return;
        } else {

            LOG.info("Splitting input file '{}' into individual claims", fileName);
            try {
                transform(fileName, splitXslStream, output);
            } catch (TransformerConfigurationException ex) {
                LOG.error("Error transforming XML: " + ex.getMessage());
                return;
            } catch (TransformerException ex) {
                LOG.error("Error transforming XML: " + ex.getMessage());
                return;
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
                LOG.debug("Processing claim file: '{}'.", filename);


                File tmpFile = filenames[i];

                JAXBContext jaxbContext;
                Chox chox = null;

                if (tmpFile.exists()) {

                    LOG.debug("Upload file created: '{}'.", tmpFile.getPath());
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
                } finally {
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
                if (result.getBREMessages() != null) {
                    List<String> messages = result.getBREMessages().getMessages();
                    for (String m : messages) {
                        LOG.info("    BRE Message: {}", m);
                    }
                }
            }


        }

        File f = new File(output);
        if (f.exists()) {
            f.delete();
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
