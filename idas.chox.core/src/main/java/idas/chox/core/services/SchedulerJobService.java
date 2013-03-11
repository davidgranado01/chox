package idas.chox.core.services;

import java.util.List;

import idas.chox.core.model.SchedulerJob;

public interface SchedulerJobService {

    List<SchedulerJob> getSchedulerJobs(String jobName);
}
