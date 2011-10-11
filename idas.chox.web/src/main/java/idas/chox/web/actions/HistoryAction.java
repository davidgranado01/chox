package idas.chox.web.actions;

import idas.chox.core.model.History;
import idas.chox.web.viewdata.HistoryViewData;
import java.util.ArrayList;
import java.util.List;
import net.sf.json.JSONArray;
import idas.chox.service.security.ApplicationAccessibility;
import java.util.Collections;

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

    /*
     * isPublic : true > SHOW ALL RECORDS WITH IS_PUBLIC IS TRUE ONLY
     * isPublic : false > SHOW ALL RECORDS REGARDLESS THE IS_PUBLIC
     */
    public String getHistory() {

        List<HistoryViewData> histories = new ArrayList<HistoryViewData>();
        histories = new ArrayList<HistoryViewData>();

        for (History h : claim.getHistories()) {

            if (h.getType().equalsIgnoreCase("Error") && (h.getIsPublic() || !this.getIsCHO())) {
                // SHOW ERROR TYPE ONLY
                // REJECT isPublic is FALSE && Is CHO USER
                histories.add(new HistoryViewData(h));
            }
        }

        Collections.sort(histories);
        
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