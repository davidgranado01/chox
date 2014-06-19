package idas.chox.web.viewdata;

import idas.chox.core.search.ClaimSearchCriteria;

/**
 *
 * @author seeni
 */
public class FilterViewData {

    private String key;
    private String description;
    private String queueName;
    private int queueClaimsCount;
    private ClaimSearchCriteria claimSearchCriteria; 

    public ClaimSearchCriteria getClaimSearchCriteria() {
        return claimSearchCriteria;
    }

    public void setClaimSearchCriteria(ClaimSearchCriteria claimSearchCriteria) {
        this.claimSearchCriteria = claimSearchCriteria;
    }

    public int getQueueClaimsCount() {
        return queueClaimsCount;
    }

    public void setQueueClaimsCount(int queueClaimsCount) {
        this.queueClaimsCount = queueClaimsCount;
    }

    public String getQueueName() {
        return queueName;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
