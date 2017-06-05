package idas.chox.web.actions;

import idas.chox.core.model.Claim;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.model.KeoghsRequestScoreMessage;
import idas.chox.core.services.ClaimService;
import idas.chox.web.viewdata.FraudIndicatorsViewData;

public class FraudIndicatorsAction extends BaseAction {
    private static final Logger LOG = LoggerFactory.getLogger(FraudIndicatorsAction.class);
    private String jObject;
    private int jObjectSize;
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
            return "{totalCount:" + jObjectSize + ",results:" + jObject + "}";
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

        ObjectMapper mapper = new ObjectMapper();
        try {
            jObject = mapper.writeValueAsString(viewDatas);
            jObjectSize = viewDatas.size();
        } catch (JsonProcessingException ex) {
            LOG.error("Error converting Fraud luItems to json string.");
            jObject = null;
        }
        return SUCCESS;
    }

}