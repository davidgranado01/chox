package idas.chox.web.scheduler;


import org.junit.After;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;

import idas.chox.core.services.SchedulerJobService;
import idas.chox.web.BaseWebTest;

public class MailSecurityAthenticatorTest extends BaseWebTest {

    @Autowired
    private MailSecurityAthenticator mailSecurityAthenticator;
    @Autowired
    private SchedulerJobService schedulerJobService;

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
//    @Test
//    public void testEcdUpdatePrivilegedSenders() {
//        boolean result = false;
//        String sender = "elliot.roberts@sherwoodcompliance.co.uk";
//        for (SchedulerJob schedulerJob : schedulerJobService.getSchedulerJobs(ECDUpdateSchedulerJob.JOB_NAME)) {
//            if (mailSecurityAthenticator.isPrivilegedSender(schedulerJob.getPrivilegedUsers(), sender)) {
//                result = true;
//            }
//        }
//        assertEquals(true, result);
//    }
//    
}
