package idas.chox.web.viewdata;

import idas.chox.core.model.InsurerIntelligentNote;
import idas.chox.core.model.IntelligentNote;

public class InsurerIntelligentNoteViewData {
    
    private String intelligentNoteName;
    private String intelligentNote;
    private int id;
    private int intelligentNoteId;
    private boolean status;
    
    public InsurerIntelligentNoteViewData(IntelligentNote in, InsurerIntelligentNote iin, int insurerId) {
        //for displaying purposes we need unique id (in this case we concat id of insurer and intelligent note)
        Integer uniqueDisplayingId = Integer.parseInt(insurerId + "" + in.getIntelligentNoteId());
        
        this.intelligentNoteName = in.getIntelligentNoteName();
        this.intelligentNote = in.getNote();
        this.status = iin == null ? true : iin.isStatus();
        this.id = iin == null ? -uniqueDisplayingId  : iin.getId();
        this.intelligentNoteId = in.getIntelligentNoteId();
    }

    public String getIntelligentNote() {
        return intelligentNote;
    }

    public void setIntelligentNote(String note) {
        this.intelligentNote = note;
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

    public int getIntelligentNoteId() {
        return intelligentNoteId;
    }

    public void setIntelligentNoteId(int intelligentNoteId) {
        this.intelligentNoteId = intelligentNoteId;
    }

    public String getIntelligentNoteName() {
        return intelligentNoteName;
    }

    public void setIntelligentNoteName(String intelligentNoteName) {
        this.intelligentNoteName = intelligentNoteName;
    }
   
}
