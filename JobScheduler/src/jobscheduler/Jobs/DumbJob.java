/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jobscheduler.Jobs;

import java.util.ArrayList;
import java.util.Date;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class DumbJob implements Job{

    public DumbJob() {}
    
    public void execute(JobExecutionContext context) throws JobExecutionException {
        
        System.out.println(">>>>>>>>>>>>>..07");
        
        String instName = context.getJobDetail().getName();
        String instGroup = context.getJobDetail().getGroup();

        System.out.println(">>>>>>>>>>>>>..08");
        
        JobDataMap dataMap = context.getJobDetail().getJobDataMap();

        System.out.println(">>>>>>>>>>>>>..09");
        
        String jobSays = dataMap.getString("jobSays");
        float myFloatValue = dataMap.getFloat("myFloatValue");
        ArrayList state = (ArrayList)dataMap.get("myStateData");
        state.add(new Date());
        
        System.out.println(">>>>>>>>>>>>>..10");
        
        System.err.println("Instance " + instName + " of DumbJob says: " + jobSays);
        
    }

}
