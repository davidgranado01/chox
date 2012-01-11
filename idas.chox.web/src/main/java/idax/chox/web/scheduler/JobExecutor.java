package idax.chox.web.scheduler;

import org.springframework.context.support.ClassPathXmlApplicationContext;
//XXX this class can be deleted - just for development and testing purposes
public class JobExecutor 
{
    public static void main( String[] args ) throws Exception
    {
    	new ClassPathXmlApplicationContext("applicationContext-Scheduler.xml");
    }
}