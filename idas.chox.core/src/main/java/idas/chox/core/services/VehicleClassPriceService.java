package idas.chox.core.services;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import idas.chox.core.model.VehicleClass;
import idas.chox.core.model.ClaimType;
import idas.chox.core.model.VehicleClassPrice;
import idas.chox.core.search.SearchResult;

/**
 * @author John
 */
public interface VehicleClassPriceService {
   BigDecimal getPrice(ClaimType type, VehicleClass vehicleClass, Date startDate, int insId, int choId) throws Exception;

   BigDecimal getPrice(ClaimType type, VehicleClass vehicleClass, Date startDate, BigDecimal age, int insId, int choId) throws Exception;

   SearchResult getVehicleClassPriceRatesPagination(int start, int limit, String sort, String dir);

   void deleteVehicleClassPriceRate(int id) throws Exception;

   void saveGTARates(List<VehicleClassPrice> gtaRates);
}
