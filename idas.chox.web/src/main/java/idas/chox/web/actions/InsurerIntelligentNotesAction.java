package idas.chox.web.actions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.annotation.Secured;

import idas.chox.core.model.InsurerIntelligentNote;
import idas.chox.core.model.IntelligentNote;
import idas.chox.core.services.InsurerIntelligentNoteService;
import idas.chox.service.ActionResponse;
import idas.chox.service.admin.AdminInsurerService;
import idas.chox.service.intelligentNotes.IntelligentNoteDisplayEngine;
import idas.chox.web.viewdata.InsurerIntelligentNoteViewData;

public class InsurerIntelligentNotesAction extends BaseAction implements ModelDriven<InsurerIntelligentNote> , Preparable {
    
    private static final Logger LOG = LoggerFactory.getLogger(InsurerIntelligentNotesAction.class);

    private List<InsurerIntelligentNote> insurerIntelligentNotes;
    private InsurerIntelligentNote model;
    private IntelligentNoteDisplayEngine intelligentNoteDisplayEngine;
    private InsurerIntelligentNoteService insurerIntelligentNoteService;
    private AdminInsurerService adminInsurerService;
    private int insurerId = -1;
    private int insurerInteligentNoteId = -1;
    private List<InsurerIntelligentNoteViewData> insurerIntelligentNoteViewData = new ArrayList<>();
    
    @Secured({"ROLE_CHOX_ADMIN"})
    public String doRenderActionPage() {
        return SUCCESS;
    }
    
    public String getInteligentNotes() {
        try {
            List<IntelligentNote> availableIntelligentNotes = intelligentNoteDisplayEngine.getAvailableIntelligentNotes();
            Map<Integer, InsurerIntelligentNote> mapOfInsurerIntelegentNotes = insurerIntelligentNoteService.getInsurerIntelligentNotesMap(getInsurerId(), null);
            for (IntelligentNote in : availableIntelligentNotes) {
                if(!mapOfInsurerIntelegentNotes.containsKey(in.getIntelligentNoteId())){
                    insurerIntelligentNoteViewData.add(new InsurerIntelligentNoteViewData(in, null, insurerId));
                    LOG.debug("Adding insurer innteligent note ({}) - it is in DB", in.getIntelligentNoteName());
                } else {
                    insurerIntelligentNoteViewData.add(new InsurerIntelligentNoteViewData(in, mapOfInsurerIntelegentNotes.get(in.getIntelligentNoteId()), getInsurerId()));
                    LOG.debug("Adding new insurer innteligent note ({}) ", in.getIntelligentNoteName());
                }
            }
        } catch (Exception ex) {
            handleException(ex);
            return ERROR;
        }
        return SUCCESS;
    }
    
    public String getJsonData() {
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = null;
        try {
            jsonString = mapper.writeValueAsString(insurerIntelligentNoteViewData);
        } catch (JsonProcessingException ex) {
            LOG.error("Error converting insurerIntelligentNoteViewData to json string.");
        }
        return "{totalCount:" + this.getInsurerIntelligentNoteViewData().size() + ",results:" + jsonString + "}";
    }
    
    @Secured ({"ROLE_CHOX_ADMIN"})
    public String updateInsurerInteligentNoteStatus() throws Exception {
        try {
            //insurer intelligent notes are inserted into DB when it is first updated (in case we display new insurer intelligent note it has negative id)
            if (this.insurerInteligentNoteId > 0) {
                InsurerIntelligentNote iiNote = insurerIntelligentNoteService.getInsurerIntelligentNote(insurerInteligentNoteId);
                LOG.debug("Updating insurer inteligent note ({}) with id: {}", iiNote.getIntelligentNoteId() ,iiNote.getId());
                iiNote.setStatus(!iiNote.isStatus());
                ActionResponse response;
                response = adminInsurerService.updateInsurerIntelligentNote(iiNote);
                setActionResponse(response);
            } else {
                LOG.debug("Inserting insurer inteligent note ({}) with temporary display id: {}", model.getIntelligentNoteId() ,insurerInteligentNoteId);
                insurerIntelligentNoteService.createInsurerIntelligentNote(model.getIntelligentNoteId(), insurerId, model.isStatus());
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

    public int getInsurerInteligentNoteId() {
        return insurerInteligentNoteId;
    }

    public void setInsurerInteligentNoteId(int insurerInteligentNoteId) {
        this.insurerInteligentNoteId = insurerInteligentNoteId;
    }
    
    public void setIntelligentNoteDisplayEngine(
            IntelligentNoteDisplayEngine intelligentNoteDisplayEngine) {
        this.intelligentNoteDisplayEngine = intelligentNoteDisplayEngine;
    }

}
