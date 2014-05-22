package idas.chox.service.monitors;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author Emmanuel
 */
public final class ClaimViewingMonitor {

    private static ClaimViewingMonitor instance = new ClaimViewingMonitor();
    private ConcurrentHashMap<String, ClaimViewState> claims = new ConcurrentHashMap<String, ClaimViewState>();

    private ClaimViewingMonitor() {
    }

    public static ClaimViewingMonitor getInstance() {
        return instance;
    }

    public List<ActivityMonitorUserDetail> ping(Integer claimId, ActivityMonitorUserDetail activityMonitorUserDetail, long interval) {
        String key = forStateKey(claimId);

        if (claims.containsKey(key)) {
            ClaimViewState vs = claims.get(key);
            vs.ping(activityMonitorUserDetail, interval);
        } else {
            ClaimViewState vs = new ClaimViewState(activityMonitorUserDetail, interval);
            claims.putIfAbsent(key, vs);
        }
        return getWhoIsViewing(key);
    }

    private String forStateKey(Integer claimId) {
//        previously we used org and org type "%d_%s_%d", claimId, CompanyType, orgId
        return String.format("%d", claimId);
    }

    private List<ActivityMonitorUserDetail> getWhoIsViewing(String key) {
        ClaimViewState cvs = claims.get(key);
        if (cvs == null) {
            return null;
        }
        if (!cvs.isExpired()) {
            return cvs.getClaimViewingUsers();
        } else {
            claims.remove(key);
            return null;
        }

    }

    public boolean isClaimViewingBySomeBody(Integer claimId) {
//        previously we used org and org type "%d_%s_%d", claimId, CompanyType, orgId
        String key = String.format("%d", claimId);

        ClaimViewState cvs = claims.get(key);
        if (cvs == null) {
            return false;
        }
        if (!cvs.isExpired()) {
            return true;
        } else {
            claims.remove(key);
            return false;
        }
    }
}
