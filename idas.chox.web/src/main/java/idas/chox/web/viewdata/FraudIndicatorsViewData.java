package idas.chox.web.viewdata;

import idas.chox.core.model.KeoghsRequestScoreMessage;

/**
 *
 * @author john
 */
public class FraudIndicatorsViewData {
    private final int id;
    private final String scoreMessageHeading;
    private final String scoreMessageDetail;

    public int getId() {
        return id;
    }

    public String getScoreMessageHeading() {
        return scoreMessageHeading;
    }


    public String getScoreMessageDetail() {
        return scoreMessageDetail;
    }

    public FraudIndicatorsViewData(KeoghsRequestScoreMessage keoghsRequestScoreMessage) {
        this.id = keoghsRequestScoreMessage.getId();
        this.scoreMessageHeading = keoghsRequestScoreMessage.getScoreMessageHeading();
        this.scoreMessageDetail = keoghsRequestScoreMessage.getScoreMessageDetail();
    }
}
