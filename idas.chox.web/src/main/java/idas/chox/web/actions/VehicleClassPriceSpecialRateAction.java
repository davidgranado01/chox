package idas.chox.web.actions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import idas.chox.core.model.*;
import idas.chox.core.search.SearchResult;
import idas.chox.core.services.*;
import idas.chox.web.viewdata.VehicleClassPriceSpecialRateViewData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class VehicleClassPriceSpecialRateAction extends BaseAction {

    private static final Logger LOG = LoggerFactory.getLogger(VehicleClassDropDownAction.class);
    public static final int CHOX_ADMIN_INT = 1;
    protected VehicleClassPriceSpecialRateService vehicleClassPriceSpecialRateService;
    private InsurerService insurerService;
    private ChorganisationService chorganisationService;
    private VehicleClassService vehicleClassService;
    private UserService userService;
    protected List<VehicleClassPriceSpecialRate> vehicleClassPriceSpecialRates;

    private int start;
    private int limit;
    private int totalCount;
    private int id;
    private String sort;
    private String dir;
    private String csvContent;


    public void setVehicleClassPriceSpecialRates(List<VehicleClassPriceSpecialRate> vehicleClassPriceSpecialRates) {
        this.vehicleClassPriceSpecialRates = vehicleClassPriceSpecialRates;
    }

    public void setVehicleClassPriceSpecialRateService(VehicleClassPriceSpecialRateService vehicleClassPriceSpecialRateService) {
        this.vehicleClassPriceSpecialRateService = vehicleClassPriceSpecialRateService;
    }

    public InsurerService getInsurerService() {
        return insurerService;
    }

    public void setInsurerService(InsurerService insurerService) {
        this.insurerService = insurerService;
    }

    public ChorganisationService getChorganisationService() {
        return chorganisationService;
    }

    public void setChorganisationService(ChorganisationService chorganisationService) {
        this.chorganisationService = chorganisationService;
    }

    public VehicleClassService getVehicleClassService() {
        return vehicleClassService;
    }

    public void setVehicleClassService(VehicleClassService vehicleClassService) {
        this.vehicleClassService = vehicleClassService;
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public String getVehicleClassSpecialRates() {
        try {
            SearchResult searchResult = vehicleClassPriceSpecialRateService.getVehicleClassPriceSpecialRatesPagination(start, limit, sort, dir);
            this.vehicleClassPriceSpecialRates = searchResult.getResult();
            this.totalCount = searchResult.getTotalCount();
            return SUCCESS;
        } catch (Exception ex) {
            LOG.error("Exception creating jsonArray: {}", ex.getMessage());
            ex.printStackTrace();
            return SUCCESS;
        }
    }

    public String deleteVehicleClassSpecialRate() {
        try {
            vehicleClassPriceSpecialRateService.deleteVehicleClassPriceSpecialRate(id);
            return SUCCESS;
        } catch (Exception ex) {
            LOG.error("Exception creating jsonArray: {}", ex.getMessage());
            ex.printStackTrace();
            return SUCCESS;
        }
    }

    public String uploadSupplierRates() {
        try {
            if (StringUtils.isEmpty(csvContent)) {
                return ERROR;
            }

            String[] supplierRateStrings = csvContent.split(";");
            List<VehicleClassPriceSpecialRate> supplierRates = Arrays.stream(supplierRateStrings)
                    .map(this::parseSupplierRate).distinct().filter(Objects::nonNull).collect(Collectors.toList());
            vehicleClassPriceSpecialRateService.saveSupplierRates(supplierRates);
            return SUCCESS;
        } catch (Exception ex) {
            LOG.error("Exception creating jsonArray: {}", ex.getMessage());
            return ERROR;
        }
    }

    private Map<String, Insurer> insurers = new HashMap<>();
    private Insurer getInsurerByName(String insurerName) {
        if (insurers.containsKey(insurerName)) {
            return insurers.get(insurerName);
        }

        Insurer insurer = insurerService.getInsurerByName(insurerName);
        if (null != insurer) {
            insurers.put(insurerName, insurer);
            return insurer;
        }

        return null;
    }

    private Map<String, Chorganisation> chorganisations = new HashMap<>();
    private Chorganisation getChorganisationByName(String choName) {
        if (chorganisations.containsKey(choName)) {
            return chorganisations.get(choName);
        }

        Chorganisation chorganisation = chorganisationService.getChorgByName(choName);
        if (null != chorganisation) {
            chorganisations.put(choName, chorganisation);
            return chorganisation;
        }

        return null;
    }

    private static WebUser choxSystemUser;
    private WebUser getChoxSystemUser() {
        if (choxSystemUser == null) {
            choxSystemUser = userService.findByEmail("system@chox.com");
        }

        return choxSystemUser;
    }

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private final Date now = new Date();

    private VehicleClassPriceSpecialRate parseSupplierRate(String rateString) {
        VehicleClassPriceSpecialRate supplierRate = new VehicleClassPriceSpecialRate();
        String[] rate = rateString.split(",");
        if (rate.length != 5) {
            return null;
        }

        try {
            Date startDate = dateFormat.parse(rate[4]);
            supplierRate.setStartDate(startDate);
        } catch (ParseException e) {
            return null;
        }
        try {
            BigDecimal price = new BigDecimal(rate[3]);
            supplierRate.setPrice(price);
        } catch (NumberFormatException ex) {
            return null;
        }
        Insurer insurer = getInsurerByName(rate[0]);
        if (null == insurer) {
            return null;
        }
        Chorganisation chorganisation = getChorganisationByName(rate[1]);
        if (null == chorganisation) {
            return null;
        }
        VehicleClass vehicleClass = vehicleClassService.getVehicleClassByName(rate[2]);
        if (null == vehicleClass) {
            return null;
        }

        supplierRate.setInsurer(insurer);
        supplierRate.setChorganisation(chorganisation);
        supplierRate.setVehicleClass(vehicleClass);
        supplierRate.setVersion(0);
        supplierRate.setCreatedBy(getChoxSystemUser());
        supplierRate.setLastModifiedBy(getChoxSystemUser());
        supplierRate.setCreatedDate(now);
        supplierRate.setLastModifiedDate(now);
        supplierRate.setAge(BigDecimal.valueOf(99));

        return supplierRate;
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
}
