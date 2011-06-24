package idas.chox.web;

import idas.chox.core.model.History;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author seeni
 */
public class ExcelHistory {

    private List<History> histories;

    public List<History> getHistories() {
        return histories;
    }

    public void setHistories(List<History> histories, boolean isCHO) {
        History history;
        List<History> newHistories = new ArrayList();
        if (histories != null) {
            Iterator iterator = histories.iterator();
            while (iterator.hasNext()) {
                history = (History) iterator.next();
                if (!(history.getType().equals("INFO")) && (history.getIsPublic() || !isCHO)) {
                    newHistories.add(history);
                }
            }
        }
        this.histories = newHistories;
    }
}
