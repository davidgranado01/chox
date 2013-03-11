package idas.chox.core.services;

import java.util.List;
import java.util.Map;

import idas.chox.core.model.InsurerIntelligentNote;


public interface InsurerIntelligentNoteService {

    Map<Integer, InsurerIntelligentNote> getInsurerIntelligentNotesMap(int insurerId, Boolean status);

    void updateInsurerIntelligentNote(InsurerIntelligentNote insurerIntelligentNote);
    
    InsurerIntelligentNote getInsurerIntelligentNote(int insurerIntelligentNoteId);

    List<InsurerIntelligentNote> getInsurerIntelligeintNoteByNoteId(int intelligentNoteId);

    void createInsurerIntelligentNote(int intelligentNoteId, int insurerId, boolean status);

}
