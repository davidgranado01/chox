package idas.chox.web.viewdata;

import idas.chox.core.model.Chorganisation;
import idas.chox.core.model.Insurer;
import idas.chox.core.model.Task;
import idas.chox.core.model.WebUser;
import java.text.Format;
import java.text.SimpleDateFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author John
 */
public class TaskViewData {
    private static final Logger LOG = LoggerFactory.getLogger(TaskViewData.class);
    private int id;
    private int claimId;
    private String choReference;
    private String dueDate;
    private String completedDate;
    private String type;
    private String description;
    private Boolean complete;
    private String createdBy;
    private String completedBy;
    private String createdDate;

    public TaskViewData(Task task) {
        Format dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        this.id = task.getId();
        this.createdDate = dateFormat.format(task.getCreatedDate());
        this.dueDate = dateFormat.format(task.getDueDate());
        this.complete = task.getComplete();
        if (complete) {
            this.completedDate = dateFormat.format(task.getCompletedDate());
            LOG.debug("Completed date set to '{}'", completedDate);
        }
        else
            this.completedDate = "";
        this.type = task.getType();
        this.description = task.getDescription();
        if (task.getClaim() != null) {
            choReference = task.getClaim().getChoReference();
            claimId =  task.getClaim().getId();
        }
        else
            choReference = "";

        String orgName = "";
        WebUser user = task.getCreatedBy();
        if (user != null) {
            Chorganisation cho = user.getChorganisation();
            Insurer ins = user.getInsurer();

            if (ins != null) {
                orgName = String.format("(%1$s)", ins.getName());
            } else if (cho != null) {
                orgName = String.format("(%1$s)", cho.getName());
            }
            this.createdBy = String.format("%1$s %2$s %3$s", user.getFirstName(), user.getLastName(), orgName);
        }
        user = task.getCompletedBy();
        if (user != null) {
            Chorganisation cho = user.getChorganisation();
            Insurer ins = user.getInsurer();

            if (ins != null) {
                orgName = String.format("(%1$s)", ins.getName());
            } else if (cho != null) {
                orgName = String.format("(%1$s)", cho.getName());
            }
            this.completedBy = String.format("%1$s %2$s %3$s", user.getFirstName(), user.getLastName(), orgName);
        }
    }

    public String getChoReference() {
        return choReference;
    }

    public Boolean getComplete() {
        return complete;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public String getDescription() {
        return description;
    }

    public String getDueDate() {
        return dueDate;
    }

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getCompletedDate() {
        return completedDate;
    }

    public String getCompletedBy() {
        return completedBy;
    }

    public int getClaimId() {
        return claimId;
    }

}
