package idas.chox.web.actions;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import idas.chox.core.model.*;
import idas.chox.core.search.SearchResult;
import idas.chox.core.services.UserService;
import idas.chox.core.services.VehicleClassPriceService;
import idas.chox.core.services.VehicleClassService;
import idas.chox.web.viewdata.VehicleClassPriceSpecialRateViewData;
import idas.chox.web.viewdata.VehicleClassPriceViewData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Stream;

public class VehicleClassPriceAction extends BaseAction {

    private static final Logger LOG = LoggerFactory.getLogger(VehicleClassPriceAction.class);
    public static final int CHOX_ADMIN_INT = 1;


    protected VehicleClassPriceService vehicleClassPriceService;


    private VehicleClassService vehicleClassService;
    protected List<VehicleClassPrice> vehicleClassPriceRates;
    private UserService userService;

    private int start;
    private int limit;
    private int totalCount;
    private int id;
    private String sort;
    private String dir;
    private String csvContent;
    private String actionResponseString;

    public void setVehicleClassPriceService(VehicleClassPriceService vehicleClassPriceService) {
        this.vehicleClassPriceService = vehicleClassPriceService;
    }

    public void setVehicleClassService(VehicleClassService vehicleClassService) {
        this.vehicleClassService = vehicleClassService;
    }

    public void setVehicleClassPriceRates(List<VehicleClassPrice> vehicleClassPriceRates) {
        this.vehicleClassPriceRates = vehicleClassPriceRates;
    }


    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }


    public String getVehiclePriceRates() {
        try {
            SearchResult searchResult = vehicleClassPriceService.getVehicleClassPriceRatesPagination(start, limit, sort, dir);
            this.vehicleClassPriceRates = searchResult.getResult();
            this.totalCount = searchResult.getTotalCount();
            return SUCCESS;
        } catch (Exception ex) {
            LOG.error("Exception creating jsonArray: {}", ex.getMessage());
            ex.printStackTrace();
            return SUCCESS;
        }
    }


    public String deleteVehiclePriceRate() {
        try {
            vehicleClassPriceService.deleteVehicleClassPriceRate(id);
            return SUCCESS;
        } catch (Exception ex) {
            LOG.error("Exception creating jsonArray: {}", ex.getMessage());
            ex.printStackTrace();
            return SUCCESS;
        }
    }

    public String uploadGTARates() {
        try {
            if (StringUtils.isEmpty(csvContent)) {
                return ERROR;
            }

            String[] gtaRateStrings = csvContent.split(";");
            List<String> failedRows = new ArrayList<>();
            Set<VehicleClassPrice> gtaRates = new HashSet<>();
            for (String gtaRateString : gtaRateStrings) {
                VehicleClassPrice gtaRate = parseGtaRate(gtaRateString);
                if (null == gtaRate) {
                    failedRows.add(gtaRateString.substring(gtaRateString.lastIndexOf(",")));
                } else {
                    if (!gtaRates.stream().anyMatch(rate -> (rate.getVehicleClass().getName().toLowerCase(Locale.ROOT).equals(gtaRate.getVehicleClass().getName().toLowerCase(Locale.ROOT)) && rate.getStartDate().equals(gtaRate.getStartDate()) && rate.getAge().equals(gtaRate.getAge()))))
                        gtaRates.add(gtaRate);
                    else
                        failedRows.add(gtaRateString.substring(gtaRateString.lastIndexOf(",")));
                }
            }
            List<VehicleClassPrice> gtaRatesList = new ArrayList<>(gtaRates);
            vehicleClassPriceService.saveGTARates(gtaRatesList);
            actionResponseString = "Successfully uploaded " + gtaRatesList.size() + " rates of " + gtaRateStrings.length + ":" + String.join(",", failedRows);
            return SUCCESS;
        } catch (Exception ex) {
            actionResponseString = "Failed to upload the gta rates";
            ex.printStackTrace();
            LOG.error("Exception creating jsonArray: {} ", ex.getMessage());
            return ERROR;
        }
    }

    private final Date now = new Date();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    private VehicleClassPrice parseGtaRate(String rateString) {
        VehicleClassPrice gtaRate = new VehicleClassPrice();
        long age =0;
        String[] rate = rateString.split(",");
        if (rate.length < 4) {
            return null;
        }
        try{
            age = Integer.parseInt(rate[3]);
        }catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }


        try {
            Date startDate = dateFormat.parse(rate[2]);
            gtaRate.setStartDate(startDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
        try {
            String rawprice = new String(rate[1]);
            //here their is a chance that they can upload with currency symbol
            String[] priceString = rawprice.split("£");
            String price = priceString.length == 2 ? priceString[1] : priceString[0];
            BigDecimal newPrice = BigDecimal.valueOf(Double.valueOf(price));
            gtaRate.setPrice(newPrice);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

        VehicleClass vehicleClass = vehicleClassService.getVehicleClassByName(rate[0]);
        if (vehicleClass == null) {
            return null;
        }


        gtaRate.setVehicleClass(vehicleClass);
        gtaRate.setAge(BigDecimal.valueOf(age));
        gtaRate.setCreatedBy(getChoxSystemUser());
        gtaRate.setCreatedDate(now);
        gtaRate.setVersion(0);
        gtaRate.setLastModifiedBy(getChoxSystemUser());
        gtaRate.setLastModifiedDate(now);

        VehicleClassPrice vehicleClassPrice = vehicleClassPriceService.isDataWithSameOrLessThanStartDatePresentOrNot(gtaRate,vehicleClass);

        if(vehicleClassPrice!=null) {
            return null;
        }

        return gtaRate;
    }

    public String getJsonData() {
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = null;

        try {
            LOG.debug("Converting results to view data");
            List<VehicleClassPriceViewData> viewData = new ArrayList<>(vehicleClassPriceRates.size());
            for (VehicleClassPrice vehicleClassPriceRate : vehicleClassPriceRates) {

                boolean showDelete = false;

                Stream<VehicleClassPrice> filteredGtaRates = vehicleClassPriceRates.stream().filter((rate) -> rate.getVehicleClass().getName().toLowerCase(Locale.ROOT).equals(vehicleClassPriceRate.getVehicleClass().getName().toLowerCase(Locale.ROOT)));

                VehicleClassPrice vehicleClassPriceRateHigh = filteredGtaRates.max((rate1, rate2) -> rate1.getStartDate().after(rate2.getStartDate()) ? 1: 0).get();

                if (vehicleClassPriceRate.getStartDate().equals(vehicleClassPriceRateHigh.getStartDate())) {
                    showDelete = true;
                }

                //LOG.debug("Adding VehicleClassPriceSpecialRateViewData to view data: CHO={}, Insurer={}, VehicleClass={}", vehicleClassPriceSpecialRate.getChorganisation().getName(), vehicleClassPriceSpecialRate.getInsurer().getName(), vehicleClassPriceSpecialRate.getVehicleClass().getName());
                viewData.add(new VehicleClassPriceViewData(vehicleClassPriceRate, showDelete));
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

    private static WebUser choxSystemUser;

    private WebUser getChoxSystemUser() {
        if (choxSystemUser == null) {
            choxSystemUser = userService.findByEmail("system@chox.com");
        }

        return choxSystemUser;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public String getCsvContent() {
        return csvContent;
    }

    public void setCsvContent(String csvContent) {
        this.csvContent = csvContent;
    }

    @Override
    public String getActionResponseString() {
        return actionResponseString;
    }

    public void setActionResponseString(String actionResponseString) {
        this.actionResponseString = actionResponseString;
    }
}
