package idas.chox.web.viewdata;

import idas.chox.core.model.InsurerIntelligentNote;
import idas.chox.core.model.IntelligentNote;
import idas.chox.core.util.DateHelper;

public class InsurerIntelligentNoteViewData {
    
    private String note;
    private int id;
    private boolean status;
    
    public InsurerIntelligentNoteViewData(IntelligentNote in, InsurerIntelligentNote iin) {
        this.note = in.getNote();
        this.status = iin.isStatus();
        this.id = iin.getId();
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
   
}
