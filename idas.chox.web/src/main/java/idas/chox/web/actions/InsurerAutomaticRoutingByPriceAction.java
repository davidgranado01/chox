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

/**
 *
 * @author rajareddydodda
 */
public class InsurerAutomaticRoutingByPriceAction extends BaseAction implements ModelDriven<AutomaticRoutingPrice>, Preparable {

    static final Logger LOG = LoggerFactory.getLogger(InsurerAutomaticRoutingByPriceAction.class);
    private AutomaticRoutingPrice model;
    private String objectId;
    private AdminInsurerService adminInsurerService;

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

    public void prepare() throws Exception {

        LOG.debug("InsurerAutomaticRouting based on price ..... Prepare");

        try {

            model = new AutomaticRoutingPrice();

            if (this.objectId != null && !this.objectId.equalsIgnoreCase("")) {
                if (Integer.valueOf(objectId) > 0) {
                    model = adminInsurerService.getInsurerAutomaticRoutingByPrice(Integer.valueOf(this.objectId));

                }
            }

        } catch (Exception ex) {
            handleException(ex);
        }

    }
}
