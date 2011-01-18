package idas.chox.core.services;

import java.math.BigDecimal;
import java.util.Date;
import idas.chox.core.model.VehicleClass;
import idas.chox.core.model.VehicleClassPrice;
import java.util.List;

/**
 *
 * @author John
 */
public interface VehicleClassPriceService {
   public BigDecimal getPrice(VehicleClass vehicleClass, Date startDate);
   public List<VehicleClassPrice> getPrices(VehicleClass vehicleClass, Date startDate);
   public BigDecimal getPrice(VehicleClass vehicleClass, Date startDate, BigDecimal age);
}
