package idas.chox.core.model;

import idas.chox.core.bre.IBusinessRule;
import idas.chox.core.bre.RuleEvaluation;
import idas.chox.core.bre.RuleEvaluationResult;
import idas.chox.core.bre.RulesEngineResponse;
import idas.chox.core.util.DateHelper;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class History extends Entity implements Serializable {

    protected String ruleId;
    protected boolean isSystem;
    protected Claim claim;
    protected String narrative;
    protected Date processDate;
    protected boolean isPublic;
    protected String type;

    public History() {
    }

    public java.lang.String getNarrative() {
        return narrative;
    }

    public void setNarrative(java.lang.String narrative) {
        this.narrative = narrative;
    }

    public java.util.Date getProcessDate() {
        return processDate;
    }

    public void setProcessDate(java.util.Date processDate) {
        this.processDate = processDate;
    }

    public boolean getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }

    public java.lang.String getType() {
        return type;
    }

    public void setType(java.lang.String type) {
        this.type = type;
    }

    public String getRuleId() {
        return ruleId;
    }

    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }

    public boolean isIsSystem() {
        return isSystem;
    }

    public void setIsSystem(boolean isSystem) {
        this.isSystem = isSystem;
    }

    public Claim getClaim() {
        return claim;
    }

    public void setClaim(Claim claim) {
        this.claim = claim;
    }

    public static History New(Attachment attachment) {
        String strNarrative = String.format("New file is uploaded. [Claim id : %s][Category id : %s][File Name : %s][Attachment id : %s]", attachment.getClaim().getId(), attachment.getCategory(), attachment.getFileName(), attachment.getId());
        History his = new History();
        his.setClaim(attachment.getClaim());
        his.setIsPublic(true);
        his.setIsSystem(false);
        his.setNarrative(strNarrative);
        his.setProcessDate(DateHelper.getCurrentDateTime());
        his.setRuleId("H01");
        his.setType("INFO");
        return his;
    }

    public static History New(RuleEvaluation rv) {

        String sType = "INFO";
        if (rv.getResult() == RuleEvaluationResult.RuleFailed) {
            sType = "ERROR";
        }

        IBusinessRule rBusinessRule = rv.getRelatedRule();

        History history = new History();

        history.setProcessDate(DateHelper.getCurrentDateTime());
        history.setIsPublic(rv.getIsVisibleToCHO());
        history.setNarrative(rv.toString());
        history.setType(sType);
        history.setRuleId(rBusinessRule.getRuleId());
        history.setIsSystem(true);

        return history;

    }

    public static List<History> New(RulesEngineResponse rulesEngineResponse) {
        List<History> histories = new ArrayList<History>();
        List<RuleEvaluation> results = rulesEngineResponse.getResults();
        if (results != null) {
            for (RuleEvaluation result : results) {
                histories.add(New(result));
            }
        }
        return histories;
    }
}
