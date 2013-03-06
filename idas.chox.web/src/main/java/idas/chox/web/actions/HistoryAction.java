package idas.chox.web.actions;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

import net.sf.json.JSONArray;

import idas.chox.core.model.History;
import idas.chox.service.security.TabAccessibility;
import idas.chox.web.viewdata.HistoryViewData;

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
        return TabAccessibility.TAB_HISTORY;
    }

    @Override
    protected History loadModel() {
        return new History();
    }
}