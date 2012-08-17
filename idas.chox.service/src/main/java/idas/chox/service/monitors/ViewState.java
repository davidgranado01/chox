package idas.chox.service.monitors;

import idas.chox.core.services.ClaimService;


/**
 *
 * @author Emmanuel
 */
public class ViewState {
    private Long birth = System.currentTimeMillis();
    private final Object lock = new Object();
    private ClaimService claimService;
    
    public ViewState() {
    }
    

    public boolean isExpired() {
        boolean hasExpired;
        long maxAge = claimService.getActivityMonitorRequestInterval()+1000;
        synchronized (lock) {            
            hasExpired = System.currentTimeMillis() - birth > maxAge;
        }
        return hasExpired;
    }

    public void refresh() {
       synchronized (lock) {
            birth = System.currentTimeMillis();
            //System.out.println("Refreshed to " + birth.toString());
       }
    }

    public ClaimService getClaimService() {
        return claimService;
    }

    public void setClaimService(ClaimService claimService) {
        this.claimService = claimService;
    }
}
