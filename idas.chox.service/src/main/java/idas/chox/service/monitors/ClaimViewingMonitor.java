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

    public List<Integer> ping(Integer claimId, String CompanyType, Integer orgId, Integer userId, long interval) {
        String key = forStateKey(claimId);

        if (claims.containsKey(key)) {
            ClaimViewState vs = claims.get(key);
            vs.ping(userId, interval);
        } else {
            ClaimViewState vs = new ClaimViewState(userId, interval);
            claims.putIfAbsent(key, vs);
        }        
        return getWhoIsViewing(key);        
    }

    private String forStateKey(Integer claimId) {
//        previously we used org and org type "%d_%s_%d", claimId, CompanyType, orgId
        return String.format("%d", claimId);
    }

    public List<Integer> getWhoIsViewing(Integer claimId, String CompanyType, Integer orgId) {
//        previously we used org and org type "%d_%s_%d", claimId, CompanyType, orgId
        String stateKey = String.format("%d", claimId);
        return getWhoIsViewing(stateKey);
    }

    private List<Integer> getWhoIsViewing(String key) {
        ClaimViewState cvs = claims.get(key);
        if(cvs == null)
        {
            return null;
        }
        if (!cvs.isExpired()) {
            return cvs.getUserIds();
        } else {
            claims.remove(key);
            return null;
        }

    }
    
    public boolean isClaimViewingBySomeBody(Integer claimId, String CompanyType, Integer orgId)
    {
//        previously we used org and org type "%d_%s_%d", claimId, CompanyType, orgId
        String key = String.format("%d", claimId);
        
        ClaimViewState cvs = claims.get(key);
        if(cvs == null)
        {
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
