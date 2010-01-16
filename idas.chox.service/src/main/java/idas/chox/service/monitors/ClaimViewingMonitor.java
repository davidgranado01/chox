/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.service.monitors;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author Emmanuel
 */
public class ClaimViewingMonitor {

    private static ClaimViewingMonitor instance = new ClaimViewingMonitor();
    private ConcurrentHashMap<String, ClaimViewState> claims = new ConcurrentHashMap<String, ClaimViewState>();

    private ClaimViewingMonitor() {
    }

    public static ClaimViewingMonitor getInstance() {
        return instance;
    }

    public List<Integer> ping(Integer claimId, String CompanyType, Integer orgId, Integer userId) {
        String key = forStateKey(claimId, CompanyType, orgId);

        if (claims.containsKey(key)) {
            ClaimViewState vs = (ClaimViewState) claims.get(key);
            vs.ping(userId);
        } else {
            ClaimViewState vs = new ClaimViewState(userId);
            claims.putIfAbsent(key, vs);
        }        
        return getWhoIsViewing(key);        
    }

    private String forStateKey(Integer claimId, String CompanyType, Integer orgId) {
        return String.format("%d_%s_%d", claimId, CompanyType, orgId);
    }

    public List<Integer> getWhoIsViewing(Integer claimId, String CompanyType, Integer orgId) {
        String stateKey = String.format("%d_%s_%d", claimId, CompanyType, orgId);
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
        String key = String.format("%d_%s_%d", claimId, CompanyType, orgId);
        
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
