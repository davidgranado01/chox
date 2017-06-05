package idas.chox.web.actions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.model.LookupItem;
import idas.chox.core.model.TaskType;

/**
 *
 * @author John
 */
public class TaskTypesAction  extends BaseAction {
    private static final Logger LOG = LoggerFactory.getLogger(TaskTypesAction.class);
    private List<LookupItem> taskTypesLI;
    private String jObject;
    private int jObjectSize;
    private int visibility;
    private String visibilityRole;

    public void setVisibility(int visibility) {
        this.visibility = visibility;
    }

    public void setVisibilityRole(String visibilityRole) {
        this.visibilityRole = visibilityRole;
    }


    public String getJsonArrayData() {
        if (jObject != null) {
            String jsonString = "{totalCount:" + jObjectSize + ",results:" + jObject + "}";
            LOG.debug("Returning json string: '{}'", jsonString);
            return jsonString;
        }
        return "";
    }

    public String getTaskTypes() {
        taskTypesLI = new ArrayList<>();
        Map<String, String> taskTypes;
        boolean isCHO = getIsCHO();
        LOG.debug("Getting task types for visibility={} and isCHO={}", visibility, isCHO);
        if (visibility == 1) {
            taskTypes = TaskType.getPrivateTaskTypes();
        } else if (visibility == 2 && isCHO) {
            taskTypes = TaskType.getChoInternalTaskTypes();
        } else if (visibility == 2 && !isCHO) {
            taskTypes = TaskType.getInsurerInternalTaskTypes();
        } else if (visibility == 3 && isCHO) {
            taskTypes = TaskType.getChoExternalTaskTypes();
        } else if (visibility == 3 && !isCHO) {
            taskTypes = TaskType.getInsurerExternalTaskTypes();
        } else {
            LOG.warn("Cannot determine which tasks to return for visibility={}", visibility);
            return ERROR;
        }
        for (Map.Entry<String, String> entry : taskTypes.entrySet()) {
            taskTypesLI.add(new LookupItem(entry.getKey(), entry.getValue()));
        }

        ObjectMapper mapper = new ObjectMapper();
        String jsonString = null;
        try {
            jObject = mapper.writeValueAsString(taskTypesLI);
            jObjectSize = taskTypesLI.size();
        } catch (JsonProcessingException ex) {
            LOG.error("Error converting taskTypes to json string.");
            jObject = null;
        }

        return SUCCESS;
    }
}
