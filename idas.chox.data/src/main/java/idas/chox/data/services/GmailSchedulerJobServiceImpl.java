package idas.chox.data.services;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.model.GmailSchedulerJob;
import idas.chox.core.services.GmailSchedulerJobService;

public class GmailSchedulerJobServiceImpl extends BaseDataService implements GmailSchedulerJobService {

    private static final Logger LOG = LoggerFactory.getLogger(GmailSchedulerJobServiceImpl.class);

    @Override
    public GmailSchedulerJob getSchedulerJobs(String subject) {

        DetachedCriteria criteria = DetachedCriteria.forClass(GmailSchedulerJob.class)
                .add(Restrictions.eq("active", true))
                .add(Restrictions.ilike("emailSubject", "%" + subject.trim() + "%"));
        List<GmailSchedulerJob> results = findByCriteria(criteria);
        
        return results == null || results.size() == 0 ? null : results.get(0);
    }
}
