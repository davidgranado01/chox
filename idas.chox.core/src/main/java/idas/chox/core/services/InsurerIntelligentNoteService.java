package idas.chox.core.services;

import idas.chox.core.model.InsurerIntelligentNote;

import java.util.List;
import java.util.Map;

public interface InsurerIntelligentNoteService {

    public Map<Integer, InsurerIntelligentNote> getInsurerIntelligentNotesMap(int insurerId, Boolean status);

    public void updateInsurerIntelligentNote(InsurerIntelligentNote insurerIntelligentNote);
    
    public InsurerIntelligentNote getInsurerIntelligentNote(int insurerIntelligentNoteId);

    public List<InsurerIntelligentNote> getInsurerIntelligeintNoteByNoteId(int intelligentNoteId);

    public void createInsurerIntelligentNote(int intelligentNoteId, int insurerId, boolean status);

}
