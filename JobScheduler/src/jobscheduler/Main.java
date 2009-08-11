/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jobscheduler;

import jobscheduler.Jobs.DumbJob;
import java.util.ArrayList;
import java.util.Date;
import jobscheduler.Jobs.RunDashboardProcess;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerUtils;



/**
 *
 * @author Carlson
 */
public class Main {

    
    public static void main(String[] args) throws SchedulerException {
        
        SchedulerFactory schedFact = new org.quartz.impl.StdSchedulerFactory();
        Scheduler sched = schedFact.getScheduler();
        sched.start();

        JobDetail jobDetail = new JobDetail("Test Job", null, RunDashboardProcess.class);
        jobDetail.getJobDataMap().put("userId", 999);
        
        Trigger trigger = TriggerUtils.makeHourlyTrigger();
        trigger.setName("myTrigger");
        sched.scheduleJob(jobDetail, trigger);
        
    }

}
