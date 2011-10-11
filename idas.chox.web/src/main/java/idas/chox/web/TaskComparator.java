package idas.chox.web;

import idas.chox.web.viewdata.TaskViewData;
import java.util.Comparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author seeni
 */
public class TaskComparator implements Comparator<TaskViewData> {

    private final String name;
    private static final Logger LOG = LoggerFactory.getLogger(TaskComparator.class);

    public TaskComparator(String name) {
         LOG.debug("sorting column name is set in the class constructor sorting field is={}",name);
        this.name = name;
    }

    @Override
    public int compare(TaskViewData t, TaskViewData t1) {
        int r = 0;
        if (name.equalsIgnoreCase("choReference")) {
            r = t.getChoReference().compareTo(t1.getChoReference());
        } else if (name.equalsIgnoreCase("dueDate")) {
            r = t.getDueDate_dateFormat().compareTo(t1.getDueDate_dateFormat());
        } else if (name.equalsIgnoreCase("type")) {
            r = t.getType().compareTo(t1.getType());
        } else if (name.equalsIgnoreCase("description")) {
            r = t.getDescription().compareTo(t1.getDescription());
        } else if (name.equalsIgnoreCase("createdDate")) {
            r = t.getCreatedDate_dateFormat().compareTo(t1.getCreatedDate_dateFormat());
        } else if (name.equalsIgnoreCase("createdBy")) {
            r = t.getCreatedBy().compareTo(t1.getCreatedBy());
        }
        return r;
    }
}
