package idas.chox.core.model;

public class InsurerIntelligentNote {
    
    private Integer id;
    private Insurer insurer;
    private Integer intelligentNoteId;
    private boolean status;
    
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public Insurer getInsurer() {
        return insurer;
    }
    public void setInsurer(Insurer insurer) {
        this.insurer = insurer;
    }
    
    public boolean isStatus() {
        return status;
    }
    public void setStatus(boolean status) {
        this.status = status;
    }
    public Integer getIntelligentNoteId() {
        return intelligentNoteId;
    }
    public void setIntelligentNoteId(Integer intelligentNoteId) {
        this.intelligentNoteId = intelligentNoteId;
    }

}
