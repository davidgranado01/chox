package idas.chox.data.services;

import idas.chox.core.model.Invoice;
import idas.chox.core.services.ClaimService;
import idas.chox.core.services.InvoiceService;
import idas.chox.core.xmlValidation.ClaimResult;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

// This class is used nowhere please make sure this class is declared in 
// applicationContext-service.xml and injected required services before use.
public class InvoiceServiceImpl extends SecureDataService implements InvoiceService {
    
    private ClaimService claimService;

    public ClaimService getClaimService() {
        return claimService;
    }

    public void setClaimService(ClaimService claimService) {
        this.claimService = claimService;
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    @Override
    public void saveInvoiceForXMLUploader(final ClaimResult claimResult) {

        if ((claimResult.getClaim().getInvoice()) != null) {
            
            claimService.updateLiabilityPayment(claimResult.getClaim());
            
            
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
