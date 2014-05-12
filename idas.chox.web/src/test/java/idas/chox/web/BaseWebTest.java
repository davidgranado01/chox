package idas.chox.web;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { 
	    "classpath:applicationContext-IntelligentNote.xml",
	    "classpath:applicationContext-Filters.xml",
	    "classpath:applicationContext-Workflow.xml",
	    "classpath:applicationContext.xml",
	    "classpath:applicationContext-services.xml",
	    "classpath:applicationContext-security.xml",
	    "classpath:applicationContext-BRE.xml", 
	    "classpath:applicationContext-Scheduler.xml", 
            "classpath:applicationContext-activemq.xml", 
	    "classpath:applicationContext-XMLReader.xml"})
public abstract class BaseWebTest {

}
