package idas.chox.core.model;

public class InsurerIntelligentNote {
    
    private Integer id;
    private Insurer insurer;
    private String intelligent_note_id;
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
    public String getIntelligent_note_id() {
        return intelligent_note_id;
    }
    public void setIntelligent_note_id(String intelligent_note_id) {
        this.intelligent_note_id = intelligent_note_id;
    }
    public boolean isStatus() {
        return status;
    }
    public void setStatus(boolean status) {
        this.status = status;
    }

}
