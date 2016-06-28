package idas.chox.web.actions;

import idas.chox.core.model.Claim;
import java.util.ArrayList;
import java.util.List;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

import net.sf.json.JSONArray;

import idas.chox.core.model.KeoghsRequestScoreMessage;
import idas.chox.core.services.ClaimService;
import idas.chox.web.viewdata.FraudIndicatorsViewData;

public class FraudIndicatorsAction extends BaseAction {
//    private static final Logger LOG = LoggerFactory.getLogger(FraudIndicatorsAction.class);
    private JSONArray jObject;
    private int claimId;
    private static ClaimService claimService;

    public void setClaimId(int claimId) {
        this.claimId = claimId;
    }

    public static void setClaimService(ClaimService claimService) {
        FraudIndicatorsAction.claimService = claimService;
    }

    public String getJsonArrayData() {
        if (jObject != null) {
            return "{totalCount:" + this.jObject.size() + ",results:" + jObject.toString() + "}";
        }
        return "";
    }


    public String getFraudIndicators() {

        List<FraudIndicatorsViewData> viewDatas = new ArrayList<>();

        Claim claim = claimService.getClaim(claimId);
        List<KeoghsRequestScoreMessage> keoghsRequestScoreMessages = claim.getKeoghsRequest().getKeoghsRequestScoreMessages();

        for (KeoghsRequestScoreMessage sm : keoghsRequestScoreMessages) {
            viewDatas.add(new FraudIndicatorsViewData(sm));
        }

        this.jObject = JSONArray.fromObject(viewDatas);
        return SUCCESS;
    }

}