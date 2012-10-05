package idas.chox.web.scheduler;

import org.quartz.JobExecutionException;

public interface Scheduler {

    public void execute() throws JobExecutionException;
    
}
