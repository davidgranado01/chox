package idas.chox.core.services;

import java.util.List;
import idas.chox.core.model.PaidInvoiceEntry;

/**
 *
 * @author john
 */
public interface PaidInvoiceService  extends DataService {
        List<PaidInvoiceEntry> getPaidInvoiceEntries(String insurerName);
}
