package jobscheduler;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import jobscheduler.services.DataService;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;


/**
 *
 * @author Carlson
 */
public class Main {

    
    public static void main(String[] args) throws SchedulerException {
        
        /*
        SchedulerFactory schedFact = new org.quartz.impl.StdSchedulerFactory();
        Scheduler sched = schedFact.getScheduler();
        sched.start();

        JobDetail jobDetail = new JobDetail("Test Job", null, RunDashboardProcess.class);
        jobDetail.getJobDataMap().put("userId", 999);
        
        Trigger trigger = TriggerUtils.makeHourlyTrigger();
        trigger.setName("myTrigger");
        sched.scheduleJob(jobDetail, trigger);
        */
        
        PreparedStatement ps = null;
        
        DataService dataService = new DataService();
        
        try{
            
            Connection conn = dataService.getConnection();
            Statement s = conn.createStatement();
            ResultSet rs = s.executeQuery("select sqlrunstatusreport("+999+")");
            conn.close();
            
        } catch (SQLException ex) {
            
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            
        }        
    }

}
