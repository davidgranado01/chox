package idas.chox.web.scheduler;

import org.quartz.JobExecutionException;

public interface SchedulerJob {

    public void execute() throws JobExecutionException;
    
}
