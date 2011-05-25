/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package idas.chox.core.services;


import idas.chox.core.model.Claim;
import java.math.BigDecimal;
import java.util.Date;
import idas.chox.core.model.VehicleClass;

/**
 *
 * @author seeni
 */
public interface VehicleClassPriceSpecialRateService {

   public BigDecimal getPrice(VehicleClass vehicleClass, Date startDate, BigDecimal age, int insId, int choId);
   public BigDecimal getPrice(VehicleClass vehicleClass, Date startDate, int insId, int choId);
}

