package idas.chox.data.services;

import idas.chox.core.model.Invoice;
import idas.chox.core.services.InvoiceService;
import idas.chox.core.xmlValidation.ClaimResult;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public class InvoiceServiceImpl extends SecureDataService implements InvoiceService {

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    @Override
    public void saveInvoiceForXMLUploader(final ClaimResult claimResult) {

        if ((claimResult.getClaim().getInvoice()) != null) {
            
            claimResult.getClaim().updateLiabilityPayment();
            
            
            getHibernateTemplate().saveOrUpdate((claimResult.getClaim().getInvoice()));
        }
    }

    @Override
    public Invoice getInvoice(int id) {
        return (Invoice) get(Invoice.class, id);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    @Override
    public void saveInvoice(Invoice invoice) {

        save(invoice);
    }

/*
    public void updateLiabilityPayment(Claim claim){
        LiabilityStatus l = claim.getLiabilityStatus();
        if ( l != null && l.equals(LiabilityStatus.LIABILITY_SPLIT) ){
            BigDecimal ttp = claim.getInvoice().getFullTotalToPay();
            BigDecimal insper = claim.getPercentageLiabilityAccepted();
            claim.getInvoice().setTotalToPaySplitLiability(ttp.multiply(insper).divide(new BigDecimal(100)).setScale(2,BigDecimal.ROUND_HALF_UP));
        }
    }
 * 
 */
}
