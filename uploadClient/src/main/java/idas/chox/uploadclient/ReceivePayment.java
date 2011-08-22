package idas.chox.uploadclient;

import com.idaschox.services.chox.Result;
import com.idaschox.services.chox.UploadService;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author John
 */
public class ReceivePayment {
    private static final String[] LOCATIONS = {"META-INF/client.xml"};
    static final Logger LOG = LoggerFactory.getLogger(ReceivePayment.class);

    public static void main(String[] args) throws InterruptedException {
        ApplicationContext ctx = new ClassPathXmlApplicationContext(LOCATIONS);

        LOG.debug("Getting WS bean...");
        UploadService uploadService = (UploadService) ctx.getBean("paymentReceived");

        Client client = ClientProxy.getClient(uploadService);
        client.getInInterceptors().add(new LoggingInInterceptor());
        client.getOutInterceptors().add(new LoggingOutInterceptor());

        Result result;

        try {
            LOG.info("Calling service for non-existant claim...");
            result = uploadService.paymentReceived("xxxxxxx");
            LOG.info("Result is: {} - '{}'", result.isStatus(), result.getErrorMessage());
        } catch (Exception ex) {
            LOG.error("Exception thrown calling web service: '{}'", ex.getMessage());
            if (ex.getCause() != null) {
                LOG.error("....Caused By: '{}'", ex.getCause().getMessage());
            }
        }
        
        Thread.sleep(5000);
        
        try {
            LOG.info("Calling service for claim in wrong state...");
            result = uploadService.paymentReceived("2760568");
            LOG.info("Result is: {} - '{}'", result.isStatus(), result.getErrorMessage());
        } catch (Exception ex) {
            LOG.error("Exception thrown calling web service: '{}'", ex.getMessage());
            if (ex.getCause() != null) {
                LOG.error("....Caused By: '{}'", ex.getCause().getMessage());
            }
        }

        Thread.sleep(5000);

        try {
            LOG.info("Calling service for claim owned by us and in correct state (should work!)...");
            result = uploadService.paymentReceived("2735839");
            LOG.info("Result is: {} - '{}'", result.isStatus(), result.getErrorMessage());
        } catch (Exception ex) {
            LOG.error("Exception thrown calling web service: '{}'", ex.getMessage());
            if (ex.getCause() != null) {
                LOG.error("....Caused By: '{}'", ex.getCause().getMessage());
            }
        }

        Thread.sleep(5000);


        try {
            LOG.info("Calling service on claim not owned by us but in correct state...");
            result = uploadService.paymentReceived("UNU7D115862");
            LOG.info("Result is: {} - '{}'", result.isStatus(), result.getErrorMessage());
        } catch (Exception ex) {
            LOG.error("Exception thrown calling web service: '{}'", ex.getMessage());
            if (ex.getCause() != null) {
                LOG.error("....Caused By: '{}'", ex.getCause().getMessage());
            }
        }

        Thread.sleep(5000);

        try {
            LOG.info("Calling service on claim not owned by us and in incorrect state...");
            result = uploadService.paymentReceived("UNU3D528552");
            LOG.info("Result is: {} - '{}'", result.isStatus(), result.getErrorMessage());
        } catch (Exception ex) {
            LOG.error("Exception thrown calling web service: '{}'", ex.getMessage());
            if (ex.getCause() != null) {
                LOG.error("....Caused By: '{}'", ex.getCause().getMessage());
            }
        }

    }
  
}
