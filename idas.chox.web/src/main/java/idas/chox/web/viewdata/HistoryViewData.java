package idas.chox.web.viewdata;

import idas.chox.core.model.Chorganisation;
import idas.chox.core.model.History;
import idas.chox.core.model.Insurer;
import idas.chox.core.model.WebUser;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Emmanuel
 */
public class HistoryViewData implements Comparable<HistoryViewData> {

    private int id;
    private String createdBy;
    private String createdDate;
    private String narrative;
    private boolean isOld;

    public HistoryViewData(History history) {
        Format dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        this.id = history.getId();
        this.createdDate = dateFormat.format(history.getCreatedDate());
        this.narrative = history.getNarrative();
        this.isOld = history.getIsOld();
        
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

    public boolean isIsOld() {
        return isOld;
    }

    @Override
    public int compareTo(HistoryViewData t) {
        Format dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date1;
        Date date2;
        try {
            date1 = (Date)dateFormat.parseObject(t.createdDate);
            date2 = (Date)dateFormat.parseObject(this.createdDate);
        } catch (ParseException e) {
            return 0; // cannot happen!!
        }
        
        if (date1.equals(date2))
            return 0;
        else if (date1.before(date2))
            return -1;
        else
            return 1;
    }
}
