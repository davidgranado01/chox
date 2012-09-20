package idas.chox.web.scheduler;

import java.util.Properties;

import org.junit.After;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import idas.chox.core.model.EmailUpdateUser;
import idas.chox.core.services.SchedulerPrivilegedUserService;
import idas.chox.web.BaseWebTest;


public class MailSecurityAthenticatorTest extends BaseWebTest {
    
	@Autowired
	private MailSecurityAthenticator mailSecurityAthenticator;
	
	@Autowired
	private MailUtil mailUtil;
        
        @Autowired
        private SchedulerPrivilegedUserService schedulerPrivilegedUserService;
        
	private Properties props;
	
	private static final String sender =  "patrik.bego@sherwoodcompliance.co.uk";
	
	@Before
    public void setProperties() throws Exception {
		Resource resource = new ClassPathResource("/applicationTest.properties");
    	props = PropertiesLoaderUtils.loadProperties(resource);
    }
	
	@After
    public void clean() {
    }
	
    /**
     * Test of isPrivilegedSender method, of class MailSecurityAthenticator.
     */
    @Test
    public void testIsPrivilegedSender()  {
        boolean result = false;
        EmailUpdateUser schedulerPrivilegedUser = mailSecurityAthenticator.isPrivilegedSender(schedulerPrivilegedUserService.getPenaltyChargeUpdatePrivilegedUsers(), sender);
        if (schedulerPrivilegedUser != null)
            result = true;
        assertEquals(true, result);
    }

}
