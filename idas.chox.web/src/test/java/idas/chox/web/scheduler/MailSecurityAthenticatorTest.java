package idas.chox.web.scheduler;

import static org.junit.Assert.assertEquals;
import idas.chox.core.model.EmailUpdateUser;
import idas.chox.core.services.EmailUpdateUserService;
import idas.chox.web.BaseWebTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


public class MailSecurityAthenticatorTest extends BaseWebTest {
    
	@Autowired
	private MailSecurityAthenticator mailSecurityAthenticator;
	
	@Autowired
	private MailUtil mailUtil;
        
    @Autowired
    private EmailUpdateUserService emailUpdateUserService;
        
	private Properties props;
	
	private static final String sender =  "patrik.bego@sherwoodcompliance.co.uk";
	
	@Before
    public void setProperties() throws Exception {
		//Resource resource = new ClassPathResource("/applicationTest.properties");
    	//props = PropertiesLoaderUtils.loadProperties(resource);
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
        // XXX this needs to be updated so that we will get the list of users from the database
        EmailUpdateUser emailUpdateUser = new EmailUpdateUser();
        emailUpdateUser.setEmail("patrik.bego@sherwoodcompliance.co.uk");
        List<EmailUpdateUser> emailUpdateUserList = new ArrayList<EmailUpdateUser>();
        emailUpdateUserList.add(emailUpdateUser);
        EmailUpdateUser schedulerPrivilegedUser = mailSecurityAthenticator.isPrivilegedSender(emailUpdateUserList, sender);
        if (schedulerPrivilegedUser != null)
            result = true;
        assertEquals(true, result);
    }

}
