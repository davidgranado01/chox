package idas.chox.web.actions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import idas.chox.core.model.VehicleClassPriceSpecialRate;
import idas.chox.core.services.VehicleClassPriceSpecialRateService;
import idas.chox.web.viewdata.VehicleClassPriceSpecialRateViewData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;

import java.util.ArrayList;
import java.util.List;

public class VehicleClassPriceSpecialRateAction extends BaseAction {

    private static final Logger LOG = LoggerFactory.getLogger(VehicleClassDropDownAction.class);
    public static final int CHOX_ADMIN_INT = 1;
    protected VehicleClassPriceSpecialRateService vehicleClassPriceSpecialRateService;
    protected List<VehicleClassPriceSpecialRate> vehicleClassPriceSpecialRates;

    public void setVehicleClassPriceSpecialRates(List<VehicleClassPriceSpecialRate> vehicleClassPriceSpecialRates) {
        this.vehicleClassPriceSpecialRates = vehicleClassPriceSpecialRates;
    }

    public void setVehicleClassPriceSpecialRateService(VehicleClassPriceSpecialRateService vehicleClassPriceSpecialRateService) {
        this.vehicleClassPriceSpecialRateService = vehicleClassPriceSpecialRateService;
    }

    @Override
    public String execute() throws Exception {
        if (getUserOrganisationType() != CHOX_ADMIN_INT) {
            throw new AccessDeniedException("Illegal access detected.");
        }

        try {
            this.vehicleClassPriceSpecialRates = vehicleClassPriceSpecialRateService.getAllVehicleClassPriceSpecialRates();
        } catch (Exception ex) {
            handleException(ex);
            return ERROR;
        }

        return SUCCESS;
    }

    public String getVehicleClassSpecialRates() {
        try {
            this.vehicleClassPriceSpecialRates = vehicleClassPriceSpecialRateService.getAllVehicleClassPriceSpecialRates();
            return SUCCESS;
        } catch (Exception ex) {
            LOG.error("Exception creating jsonArray: {}", ex.getMessage());
            return SUCCESS;
        }
    }

    public String getJsonData() {
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = null;

        try {
            LOG.debug("Converting results to view data");
            List<VehicleClassPriceSpecialRateViewData> viewData = new ArrayList<>(vehicleClassPriceSpecialRates.size());
            for (VehicleClassPriceSpecialRate vehicleClassPriceSpecialRate : vehicleClassPriceSpecialRates) {
                LOG.debug("Adding VehicleClassPriceSpecialRateViewData to view data: CHO={}, Insurer={}, VehicleClass={}", vehicleClassPriceSpecialRate.getChorganisation().getName(), vehicleClassPriceSpecialRate.getInsurer().getName(), vehicleClassPriceSpecialRate.getVehicleClass().getName());
                viewData.add(new VehicleClassPriceSpecialRateViewData(vehicleClassPriceSpecialRate));
            }
            try {
                jsonString = mapper.writeValueAsString(viewData);
            } catch (JsonProcessingException ex) {
                LOG.error("Error converting luItems to json string.");
            }
        } catch (Exception ex) {
            LOG.error("Exception creating jsonArray: {}", ex.getMessage());
            return null;
        }
        LOG.debug("Returning json data: {}", jsonString);
        return "{totalCount:" + vehicleClassPriceSpecialRates.size() + ",results:" + jsonString + "}";
    }

}
