package idas.chox.web.actions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import idas.chox.core.model.VehicleClassPriceSpecialRate;
import idas.chox.core.search.SearchResult;
import idas.chox.core.services.VehicleClassPriceSpecialRateService;
import idas.chox.web.viewdata.VehicleClassPriceSpecialRateViewData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class VehicleClassPriceSpecialRateAction extends BaseAction {

    private static final Logger LOG = LoggerFactory.getLogger(VehicleClassDropDownAction.class);
    public static final int CHOX_ADMIN_INT = 1;
    protected VehicleClassPriceSpecialRateService vehicleClassPriceSpecialRateService;
    protected List<VehicleClassPriceSpecialRate> vehicleClassPriceSpecialRates;

    private int start;
    private int limit;
    private int totalCount;

    public void setVehicleClassPriceSpecialRates(List<VehicleClassPriceSpecialRate> vehicleClassPriceSpecialRates) {
        this.vehicleClassPriceSpecialRates = vehicleClassPriceSpecialRates;
    }

    public void setVehicleClassPriceSpecialRateService(VehicleClassPriceSpecialRateService vehicleClassPriceSpecialRateService) {
        this.vehicleClassPriceSpecialRateService = vehicleClassPriceSpecialRateService;
    }

    public String getVehicleClassSpecialRates() {
        try {
            SearchResult searchResult = vehicleClassPriceSpecialRateService.getVehicleClassPriceSpecialRatesPagination(start, limit);
            this.vehicleClassPriceSpecialRates = searchResult.getResult();
            this.totalCount = searchResult.getTotalCount();
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
        return "{totalCount:" + totalCount + ",results:" + jsonString + "}";
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }
}
