package idas.chox.core.services;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import idas.chox.core.model.Claim;
import idas.chox.core.model.InsurerDiscount;
import idas.chox.core.model.WebUser;

/**
 *
 * @author seeni
 */
public interface InsurerDiscountService {

    public Map addOrUpdateDiscount(int insId, int choId, InsurerDiscount insurerDiscount);

    public List<InsurerDiscount> getInsurerDiscount(int choId, int InsId);

    public Map deleteInsurerDiscount(InsurerDiscount insurerDiscount);

    public InsurerDiscount getInsurerDiscount(int insurerDiscountId);

    public BigDecimal getDiscountPercentage(int insId, int choId, Date invoiceCreatedDate, int insurerDiscountTypeValue);

    public void applyInsurerDiscounts(Claim claim, WebUser user, boolean canAddComment);

    public void addInsurerDiscountComment(Claim claim, BigDecimal insurerDiscountAmount, BigDecimal insurerDiscountPercentage, String insurerDiscountType, WebUser user);
}
