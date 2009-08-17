/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chox.web.viewdata;

import chox.model.Chorganisation;
import chox.model.History;
import chox.model.Insurer;
import chox.model.WebUser;
import java.text.Format;
import java.text.SimpleDateFormat;

/**
 *
 * @author Emmanuel
 */
public class HistoryViewData {

    private int id;
    private String createdBy;
    private String createdDate;
    private String narrative;

    public HistoryViewData(History history) {
        Format dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        this.id = history.getId();
        this.createdDate = dateFormat.format(history.getCreatedDate());
        this.narrative = history.getNarrative();
        String orgName = "";
        WebUser user = history.getCreatedBy();
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
    }

    public int getId() {
        return id;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public String getNarrative() {
        return narrative;
    }
}
