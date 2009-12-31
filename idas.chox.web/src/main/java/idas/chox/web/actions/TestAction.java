/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.web.actions;

import idas.chox.data.services.BaseDataService;
import java.util.List;
import net.sf.json.JSONArray;

/**
 *
 * @author Emmanuel
 */
public class TestAction extends BaseAction {

    private BaseDataService baseDataService;
    private String query;
    private List result;

    public String runHQL() {
        String q = getQuery();
        result = getDataService().query(q);
        return SUCCESS;
    }

    public String runSQL() {
        String q = getQuery();
        result = getDataService().externalQuery(q);
        return SUCCESS;
    }

    @Override
    public String execute() {
        return SUCCESS;
    }

    public BaseDataService getDataService() {
        return baseDataService;
    }

    public void setDataService(BaseDataService baseDataService) {
        this.baseDataService = baseDataService;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getJsonData() {
        JSONArray jsonArray = JSONArray.fromObject(result);
        return "{totalCount:" + result.size() + ",results:" + jsonArray.toString() + "}";
    }
}
 