/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.web.actions;

import idas.chox.data.services.DataService;
import java.util.List;
import net.sf.json.JSONArray;

/**
 *
 * @author Emmanuel
 */
public class TestAction extends BaseAction {

    private DataService dataService;
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

    public DataService getDataService() {
        return dataService;
    }

    public void setDataService(DataService dataService) {
        this.dataService = dataService;
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
 