package idax.chox.web.scheduler;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class JobExecutor 
{
    public static void main( String[] args ) throws Exception
    {
    	new ClassPathXmlApplicationContext("applicationContext-Scheduler.xml");
    }
}