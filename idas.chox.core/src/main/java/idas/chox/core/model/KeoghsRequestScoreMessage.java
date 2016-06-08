package idas.chox.core.model;

import java.io.Serializable;

/**
 *
 * @author John
 */
public class KeoghsRequestScoreMessage  extends Entity implements Serializable {
    private KeoghsRequest keoghsRequest;
    private String scoreMessageHeading;
    private String scoreMessageDetail;

    public KeoghsRequest getKeoghsRequest() {
        return keoghsRequest;
    }

    public void setKeoghsRequest(KeoghsRequest keoghsRequest) {
        this.keoghsRequest = keoghsRequest;
    }

    public String getScoreMessageHeading() {
        return scoreMessageHeading;
    }

    public void setScoreMessageHeading(String scoreMessageHeading) {
        this.scoreMessageHeading = scoreMessageHeading;
    }

    public String getScoreMessageDetail() {
        return scoreMessageDetail;
    }

    public void setScoreMessageDetail(String scoreMessageDetail) {
        this.scoreMessageDetail = scoreMessageDetail;
    }

}
