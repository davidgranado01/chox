package idas.chox.core.model;

/**
 *
 * @author emmanuel
 */
public interface IntelligentNote {
    public Boolean isShowingFor(Claim c);
    public String getNote();
    public int getIntelligentNoteId();
    public String getIntelligentNoteName();
}
