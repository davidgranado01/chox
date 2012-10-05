package idas.chox.web.scheduler;


import org.junit.After;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import idas.chox.core.model.SchedulerJob;
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
    @Test
    public void testEcdUpdatePrivilegedSenders() {
        boolean result = false;
        String sender = "elliot.roberts@sherwoodcompliance.co.uk";
        for (SchedulerJob schedulerJob : schedulerJobService.getSchedulerJobs(ECDUpdateSchedulerJob.JOB_NAME)) {
            if (mailSecurityAthenticator.isPrivilegedSender(schedulerJob.getPrivilegedUsers(), sender)) {
                result = true;
            }
        }
        assertEquals(true, result);
    }
    
    @Test
    public void testReferenceUpdatePrivilegedSenders() {
        boolean result = false;
        String sender = "elliot.roberts@sherwoodcompliance.co.uk";
        for (SchedulerJob schedulerJob : schedulerJobService.getSchedulerJobs(ReferenceUpdateEmailSchedulerJob.JOB_NAME)) {
            if (mailSecurityAthenticator.isPrivilegedSender(schedulerJob.getPrivilegedUsers(), sender)) {
                result = true;
            }
        }
        assertEquals(true, result);
    }

    @Test
    public void testPenaltyUpdatePrivilegedSenders() {
        boolean result = false;
        String sender = "elliot.roberts@sherwoodcompliance.co.uk";
        for (SchedulerJob schedulerJob : schedulerJobService.getSchedulerJobs(PenaltyChargeUpdateEmailSchedulerJob.JOB_NAME)) {
            if (mailSecurityAthenticator.isPrivilegedSender(schedulerJob.getPrivilegedUsers(), sender)) {
                result = true;
            }
        }
        assertEquals(true, result);
    }

    @Test
    public void testDbReferenceUpdatePrivilegedSenders() {
        boolean result = false;
        String sender = "elliot.roberts@sherwoodcompliance.co.uk";
        for (SchedulerJob schedulerJob : schedulerJobService.getSchedulerJobs(ReferenceUpdateDbSchedulerJob.JOB_NAME)) {
            if (mailSecurityAthenticator.isPrivilegedSender(schedulerJob.getPrivilegedUsers(), sender)) {
                result = true;
            }
        }
        assertEquals(true, result);
    }
}
