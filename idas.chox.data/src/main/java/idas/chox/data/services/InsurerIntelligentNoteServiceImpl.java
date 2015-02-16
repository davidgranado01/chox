package idas.chox.data.services;

import idas.chox.core.model.InsurerIntelligentNote;
import idas.chox.core.services.InsurerIntelligentNoteService;
import idas.chox.core.services.InsurerService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public class InsurerIntelligentNoteServiceImpl  extends SecureDataService implements InsurerIntelligentNoteService {

    private static final Logger LOG = LoggerFactory.getLogger(InsurerIntelligentNoteServiceImpl.class);
    
    InsurerService insurerService;

    @Override
    public Map<Integer, InsurerIntelligentNote> getInsurerIntelligentNotesMap(int insurerId, Boolean status) {
        DetachedCriteria criteria = DetachedCriteria.forClass(InsurerIntelligentNote.class);
        criteria.add(Restrictions.eq("insurer.id", insurerId));
        if(status != null){
            criteria.add(Restrictions.eq("status", status.booleanValue()));
        }
        List<InsurerIntelligentNote> iinList = findByCriteria(criteria);
        Map<Integer, InsurerIntelligentNote> iinMap = new HashMap<Integer, InsurerIntelligentNote>();
        for(InsurerIntelligentNote iin : iinList){
            iinMap.put(iin.getIntelligentNoteId(), iin);
        }
        return iinMap;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, value="transactionManager")
    public void updateInsurerIntelligentNote(
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

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, value="transactionManager")
    public void createInsurerIntelligentNote(int intelligentNoteId,
            int insurerId, boolean status) {
        InsurerIntelligentNote iin = new InsurerIntelligentNote();
        iin.setInsurer(insurerService.getInsurer(insurerId));
        iin.setStatus(status);
        iin.setIntelligentNoteId(intelligentNoteId);
        save(iin);
    }

    public void setInsurerService(InsurerService insurerService) {
        this.insurerService = insurerService;
    }

}
