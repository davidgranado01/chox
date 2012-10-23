package idas.chox.data.services;

import idas.chox.core.model.InsurerIntelligentNote;
import idas.chox.core.model.ReasonOfRejection;
import idas.chox.core.services.InsurerIntelligentNoteService;

import java.util.List;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InsurerIntelligentNoteServiceImpl  extends SecureDataService implements InsurerIntelligentNoteService {

    private static final Logger LOG = LoggerFactory.getLogger(InsurerIntelligentNoteServiceImpl.class);

    @Override
    public List<InsurerIntelligentNote> getInsurerIntelligentNotes(int insurerId) {
        DetachedCriteria criteria = DetachedCriteria.forClass(InsurerIntelligentNote.class);
        criteria.add(Restrictions.eq("insurer.id", insurerId));
        return findByCriteria(criteria);
    }

    @Override
    public void saveInsurerIntelligentNote(
            InsurerIntelligentNote insurerIntelligentNote) {
        save(insurerIntelligentNote);
    }

    @Override
    public InsurerIntelligentNote getInsurerIntelligentNote(
            int insurerIntelligentNoteId) {
        return (InsurerIntelligentNote) get(InsurerIntelligentNote.class, insurerIntelligentNoteId);
    }

    @Override
    public List<InsurerIntelligentNote> getInsurerIntelligeintNoteByNoteId(int intelligetnNoteid) {
        DetachedCriteria criteria = DetachedCriteria.forClass(InsurerIntelligentNote.class);
        criteria.add(Restrictions.eq("intelligent_note_id", intelligetnNoteid));
        return findByCriteria(criteria);
    }
    
    


}
