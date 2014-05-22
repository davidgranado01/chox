package idas.chox.service.monitors;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Emmanuel
 */
public class ClaimViewState extends ViewState {

    private static final Logger LOG = LoggerFactory.getLogger(ClaimViewState.class);
    private ConcurrentHashMap<ActivityMonitorUserDetail, ViewState> users = new ConcurrentHashMap<ActivityMonitorUserDetail, ViewState>();

    public ClaimViewState(ActivityMonitorUserDetail activityMonitorUserDetail, long interval) {
        super(interval);
        users.put(activityMonitorUserDetail, new ViewState(interval));
        LOG.trace("Created for user: {}", activityMonitorUserDetail.getId());
    }

    public void ping(ActivityMonitorUserDetail activityMonitorUserDetail, long interval) {
        users.putIfAbsent(activityMonitorUserDetail, new ViewState(interval));
        users.get(activityMonitorUserDetail).refresh();
        this.refresh();
        LOG.trace("User {} refreshed.", activityMonitorUserDetail.getId());
    }

    public List<ActivityMonitorUserDetail> getClaimViewingUsers() {
        List<ActivityMonitorUserDetail> result = null;
        for (ActivityMonitorUserDetail activityMonitorUserDetail : users.keySet()) {
            if (result == null) {
                result = new ArrayList<ActivityMonitorUserDetail>();
            }
            ViewState v = users.get(activityMonitorUserDetail);
            if (!v.isExpired()) {
                LOG.trace("adding user to result: {}", activityMonitorUserDetail.getId());
                result.add(activityMonitorUserDetail);
            } else {
                LOG.trace("removing user from result: {} (should probably be from users)", activityMonitorUserDetail.getId());
                result.remove(activityMonitorUserDetail);
            }
        }
        LOG.trace("Returning {} user ids (viewing claim)", result != null ? result.size() : 0);
        return result;
    }
}
