/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.web.actions;

import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.model.AutomaticRoutingPrice;
import idas.chox.service.admin.AdminInsurerService;
import idas.chox.core.model.IdLookupItem;
import idas.chox.web.viewdata.InsurerAutomaticRoutingByPriceViewData;
import java.util.ArrayList;
import java.util.List;
import net.sf.json.JSONArray;

/**
 *
 * @author rajareddydodda
 */
public class InsurerAutomaticRoutingByPriceAction extends BaseAction implements ModelDriven<AutomaticRoutingPrice>, Preparable {

    static final Logger LOG = LoggerFactory.getLogger(InsurerAutomaticRoutingByPriceAction.class);
    private AutomaticRoutingPrice model;
    private String objectId;
    private int insurerId = -1;
    private AdminInsurerService adminInsurerService;
    private List<InsurerAutomaticRoutingByPriceViewData> insurerAutomaticRoutingsByPrice = new ArrayList<InsurerAutomaticRoutingByPriceViewData>();

    /**
     * @return the model
     */
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
        JSONArray jObject = JSONArray.fromObject(this.insurerAutomaticRoutingsByPrice);
        return "{totalCount:" + this.insurerAutomaticRoutingsByPrice.size() + ",results:" + jObject.toString() + "}";
    }



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

        List items = new ArrayList<IdLookupItem>();
        try {
            items = adminInsurerService.getAvailableWorkgroups(this.insurerId);
        } catch (Exception ex) {
            handleException(ex);
        }

        return items;
    }

    public String getInsurerAutomaticRoutingByPrice() {

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

    public int getInsurerId() {
        return insurerId;
    }

    public void setInsurerId(int insurerId) {
        this.insurerId = insurerId;
    }
}
