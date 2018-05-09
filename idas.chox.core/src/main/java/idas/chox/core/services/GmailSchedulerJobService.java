package idas.chox.core.services;

import idas.chox.core.model.GmailSchedulerJob;

public interface GmailSchedulerJobService {

    GmailSchedulerJob getSchedulerJobs(String subject);
}
