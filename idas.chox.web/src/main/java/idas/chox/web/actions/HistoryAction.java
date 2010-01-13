package idas.chox.web.actions;

// import idas.chox.core.model.Claim;
import idas.chox.core.model.History;
import idas.chox.web.viewdata.HistoryViewData;
import java.util.ArrayList;
import java.util.List;
import net.sf.json.JSONArray;
import idas.chox.service.security.ApplicationAccessibility;

public class HistoryAction extends ClaimModelAction<History> {

    private JSONArray jObject;

    public String doRenderActionPage() {
        return SUCCESS;
    }

    public String getJsonArrayData() {
        if (jObject != null) {
            return "{totalCount:" + this.jObject.size() + ",results:" + jObject.toString() + "}";
        }
        return "";
    }

    public String getHistory() {
        List<HistoryViewData> histories = new ArrayList<HistoryViewData>();
        // Claim claim = claimService.getClaim(claimId);
        histories = new ArrayList<HistoryViewData>();
        for (History h : claim.getHistories()) {
            histories.add(new HistoryViewData(h));
        }
        this.jObject = JSONArray.fromObject(histories);
        return SUCCESS;
    }

    @Override
    String getTabName() {
        return ApplicationAccessibility.TAB_HISTORY;
    }

    @Override
    protected History loadModel() {
        return new History();
    }
}