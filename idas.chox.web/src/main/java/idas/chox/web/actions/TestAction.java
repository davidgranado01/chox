package idas.chox.web.actions;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import idas.chox.data.services.BaseDataService;

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
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = null;
        try {
            jsonString = mapper.writeValueAsString(result);
        } catch (JsonProcessingException ex) {
        }
        return "{totalCount:" + result.size() + ",results:" + jsonString + "}";
    }
}
 