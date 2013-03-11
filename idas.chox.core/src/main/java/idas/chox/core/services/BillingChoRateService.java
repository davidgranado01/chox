package idas.chox.core.services;

import java.math.BigDecimal;
import java.util.List;

import idas.chox.core.model.BillingChoRate;

/**
 *
 * @author abrar
 */
public interface BillingChoRateService {
    List<BillingChoRate> getBillingChoRates();
    List<BillingChoRate> getBillingChoRates(int cho_organisation_id);
    BigDecimal getRateForCho(int cho_organisation_id, int volume);
    BigDecimal getRate(int volume);
    BillingChoRate getObject(int id);
}
