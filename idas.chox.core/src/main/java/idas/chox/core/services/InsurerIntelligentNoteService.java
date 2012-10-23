package idas.chox.core.services;

import idas.chox.core.model.InsurerIntelligentNote;

import java.util.List;

public interface InsurerIntelligentNoteService {

    public List<InsurerIntelligentNote> getInsurerIntelligentNotes(int insurerId);

    public void saveInsurerIntelligentNote(InsurerIntelligentNote insurerIntelligentNote);
    
    public InsurerIntelligentNote getInsurerIntelligentNote(int insurerIntelligentNoteId);

    public List<InsurerIntelligentNote> getInsurerIntelligeintNoteByNoteId(int intelligentNoteId);

}
