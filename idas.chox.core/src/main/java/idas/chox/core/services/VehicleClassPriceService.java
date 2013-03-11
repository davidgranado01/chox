package idas.chox.core.services;

import java.math.BigDecimal;
import java.util.Date;

import idas.chox.core.model.VehicleClass;
import idas.chox.core.model.ClaimType;

/**
 *
 * @author John
 */
public interface VehicleClassPriceService {
   BigDecimal getPrice(ClaimType type, VehicleClass vehicleClass, Date startDate, int insId, int choId) throws Exception;
   BigDecimal getPrice(ClaimType type, VehicleClass vehicleClass, Date startDate, BigDecimal age ,  int insId, int choId) throws Exception;
}
