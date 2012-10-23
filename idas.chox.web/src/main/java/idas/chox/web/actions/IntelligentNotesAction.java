package idas.chox.web.actions;

import idas.chox.core.model.InsurerIntelligentNote;
import idas.chox.core.model.IntelligentNote;
import idas.chox.core.services.InsurerIntelligentNoteService;
import idas.chox.service.ActionResponse;
import idas.chox.service.admin.AdminInsurerService;
import idas.chox.service.intelligentNotes.IntelligentNoteDisplayEngine;
import idas.chox.web.viewdata.InsurerIntelligentNoteViewData;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.annotation.Secured;

import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;


public class IntelligentNotesAction extends BaseAction implements ModelDriven<InsurerIntelligentNote> , Preparable {
    
    private static final Logger LOG = LoggerFactory.getLogger(IntelligentNotesAction.class);

    private List<String> intelligentNotes;
    private List<IntelligentNote> availableIntelligentNotes;
    private List<InsurerIntelligentNote> insurerIntelligentNotes;
    private InsurerIntelligentNote model;
    private IntelligentNoteDisplayEngine intelligentNoteDisplayEngine;
    private InsurerIntelligentNoteService insurerIntelligentNoteService;
    private AdminInsurerService adminInsurerService;
    private int intelligentNoteId = -1;
    private int insurerId = -1;
    private List<InsurerIntelligentNoteViewData> insurerIntelligentNoteViewData = new ArrayList<InsurerIntelligentNoteViewData>();
    
    public List<String> getIntelligentNotes() {
        if (intelligentNotes == null) {
            intelligentNotes = intelligentNoteDisplayEngine.getAllIntelligentNotes();
        }
        LOG.debug("Returning {} intelligent notes.", intelligentNotes.size());
        return intelligentNotes;
    }
    
    public String getInteligentNotes() {
        try {
            List<IntelligentNote> availableIntelligentNotes = intelligentNoteDisplayEngine.getAvailableIntelligentNotes();
            for (IntelligentNote in : availableIntelligentNotes) {
                insurerIntelligentNoteViewData.add(new InsurerIntelligentNoteViewData(in, getInsurerIntelligeintNoteById(in.getIntelligentNoteId(), insurerId)));
            }
        } catch (Exception ex) {
            handleException(ex);
            return ERROR;
        }
        return SUCCESS;
    }
    
    public void setIntelligentNotes(List<String> intelligentNotes) {
        this.intelligentNotes = intelligentNotes;
    }
    
    public void setIntelligentNoteDisplayEngine(
            IntelligentNoteDisplayEngine intelligentNoteDisplayEngine) {
        this.intelligentNoteDisplayEngine = intelligentNoteDisplayEngine;
    }
    
    private InsurerIntelligentNote getInsurerIntelligeintNoteById(int intelligentNoteId, int insurerId){
        List<InsurerIntelligentNote> iin = insurerIntelligentNoteService.getInsurerIntelligentNotes(insurerId);
        for (InsurerIntelligentNote insurerIntelligentNote : iin) {
            if(insurerIntelligentNote.getId() == intelligentNoteId){
                return insurerIntelligentNote;
            }
        }
        return null;
    }
    
    public String getJsonData() {
        JSONArray jObject = JSONArray.fromObject(this.getInsurerIntelligentNoteViewData());
        return "{totalCount:" + this.getInsurerIntelligentNoteViewData().size() + ",results:" + jObject.toString() + "}";
    }
    
    @Secured ({"ROLE_CHOX_ADMIN"})
    public String updateInsurerInteligentNoteStatus() throws Exception {
        try {
            if (!getIsChoxAdmin()) {
                LOG.error("Trying to update a Inteligent Note status for an insurer that isn't mine (POSSIBLE HACK ATTEMPT): {}");
                throw new AccessDeniedException("Trying to update a Inteligent Note status for an insurer that isn't mine (POSSIBLE HACK ATTEMPT)");
            }
            
            if (this.intelligentNoteId > 0) {
                InsurerIntelligentNote iiNote = insurerIntelligentNoteService.getInsurerIntelligentNote(intelligentNoteId);
                iiNote.setStatus(!iiNote.isStatus());
                ActionResponse response;
                response = adminInsurerService.updateInsurerIntelligentNote(iiNote);
                setActionResponse(response);
            }
        } catch (Exception ex) {
            handleException(ex);
            return ERROR;
        }
        return SUCCESS;
    }

    @Override
    public InsurerIntelligentNote getModel() {
        return model;
    }

    @Override
    public void prepare() throws Exception {
        model = new InsurerIntelligentNote();
    }

    public List<IntelligentNote> getAvailableIntelligentNotes() {
        return availableIntelligentNotes;
    }

    public void setAvailableIntelligentNotes(
            List<IntelligentNote> availableIntelligentNotes) {
        this.availableIntelligentNotes = availableIntelligentNotes;
    }

    public List<InsurerIntelligentNote> getInsurerIntelligentNotes() {
        return insurerIntelligentNotes;
    }

    public void setInsurerIntelligentNotes(
            List<InsurerIntelligentNote> insurerIntelligentNotes) {
        this.insurerIntelligentNotes = insurerIntelligentNotes;
    }

    public IntelligentNoteDisplayEngine getIntelligentNoteDisplayEngine() {
        return intelligentNoteDisplayEngine;
    }

    public void setModel(InsurerIntelligentNote model) {
        this.model = model;
    }

    public void setInsurerIntelligentNoteService(
            InsurerIntelligentNoteService insurerIntelligentNoteService) {
        this.insurerIntelligentNoteService = insurerIntelligentNoteService;
    }

    public void setAdminInsurerService(AdminInsurerService adminInsurerService) {
        this.adminInsurerService = adminInsurerService;
    }

    public int getIntelligentNoteId() {
        return intelligentNoteId;
    }

    public void setIntelligentNoteId(int intelligentNoteId) {
        this.intelligentNoteId = intelligentNoteId;
    }

    public List<InsurerIntelligentNoteViewData> getInsurerIntelligentNoteViewData() {
        return insurerIntelligentNoteViewData;
    }

    public void setInsurerIntelligentNoteViewData(
            List<InsurerIntelligentNoteViewData> insurerIntelligentNoteViewData) {
        this.insurerIntelligentNoteViewData = insurerIntelligentNoteViewData;
    }

    public int getInsurerId() {
        return insurerId;
    }

    public void setInsurerId(int insurerId) {
        this.insurerId = insurerId;
    }

}
