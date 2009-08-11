package jobscheduler.Jobs;

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

public class RunDashboardProcess implements Job{

    public void execute(JobExecutionContext context) throws JobExecutionException {
        
        JobDataMap dataMap = context.getJobDetail().getJobDataMap();
        Integer userId = dataMap.getInt("userId");
        PreparedStatement ps = null;
        
        DataService dataService = new DataService();
        
        try{
            
            Connection conn = dataService.getConnection();
            Statement s = conn.createStatement();
            ResultSet rs = s.executeQuery("select sqlrunstatusreport("+userId+")");
            conn.close();
            
        } catch (SQLException ex) {
            Logger.getLogger(RunDashboardProcess.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
