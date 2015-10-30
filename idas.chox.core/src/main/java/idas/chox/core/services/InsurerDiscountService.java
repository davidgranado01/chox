package idas.chox.core.services;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimType;
import idas.chox.core.model.InsurerDiscount;
import idas.chox.core.model.WebUser;

/**
 *
 * @author seeni
 */
public interface InsurerDiscountService {

    Map addOrUpdateDiscount(int insId, int choId, InsurerDiscount insurerDiscount);

    List<InsurerDiscount> getInsurerDiscount(int choId, int insId, ClaimType claimType);
    List<InsurerDiscount> getInsurerDiscount(int choId, int insId);

    Map deleteInsurerDiscount(InsurerDiscount insurerDiscount);

    InsurerDiscount getInsurerDiscount(int insurerDiscountId);

    BigDecimal getDiscountPercentage(int insId, int choId, Date invoiceCreatedDate, int insurerDiscountTypeValue, int claimTypeValue);

    void applyInsurerDiscounts(Claim claim, WebUser user, boolean canAddComment);

    void addInsurerDiscountComment(Claim claim, BigDecimal insurerDiscountAmount, BigDecimal insurerDiscountPercentage, String insurerDiscountType, WebUser user);
}
