package idas.chox.web;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { 
	    "/applicationContext-Notification.xml", 
	    "/applicationContext-IntelligentNote.xml",
	    "/applicationContext-Filters.xml",
	    "/applicationContext-workflow.xml",
	    "/applicationContext.xml",
	    "/applicationContext-services.xml",
	    "/applicationContext-security.xml",
	    "/applicationContext-BRE.xml", 
	    "/applicationContext-Scheduler.xml", 
	    "/applicationContext-XMLReader.xml"})
public abstract class BaseWebTest {

}
