/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package idas.chox.core.services;

import idas.chox.core.model.BillingChoRate;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author abrar
 */
public interface BillingChoRateService {
    public List<BillingChoRate> getBillingChoRates();
    public List<BillingChoRate> getBillingChoRates(int cho_organisation_id);
    public BigDecimal getRateForCho(int cho_organisation_id, int volume);
    public BigDecimal getRateForCho2(int cho_organisation_id, int volume);
    public BigDecimal getRate(int volume);
}
