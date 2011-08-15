package idas.chox.uploadclient;

import com.idaschox.services.chox.Chox;
import com.idaschox.services.chox.SubmissionResult;
import com.idaschox.services.chox.UploadService;
import java.io.File;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * John
 *
 */
public class App {

    private static final String[] LOCATIONS = {"META-INF/client.xml"};
    static final Logger LOG = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        ApplicationContext ctx = new ClassPathXmlApplicationContext(LOCATIONS);

        LOG.debug("Getting WS bean...");
        UploadService uploadService = (UploadService) ctx.getBean("uploadBordereau");

        Client client = ClientProxy.getClient(uploadService);
        client.getInInterceptors().add(new LoggingInInterceptor());
        client.getOutInterceptors().add(new LoggingOutInterceptor());

        // load the Chox object from the test file: testBordereau.xml
        JAXBContext jaxbContext;
        Chox chox = null;
        try {
            jaxbContext = JAXBContext.newInstance("com.idaschox.services.chox");
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
//            File input = new File("src/main/resources/testBordereau.xml");
            File input = new File("src/main/resources/testClaim.xml");
            if (!input.exists()) {
                LOG.error("File not found: {}", input.getAbsolutePath());
                System.exit(-1);
            }
            LOG.info("Loading xml from file '{}'", input.getAbsolutePath());
            JAXBElement<Chox> choxElement = (JAXBElement<Chox>) unmarshaller.unmarshal(new StreamSource(input), Chox.class);
            chox = choxElement.getValue();
            LOG.debug("Got Chox element: {}", chox);
        }
        catch (JAXBException ex) {
            LOG.error("Error load xml: '{}'", ex.getMessage());
            System.exit(-1);
        }



        LOG.debug("Calling service...");
        SubmissionResult result = uploadService.uploadBordereau(chox);

        LOG.info("Result is: '{}' - '{}'", result.getProcessStatus(), result.getUploadStatus());
    }
}
