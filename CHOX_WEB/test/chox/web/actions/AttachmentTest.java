package chox.web.actions;


import chox.services.AttachmentService;
import chox.services.ClaimService;
import junit.framework.TestCase;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class AttachmentTest extends TestCase {

    private ApplicationContext ctx;
    
    public AttachmentTest() {
        String[] paths = {"applicationContext.xml","applicationContext-services.xml"};
        ctx = new ClassPathXmlApplicationContext(paths);
    }

    
    @Override
    public void tearDown() {
    }
    
}

