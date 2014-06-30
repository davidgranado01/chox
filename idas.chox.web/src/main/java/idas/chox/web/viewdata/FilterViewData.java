package idas.chox.web.viewdata;

import idas.chox.core.search.ClaimSearchCriteria;

/**
 *
 * @author seeni
 */
public class FilterViewData {

    private String key;
    private String queueNameWithCount;
    private String queueName;
    private int queueCount;
    private String queueDescription;
    private ClaimSearchCriteria claimSearchCriteria; 

    public ClaimSearchCriteria getClaimSearchCriteria() {
        return claimSearchCriteria;
    }

    public void setClaimSearchCriteria(ClaimSearchCriteria claimSearchCriteria) {
        this.claimSearchCriteria = claimSearchCriteria;
    }

    public String getQueueDescription() {
        return queueDescription;
    }

    public void setQueueDescription(String queueDescription) {
        this.queueDescription = queueDescription;
    }

    public int getQueueCount() {
        return queueCount;
    }

    public void setQueueCount(int queueCount) {
        this.queueCount = queueCount;
    }

    public String getQueueName() {
        return queueName;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    public String getQueueNameWithCount() {
        return queueNameWithCount;
    }

    public void setQueueNameWithCount(String queueNameWithCount) {
        this.queueNameWithCount = queueNameWithCount;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
