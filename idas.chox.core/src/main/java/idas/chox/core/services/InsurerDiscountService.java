/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.core.services;

import idas.chox.core.model.InsurerDiscount;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * @author seeni
 */
public interface InsurerDiscountService {
    
    public Map addOrUpdateDiscount(int insId, int choId, Date dateFrom, Date dateTo, BigDecimal discountAmount, int discountId);
    
    public List<InsurerDiscount> getInsurerDiscount(int choId, int InsId);
    
    public Map deleteInsurerDiscount(InsurerDiscount insurerDiscount);
    
    public InsurerDiscount getInsurerDiscount(int insurerDiscountId);
    
    public BigDecimal getDiscountAmount(int insId, int choId, Date invoiceCreatedDate);
    
    
}
