package idas.chox.core.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author John
 */
public class KeoghsRequest extends Entity implements Serializable {
    private Claim claim;
    private String clientBatchReference;
    private int batchStatus;
    private int claimStatus;
    private String checkType;
    private String resultStatus;
    private String ragResult;
    private int totalScore;
    private String responseMessageDebug;
    private List<KeoghsRequestScoreMessage> keoghsRequestScoreMessages;

    public Claim getClaim() {
        return claim;
    }

    public void setClaim(Claim claim) {
        this.claim = claim;
    }

    public String getClientBatchReference() {
        return clientBatchReference;
    }

    public void setClientBatchReference(String clientBatchReference) {
        this.clientBatchReference = clientBatchReference;
    }

    public int getBatchStatus() {
        return batchStatus;
    }

    public void setBatchStatus(int batchStatus) {
        this.batchStatus = batchStatus;
    }

    public int getClaimStatus() {
        return claimStatus;
    }

    public void setClaimStatus(int claimStatus) {
        this.claimStatus = claimStatus;
    }

    public String getCheckType() {
        return checkType;
    }

    public void setCheckType(String checkType) {
        this.checkType = checkType;
    }

    public String getResultStatus() {
        return resultStatus;
    }

    public void setResultStatus(String resultStatus) {
        this.resultStatus = resultStatus;
    }

    public String getRagResult() {
        return ragResult;
    }

    public void setRagResult(String ragResult) {
        this.ragResult = ragResult;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    public String getResponseMessageDebug() {
        return responseMessageDebug;
    }

    public void setResponseMessageDebug(String responseMessageDebug) {
        this.responseMessageDebug = responseMessageDebug;
    }

    public List<KeoghsRequestScoreMessage> getKeoghsRequestScoreMessages() {
        return keoghsRequestScoreMessages;
    }

    public void setKeoghsRequestScoreMessages(List<KeoghsRequestScoreMessage> keoghsRequestScoreMessages) {
        this.keoghsRequestScoreMessages = keoghsRequestScoreMessages;
    }
    
    public void addKeoghsRequestScoreMessage(KeoghsRequestScoreMessage keoghsRequestScoreMessage) {
        if (keoghsRequestScoreMessages == null) {
            keoghsRequestScoreMessages = new ArrayList<>();
        }

        keoghsRequestScoreMessage.setKeoghsRequest(this);
        keoghsRequestScoreMessages.add(keoghsRequestScoreMessage);
    }

    public void deleteKeoghsRequestScoreMessage(KeoghsRequestScoreMessage keoghsRequestScoreMessage) {
        keoghsRequestScoreMessages.remove(keoghsRequestScoreMessage);
    }

}
