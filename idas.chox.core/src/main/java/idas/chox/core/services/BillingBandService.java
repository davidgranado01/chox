package idas.chox.core.services;

import java.util.List;

import idas.chox.core.model.ChoBillingBand;
import idas.chox.core.model.InsurerBillingBand;

/**
 *
 * @author john
 */
public interface  BillingBandService {
    List <InsurerBillingBand> getInsurerBillingBands();
    List <InsurerBillingBand> getInsurerBillingBands(int insurerId);
    List <ChoBillingBand> getChoBillingBands();
    List <ChoBillingBand> getChoBillingBands(int choId);
    InsurerBillingBand getInsurerBillingBand(int id);
    ChoBillingBand getChoBillingBand(int id);
    void deleteBillingBand(InsurerBillingBand band);
    void deleteBillingBand(ChoBillingBand band);
    void saveBillingBand(InsurerBillingBand band);
    void saveBillingBand(ChoBillingBand band);
}
