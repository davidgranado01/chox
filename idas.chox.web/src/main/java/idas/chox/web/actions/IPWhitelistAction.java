package idas.chox.web.actions;

import java.util.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.annotation.Secured;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

import idas.chox.core.model.IPWhitelist;
import idas.chox.core.services.ChorganisationService;
import idas.chox.core.services.IPWhitelistService;
import idas.chox.core.services.InsurerService;
import idas.chox.web.viewdata.IPWhitelistViewData;

public class IPWhitelistAction extends BaseAction implements ModelDriven<IPWhitelist>, Preparable {
    private static final Logger LOG = LoggerFactory.getLogger(IPWhitelistAction.class);
    private List<IPWhitelistViewData> ipWhitelistViewData;
    private int id;
    private int orgId;
    private int orgType; // 2 for insurer and 3 for cho
    private int choId;
    private int insId;
    private IPWhitelist model;
    private IPWhitelistService ipWhitelistService;
    private ChorganisationService chorganisationService;
    private InsurerService insurerService;
    private String jsonData;
    private Map result = new HashMap<String, Object>(){{put("success", Boolean.TRUE);}};

    public String getJsonData() {
        return jsonData;
    }

    public void setJsonData(String jsonData) {
        this.jsonData = jsonData;
    }

    public void setInsurerService(InsurerService insurerService) {
        this.insurerService = insurerService;
    }

    public int getOrgType() {
        return orgType;
    }

    public void setOrgType(int orgType) {
        this.orgType = orgType;
    }

    public void setChorganisationService(ChorganisationService chorganisationService) {
        this.chorganisationService = chorganisationService;
    }

    public void setIpWhitelistService(IPWhitelistService ipWhitelistService) {
        this.ipWhitelistService = ipWhitelistService;
    }

    public int getChoId() {
        return choId;
    }

    public void setChoId(int choId) {
        this.choId = choId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getInsId() {
        return insId;
    }

    public void setInsId(int insId) {
        this.insId = insId;
    }

    public int getOrgId() {
        return orgId;
    }

    public void setOrgId(int orgId) {
        this.orgId = orgId;
    }

    @Override
    public IPWhitelist getModel() {
        return model;
    }

    public void setModel(IPWhitelist model) {
        this.model = model;
    }

    @Override
    public String execute() {
//        updateModelInSession(Arrays.asList(model));
        return SUCCESS;
    }

    @Override
    @Secured({"ROLE_CHOX_ADMIN"}) // having aop security check in prepare method ensures only chox admin can access this action class.
    public void prepare() {
        try {
            model = new IPWhitelist();
            if (this.id > 0) {
                model = ipWhitelistService.getIPWhitelistById(id);
//                addModelToSession(Arrays.asList(model));
            }

        } catch (Exception ex) {
            handleException(ex);
        }
    }

    public String getIPWhitelists() {

        try {
            ipWhitelistViewData = new ArrayList<>();

            if (orgId > 0) {
                List<IPWhitelist> ipWhitelists = new ArrayList<>();
                if (orgType == 2) { // insurer
                    ipWhitelists = ipWhitelistService.getIPWhitelistsByOrgId(orgId, false, true);
                } else if (orgType == 3) { // cho
                    ipWhitelists = ipWhitelistService.getIPWhitelistsByOrgId(orgId, true, false);
                }
                for (IPWhitelist iPWhitelist : ipWhitelists) {
                    ipWhitelistViewData.add(new IPWhitelistViewData(iPWhitelist));
                }
                ObjectMapper mapper = new ObjectMapper();
                String jsonString = null;
                try {
                    jsonString = mapper.writeValueAsString(ipWhitelistViewData);
                } catch (JsonProcessingException ex) {
                    LOG.error("Error converting IPWhitelist to json string.");
                }
                setJsonData("{totalCount:" + this.ipWhitelistViewData.size() + ",results:" + jsonString + "}");
            }
        } catch (Exception ex) {
            LOG.error("Exception thrown when getting whitelist", ex);
            result.put("success", Boolean.FALSE);
            result.put("error", "Unexpected error occurred, Please contact Chox support.");
            setJsonData("{\"success\":\"False\",\"error\":\"Unexpected error occurred, Please contact Chox support.\"}");
        }
        return SUCCESS;
    }

    public String addIPWhitelist() {
        try {
            if (isNewModelValide() && choId > 0) {
                model.setChorganisation(chorganisationService.getChorganisation(choId));
                ipWhitelistService.saveIPWhitelist(model);
            } else if (isNewModelValide() && insId > 0) {
                model.setInsurer(insurerService.getInsurer(insId));
                ipWhitelistService.saveIPWhitelist(model);
            } else {
                result.put("success", Boolean.FALSE);
                result.put("error", "Invalid form values are submited. Please try again.");
            }

        } catch (Exception ex) {
            LOG.error("Exception thrown when adding whitelist", ex);
            result.put("success", Boolean.FALSE);
            result.put("error", "Unexpected error occurred, Please contact Chox support.");
        }
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = null;
        try {
            jsonString = mapper.writeValueAsString(result);
        } catch (JsonProcessingException ex) {
            LOG.error("Error converting IPWhitelist list to json string.");
        }
        setJsonData(jsonString);
        return SUCCESS;
    }

    public String updateIPWhitelist() {
        try {
            if (model == null || model.getId() == null) {
                result.put("success", Boolean.FALSE);
                result.put("error", "Record was updated by another transaction/user, please try again.");
            } else {
                if (isNewModelValide()) {
                    ipWhitelistService.saveIPWhitelist(model);
                } else {
                    result.put("success", Boolean.FALSE);
                    result.put("error", "Invalid form values are submited. Please try again.");
                }
            }
        } catch (Exception ex) {
            LOG.error("Exception thrown when updating whitelist", ex);
            result.put("success", Boolean.FALSE);
            result.put("error", "Unexpected error occurred, Please contact Chox support.");
        }
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = null;
        try {
            jsonString = mapper.writeValueAsString(result);
        } catch (JsonProcessingException ex) {
            LOG.error("Error converting IPWhitelist list to json string.");
        }
        setJsonData(jsonString);
        return SUCCESS;
    }

    public String deleteIPWhitelist() {
        try {
            if (model == null || model.getId() == null) {
                result.put("success", Boolean.FALSE);
                result.put("error", "Record was updated by another transaction/user, please try again.");
            } else {
                ipWhitelistService.deleteIPWhitelist(model);
            }

        } catch (Exception ex) {
            LOG.error("Exception thrown when deleting whitelist", ex);
            result.put("success", Boolean.FALSE);
            result.put("error", "Unexpected error occurred, Please contact Chox support.");
        }
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = null;
        try {
            jsonString = mapper.writeValueAsString(result);
        } catch (JsonProcessingException ex) {
            LOG.error("Error converting IPWhitelist list to json string.");
        }
        setJsonData(jsonString);
        return SUCCESS;
    }
    
    private boolean isNewModelValide() {
        return model.getIpAddress() != null && !"".equals(model.getIpAddress().trim()) 
                && model.getDescription() != null && !"".equals(model.getDescription().trim());
    }
}
