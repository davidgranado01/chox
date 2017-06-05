package idas.chox.web.actions;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import idas.chox.core.model.History;
import idas.chox.service.security.TabAccessibility;
import idas.chox.web.viewdata.HistoryViewData;

public class HistoryAction extends ClaimModelAction<History> {

    private String jObject;
    private int jObjectSize;

    public String doRenderActionPage() {
        return SUCCESS;
    }

    public String getJsonArrayData() {
        if (jObject != null) {
            return "{totalCount:" + jObjectSize + ",results:" + jObject + "}";
        }
        return "";
    }

    /*
     * isPublic : true > SHOW ALL RECORDS WITH IS_PUBLIC IS TRUE ONLY
     * isPublic : false > SHOW ALL RECORDS REGARDLESS THE IS_PUBLIC
     */
    public String getHistory() {

        List<HistoryViewData> histories = new ArrayList<>();

        for (History h : claim.getHistories()) {

            if (h.getType().equalsIgnoreCase("Error") && (h.getIsPublic() || !this.getIsCHO())) {
                // SHOW ERROR TYPE ONLY
                // REJECT isPublic is FALSE && Is CHO USER
                histories.add(new HistoryViewData(h));
            }
        }

        Collections.sort(histories);
        
        ObjectMapper mapper = new ObjectMapper();
        try {
            jObject = mapper.writeValueAsString(histories);
            jObjectSize = histories.size();
        } catch (JsonProcessingException ex) {
            jObject = null;
        }
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