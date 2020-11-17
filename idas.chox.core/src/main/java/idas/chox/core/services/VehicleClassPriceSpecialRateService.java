package idas.chox.core.services;

import idas.chox.core.model.VehicleClass;
import idas.chox.core.search.SearchResult;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author seeni
 */
public interface VehicleClassPriceSpecialRateService {

   BigDecimal getPrice(VehicleClass vehicleClass, Date startDate, BigDecimal age, int insId, int choId) throws Exception;
   BigDecimal getPrice(VehicleClass vehicleClass, Date startDate, int insId, int choId) throws Exception;
   SearchResult getVehicleClassPriceSpecialRatesPagination(int start, int limit, String sort, String dir);
   void deleteVehicleClassPriceSpecialRate(int id) throws Exception;
}

