package idas.chox.service.xml;

import idas.chox.core.model.Claim;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import idas.chox.data.services.*;
import idas.chox.core.services.UploadClaimXMLService;
import idas.chox.core.util.XMLUtils;
import idas.chox.core.workflow.Activity;
import idas.chox.service.workflow.ActivityFactory;
import idas.chox.core.xmlValidation.ClaimParseStatus;
import idas.chox.core.xmlValidation.ClaimResult;
import idas.chox.service.xml.readers.BordereauReader;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.springframework.transaction.annotation.Propagation;
import org.w3c.dom.*;
import org.springframework.transaction.annotation.Transactional;

public class UploadClaimXMLServiceImpl extends SecureDataService implements UploadClaimXMLService {

    private BordereauReader bordereauReader;
    private ActivityFactory activityFactory;
    private static final Logger LOG = LoggerFactory.getLogger(UploadClaimXMLServiceImpl.class);

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public boolean doProcessBordereauResult(ClaimResult claimResult, List<String> choReferences ) {

        try {

            bordereauReader.execute(claimResult);

        } catch (Exception ex) {
            LOG.error("Exception thrown in reading the Bordereau file");
            return false;
        }

        validate(claimResult,choReferences);
        LOG.debug("Processing claim '{}'.", claimResult.getClaim().getChoReference());
        if (claimResult.isValid() && claimResult.isDataValid()) {
            //CALL WORKFLOW LOGIC
            try {
                LOG.debug("claimResult for claim '{}' is valid.", claimResult.getClaim().getChoReference());

                if (claimResult.getClaimParseStatus().equals(ClaimParseStatus.newClaim)) {
                    LOG.debug("Processing newClaim activity.");
                    Activity activity = activityFactory.getActivity("newClaim");
                    activity.processInBatch(claimResult.getClaim());
                    LOG.debug("newClaim activity completed.");
                } else if (claimResult.getClaimParseStatus().equals(ClaimParseStatus.newInvoice)) {
                    LOG.debug("Processing newInvoice activity.");
                    claimResult.getClaim().setInvoice(claimResult.getInvoice());
                    Activity activity = activityFactory.getActivity("newInvoice");
                    activity.processInBatch(claimResult.getClaim());
                    LOG.debug("newInvoice activity completed.");
                } else if (claimResult.getClaimParseStatus().equals(ClaimParseStatus.tpiIntervention)) {
                    LOG.debug("Processing Tpi Invoice activity.");
                    claimResult.getClaim().setInvoice(claimResult.getInvoice());
                    Activity activity = activityFactory.getActivity("newInvoice");
                    activity.processInBatch(claimResult.getClaim());
                    LOG.debug("TpiClaim activity completed.");
                }
                return true;
            } catch (Exception ex) {
                LOG.debug("Exception caught processing claim '{}': {}", claimResult.getClaim().getChoReference(), ex.getMessage());
                claimResult.setValid(false);
                claimResult.getMessage().add(ex.getMessage());
                return false;
            }
        } else {
            LOG.debug("claimResult not valid for claim: isValid={} isDataValid={}", claimResult.isValid(), claimResult.isDataValid());
            return false;
        }
    }
    @Override

    public List<ClaimResult> formClaimResults(Document document) throws Exception {
        Element root = document.getDocumentElement();
        List<ClaimResult> claimElements = new ArrayList<ClaimResult>();

        List<Element> rentals = XMLUtils.getElements(document, root, "rental");

        if (rentals != null && rentals.size() > 0) {

            for (Element e : rentals) {

                ClaimResult claimResult = new ClaimResult();
                claimResult.setElement(e);
                claimResult.setCheckDataValid(true);
                claimResult.setDataValid(true);
                claimResult.setValid(true);
                claimElements.add(claimResult);
            }
        }
        return claimElements;
    }

    private void validate(ClaimResult claimResult, List<String> choReferences) {
        LOG.debug("Validating CHO references are unique");
//        if (claimResult.getClaimParseStatus().equals(ClaimParseStatus.newClaim)) {

            if (claimResult.getClaim() != null) {

                // CHECK DUPLICATE
                if (claimResult.getClaim().getChoReference() != null && !claimResult.getClaim().getChoReference().equalsIgnoreCase("") ) {

                    if (choReferences.contains(claimResult.getClaim().getChoReference().toLowerCase().trim())) {
                        LOG.info("Duplicate Supplier Reference found: {}", claimResult.getClaim().getChoReference());
                        claimResult.setValid(false);
                        claimResult.setDuplicateClaimInSameXmlFile(true);
                        claimResult.getMessage().add("Duplicate Supplier Reference -  Supplier Reference already exists in bordereau");

                    } else {
                        choReferences.add(claimResult.getClaim().getChoReference().toLowerCase().trim());
                    }
                }


            }
    }

    public void setBordereauReader(BordereauReader bordereauReader) {
        this.bordereauReader = bordereauReader;
    }

    public void setActivityFactory(ActivityFactory activityFactory) {
        this.activityFactory = activityFactory;
    }

    @Override
    public boolean validateFile(File uploadedFile){
        return false;
    }

    @Override
    public boolean processFile(File uploadedFile){
        return false;
    }

    @Override
    public void evictClaim(Claim claim){
             getHibernateTemplate().evict(claim);
             LOG.debug("Claim evicted.");
    }
}
