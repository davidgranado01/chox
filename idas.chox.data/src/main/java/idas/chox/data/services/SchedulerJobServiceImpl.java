package idas.chox.data.services;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.model.SchedulerJob;
import idas.chox.core.services.SchedulerJobService;

public class SchedulerJobServiceImpl extends BaseDataService implements SchedulerJobService {

    private static final Logger LOG = LoggerFactory.getLogger(SchedulerJobServiceImpl.class);

    @Override
    public List<SchedulerJob> getSchedulerJobs(String jobName) {

        DetachedCriteria criteria = DetachedCriteria.forClass(SchedulerJob.class)
                .add(Restrictions.eq("active", true))
                .add(Restrictions.eq("jobName", jobName));
        return findByCriteria(criteria);
    }
}
