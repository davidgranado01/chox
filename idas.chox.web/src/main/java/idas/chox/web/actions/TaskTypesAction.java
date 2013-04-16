package idas.chox.web.actions;

import idas.chox.core.model.LookupItem;
import idas.chox.core.model.TaskType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.sf.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author John
 */
public class TaskTypesAction  extends BaseAction {
    private static final Logger LOG = LoggerFactory.getLogger(TaskTypesAction.class);
    private List<LookupItem> taskTypesLI;
    private JSONArray jObject;
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
            String jsonString = "{totalCount:" + this.jObject.size() + ",results:" + jObject.toString() + "}";
            LOG.debug("Returning json string: '{}'", jsonString);
            return jsonString;
        }
        return "";
    }

    public String getTaskTypes() {
        taskTypesLI = new ArrayList<LookupItem>();
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

        this.jObject = JSONArray.fromObject(taskTypesLI);

        return SUCCESS;
    }
}
