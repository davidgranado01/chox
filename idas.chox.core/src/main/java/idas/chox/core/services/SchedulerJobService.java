package idas.chox.core.services;

import java.util.List;

import idas.chox.core.model.SchedulerJob;

public interface SchedulerJobService {

    public List<SchedulerJob> getSchedulerJobs(String jobName);
}
