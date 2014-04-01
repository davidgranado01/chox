package idas.chox.web.viewdata;

import idas.chox.core.model.Chorganisation;
import idas.chox.core.model.History;
import idas.chox.core.model.Insurer;
import idas.chox.core.model.WebUser;
import idas.chox.core.util.DateHelper;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Emmanuel
 */
public class HistoryViewData implements Comparable<HistoryViewData> {

    private static final Logger LOG = LoggerFactory.getLogger(HistoryViewData.class);
    private int id;
    private String createdBy;
    private String createdDate;
    private String narrative;
    private boolean isOld;

    public HistoryViewData(History history) {
        this.id = history.getId();
        this.createdDate =  DateHelper.getLocalDateTimeFormat().format(history.getCreatedDate());
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

    // Order by latest histories first.(descending date order). 
    @Override
    public int compareTo(HistoryViewData t) {
        Format dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        try {
            Date date1 = (Date) dateFormat.parseObject(t.getCreatedDate());
            Date date2 = (Date) dateFormat.parseObject(this.createdDate);
            return date1.compareTo(date2);
        } catch (Exception ex) {
            LOG.error("Exception while ordering histories: ", ex);
            return 0;
        }
    }
}
