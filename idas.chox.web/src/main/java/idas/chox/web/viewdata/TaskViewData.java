package idas.chox.web.viewdata;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.model.Chorganisation;
import idas.chox.core.model.Insurer;
import idas.chox.core.model.Task;
import idas.chox.core.model.WebUser;

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
    private String toBeCompletedBy;
    private String createdDate;
    private Date dueDate_dateFormat;
    private Date createdDate_dateFormat;

    public TaskViewData(Task task, boolean showInsurerRole) {
        Format dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        this.id = task.getId();
        this.createdDate = dateFormat.format(task.getCreatedDate());
        this.createdDate_dateFormat = task.getCreatedDate();
        this.dueDate = dateFormat.format(task.getDueDate());
        this.dueDate_dateFormat = task.getDueDate();
        this.complete = task.getComplete();
        if (complete) {
            this.completedDate = dateFormat.format(task.getCompletedDate());
            LOG.debug("Completed date set to '{}'", completedDate);
        } else {
            this.completedDate = "";
        }
        this.type = task.getType();
        this.description = task.getDescription();
        if (task.getClaim() != null) {
            choReference = task.getClaim().getChoReference();
            claimId = task.getClaim().getId();
        } else {
            choReference = "";
        }

        if ((task.getInsurer() && task.getVisibility() != 3) || (!task.getInsurer() && task.getVisibility() == 3)) {
            if (showInsurerRole) {
                toBeCompletedBy = task.getVisibilityRole();
                // ToDo : This is a hack - we should get these descriptions from the database (or, alternatively,
                // also store this in the Task
                switch (toBeCompletedBy) {
                    case "ROLE_INS_CH":
                        toBeCompletedBy = "Claim Handler";
                        break;
                    case "ROLE_INS_COM":
                        toBeCompletedBy = "Claim Ownership Manager";
                        break;
                    case "ROLE_INS_FNOL":
                        toBeCompletedBy = "FNOL Handler";
                        break;
                    case "ROLE_INS_PC":
                        toBeCompletedBy = "Payments Clerk";
                        break;
                    case "ROLE_INS_CR":
                        toBeCompletedBy = "Claim Router";
                        break;
                    case "ROLE_INS_SCR":
                        toBeCompletedBy = "Engineer";
                        break;
                    case "ROLE_INS_SUP":
                        toBeCompletedBy = "Supervisor";
                        break;
                    case "ROLE_CHO_MI":
                        toBeCompletedBy = "MI User";
                        break;
                    case "ROLE_INS_ADMIN":
                        toBeCompletedBy = "Admin Manager";
                        break;
                    case "ROLE_INS_USER":
                        toBeCompletedBy = "User Manager";
                        break;
                    case "ROLE_INS_MNG":
                        toBeCompletedBy = "Manager";
                        break;
                    case "ROLE_INS_UPLOAD":
                        toBeCompletedBy = "Insurer Claims Uploader";
                        break;
                    case "ROLE_INS":
                        toBeCompletedBy = "User";
                        break;
                }
            } else {
                toBeCompletedBy = "Insurer";
            }
        } else {
            toBeCompletedBy = "CHO";
        }
        String orgName = "";
        WebUser user = task.getRaisedBy();
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

    public Date getCreatedDate_dateFormat() {
        return createdDate_dateFormat;
    }

    public Date getDueDate_dateFormat() {
        return dueDate_dateFormat;
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

    public String getToBeCompletedBy() {
        return toBeCompletedBy;
    }
}
