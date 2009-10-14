/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.web.actions;

import chox.model.ChoBandOrganisation;
import chox.services.ChoBandOrganisationService;
import chox.services.ChoBandService;
import chox.services.ChorganisationService;


public class doInsurerChoBandMappingAction extends BaseAction {
    
    private Integer objectId = -1;
    private Integer chorganisationId = -1;
    private Integer chobandId = -1;
    private String actionResult;
    private ChoBandOrganisationService service;
    private ChoBandService choBandService;
    private ChorganisationService chorganisationService;


    public void setChoBandOrganisationService(ChoBandOrganisationService service) { this.service = service; }
    public void setChoBandService(ChoBandService choBandService) { this.choBandService = choBandService; }
    public void setChorganisationService(ChorganisationService chorganisationService) { this.chorganisationService = chorganisationService; }
    
    public String getActionResult() {
        return actionResult;
    }

    public void setActionResult(String actionResult) {
        this.actionResult = actionResult;
    }

    public Integer getChobandId() {
        return chobandId;
    }

    public void setChobandId(Integer chobandId) {
        this.chobandId = chobandId;
    }

    public Integer getChorganisationId() {
        return chorganisationId;
    }

    public void setChorganisationId(Integer chorganisationId) {
        this.chorganisationId = chorganisationId;
    }

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }
    
    public String removeObject(){
         
        try{       
        
            ChoBandOrganisation object = service.getObject(objectId);
            service.deleteObject(object);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        return SUCCESS;
    }
    
    public String addObject(){
        
        String sActionMsg = "";
        boolean bActionFlag = false;
         
        try{  
            
            ChoBandOrganisation object = new ChoBandOrganisation();
            object.setChoBand(choBandService.getObject(chobandId));
            object.setChorganisation(chorganisationService.getObject(chorganisationId));
            service.updateObject(object);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return SUCCESS;
    }
    
}
