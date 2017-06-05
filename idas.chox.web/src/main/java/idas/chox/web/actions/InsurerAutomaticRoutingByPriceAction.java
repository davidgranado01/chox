package idas.chox.web.actions;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.annotation.Secured;

import idas.chox.core.model.AutomaticRoutingPrice;
import idas.chox.service.admin.AdminInsurerService;
import idas.chox.web.viewdata.InsurerAutomaticRoutingByPriceViewData;
import idas.chox.service.ActionResponse;

/**
 *
 * @author rajareddydodda
 */
public class InsurerAutomaticRoutingByPriceAction extends BaseAction implements ModelDriven<AutomaticRoutingPrice>, Preparable {

    static final Logger LOG = LoggerFactory.getLogger(InsurerAutomaticRoutingByPriceAction.class);
    private AutomaticRoutingPrice model;
    private String objectId;
    private int insurerId = -1;
    private int automaticRoutingId = -1;
    private int workgroupId = -1;
    private AdminInsurerService adminInsurerService;
    private List<InsurerAutomaticRoutingByPriceViewData> insurerAutomaticRoutingsByPrice = new ArrayList<>();

    /**
     * @return the model
     */
    @Override
    public AutomaticRoutingPrice getModel() {
        return model;
    }

    /**
     * @param model the model to set
     */
    public void setModel(AutomaticRoutingPrice model) {
        this.model = model;
    }


    public String getJsonData() {
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = null;
        try {
            jsonString = mapper.writeValueAsString(insurerAutomaticRoutingsByPrice);
        } catch (JsonProcessingException ex) {
            LOG.error("Error converting insurerAutomaticRoutingsByPrice to json string.");
        }
        return "{totalCount:" + this.insurerAutomaticRoutingsByPrice.size() + ",results:" + jsonString + "}";
    }



    @Override
    public void prepare() throws Exception {

        LOG.debug("InsurerAutomaticRouting based on price ..... Prepare");

        try {

            model = new AutomaticRoutingPrice();

            LOG.debug("this.objectId : {}",this.objectId);
            if (this.objectId != null && !this.objectId.equalsIgnoreCase("")) {
                if (Integer.valueOf(objectId) > 0) {
                    model = adminInsurerService.getInsurerAutomaticRoutingByPrice(Integer.valueOf(this.objectId));

                }
            }

        } catch (Exception ex) {
            handleException(ex);
        }

    }

    public List getAvailableWorkgroups() {
        if (getUserOrganisationType() == 3 || (getUserOrganisationType() == 2 && this.insurerId != getUserOrganisationId())) {
            throw new AccessDeniedException("Illegal access detected.");
        }

        List items = new ArrayList<>();
        try {
            items = adminInsurerService.getAvailableWorkgroups(this.insurerId, true);
        } catch (Exception ex) {
            handleException(ex);
        }

        return items;
    }

    public String getInsurerAutomaticRoutingByPrice() {
        if (getUserOrganisationType() == 3 || (getUserOrganisationType() == 2 && this.insurerId != getUserOrganisationId())) {
            throw new AccessDeniedException("Illegal access detected.");
        }

        LOG.debug("InsurerAutomaticRouting .....");
        
        LOG.debug("this.insurerId : {}",this.insurerId);



        try {

            List<AutomaticRoutingPrice> automaticRoutingDataByPrice = adminInsurerService.getInsurerAutomaticRoutingsByPrice(this.insurerId);

            LOG.debug("automaticRoutingDataByPrice size : {}",automaticRoutingDataByPrice.size());

            for (AutomaticRoutingPrice h : automaticRoutingDataByPrice) {
                insurerAutomaticRoutingsByPrice.add(new InsurerAutomaticRoutingByPriceViewData(h));
            }

        } catch (Exception ex) {
            handleException(ex);
            return ERROR;
        }
        return SUCCESS;
    }

    @Override
    public String execute() {
        return SUCCESS;
    }

    @Secured ({"ROLE_CHOX_ADMIN"})
    public String deleteAutomaticRoutingDetailByPrice() {

        LOG.debug("deleteAutomaticRoutingDetailByPrice and id is : {}",this.getAutomaticRoutingId());
        ActionResponse response;
        response = adminInsurerService.deleteAutomaticRoutingByPrice(this.getAutomaticRoutingId());
        setActionResponse(response);
        return SUCCESS;
    }

    @Secured ({"ROLE_CHOX_ADMIN"})
    public String addNewAutomaticRoutingDetailByPrice() {
        ActionResponse response;
        response = adminInsurerService.addNewAutomaticRoutingByPrice(this.insurerId, this.getWorkgroupId(), model.getPrice());
        setActionResponse(response);
        return SUCCESS;
    }



    public int getInsurerId() {
        return insurerId;
    }

    public void setInsurerId(int insurerId) {
        this.insurerId = insurerId;
    }
    public void setAdminInsurerService(AdminInsurerService adminInsurerService) {
        this.adminInsurerService = adminInsurerService;
    }

    /**
     * @return the automaticRoutingId
     */
    public int getAutomaticRoutingId() {
        return automaticRoutingId;
    }

    /**
     * @param automaticRoutingId the automaticRoutingId to set
     */
    public void setAutomaticRoutingId(int automaticRoutingId) {
        this.automaticRoutingId = automaticRoutingId;
    }

    /**
     * @return the workgroupId
     */
    public int getWorkgroupId() {
        return workgroupId;
    }

    /**
     * @param workgroupId the workgroupId to set
     */
    public void setWorkgroupId(int workgroupId) {
        this.workgroupId = workgroupId;
    }

}
