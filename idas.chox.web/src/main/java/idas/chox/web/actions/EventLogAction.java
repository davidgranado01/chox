package idas.chox.web.actions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import idas.chox.data.services.EventLogServiceImp;
import idas.chox.events.BaseActivityEvent;
import idas.chox.service.security.TabAccessibility;
import idas.chox.web.viewdata.EventLogViewData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.annotation.Secured;

import java.util.ArrayList;
import java.util.List;

public class EventLogAction extends ClaimModelAction<BaseActivityEvent> {
    private static final Logger LOG = LoggerFactory.getLogger(EventLogAction.class);
    private int eventLogId;
    private String jsonArrayData;
    private EventLogServiceImp eventLogService;

    public void setEventLogService(EventLogServiceImp eventLogService) {
        this.eventLogService = eventLogService;
    }
    
    @Secured ({"ROLE_CHOX_ADMIN"})
    public String getJsonArrayData() {
        return jsonArrayData;
    }

    public void setJsonArrayData(String jsonArrayData) {
        this.jsonArrayData = jsonArrayData;
    }

    @Secured({"ROLE_CHOX_ADMIN"})
    public String doRenderActionPage() {
        return SUCCESS;
    }

    @Secured({"ROLE_CHOX_ADMIN"})
    public String getEventLogs() {
        List<EventLogViewData> viewDetailList = new ArrayList<>();
        List<BaseActivityEvent> eventLogs = eventLogService.getEventLogByClaim(claim);

        LOG.debug("Event log record size: {} ", eventLogs.size());
        for (BaseActivityEvent eventLog : eventLogs) {
            viewDetailList.add(new EventLogViewData(eventLog));
        }

        ObjectMapper mapper = new ObjectMapper();
        String jsonString = null;
        try {
            jsonString = mapper.writeValueAsString(viewDetailList);
        } catch (JsonProcessingException ex) {
            LOG.error("Error converting viewList list to json string.");
        }

        setJsonArrayData("{\"totalCount\":" + eventLogs.size() + ",\"results\":" + jsonString + "}");
        return SUCCESS;
    }

    @Override
    String getTabName() {
        return TabAccessibility.TAB_EVENT_LOG;
    }

    @Override
    protected BaseActivityEvent loadModel() {
        if (eventLogId > 0) {
            return (BaseActivityEvent) baseDataService.get(BaseActivityEvent.class, eventLogId);
        } else {
            return new BaseActivityEvent();
        }
    }
}
