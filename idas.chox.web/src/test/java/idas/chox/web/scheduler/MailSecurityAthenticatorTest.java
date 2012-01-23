package idas.chox.web.scheduler;

import static org.junit.Assert.assertEquals;
import idas.chox.web.BaseWebTest;

import java.util.List;
import java.util.Properties;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.security.AuthenticationManager;
import org.springframework.security.context.SecurityContextHolder;


public class MailSecurityAthenticatorTest extends BaseWebTest{
    
	@Autowired
	private MailSecurityAthenticator mailSecurityAthenticator;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private MailUtil mailUtil;
	
	private Properties props;
	
	@Before
    public void setProperties() throws Exception {
		Resource resource = new ClassPathResource("/application.properties");
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
        System.out.println("isPrivilegedSender");
        List<String> listOfPrivilegedSenders = mailUtil.parseStringToList(props.getProperty("privilegedUsers"), ",");
        String sender = "patrik.bego@sherwoodcompliance.co.uk";
        boolean result = mailSecurityAthenticator.isPrivilegedSender(listOfPrivilegedSenders, sender);
        assertEquals(true, result);
    }

    /**
     * Test of authenticateSender method, of class MailSecurityAthenticator.
     */
    @Test
    public void testAuthenticateSender() {
        System.out.println("authenticateSender");
        String userName = props.getProperty("updateUserName");
        String password = props.getProperty("updatePassword");
       
        mailSecurityAthenticator.authenticateSender(userName, password);
        
        assertEquals(true, SecurityContextHolder.getContext().getAuthentication().isAuthenticated());
        //XXX this will be changed
        assertEquals( "ROLE_CHO", SecurityContextHolder.getContext().getAuthentication().getAuthorities()[1].toString());
    }

}
