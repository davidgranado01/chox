package idas.chox.service.xml;

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
import idas.chox.service.xml.validations.CHOReferenceValidation;
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
    public void doProcessBordereauResult(ClaimResult claimResult) {

        CHOReferenceValidation choReferenceValidation = new CHOReferenceValidation();
        try {

            bordereauReader.execute(claimResult);

        } catch (Exception ex) {
        }

        choReferenceValidation.validate(claimResult);
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
//                            totalProcessed++;
//                            LOG.debug("{} of {} claims have been processed", totalRecord, totalProcessed);
            } catch (Exception ex) {
                LOG.debug("Exception caught processing claim '{}': {}", claimResult.getClaim().getChoReference(), ex.getMessage());
                claimResult.setValid(false);
                claimResult.getMessage().add(ex.getMessage());
            }
        } else {
            LOG.debug("claimResult not valid for claim: isValid={} isDataValid={}", claimResult.isValid(), claimResult.isDataValid());
        }
    }
//                             if (claimResult.getClaimParseStatus().equals(ClaimParseStatus.newInvoice)) {
//                                 claimResult.getClaim().setInvoice(null);
//                             }
//                             getHibernateTemplate().evict(claimResult.getClaim()); - for some reason this causes the claim status not to be saved!!
//                             LOG.debug("Claim evicted.");

//                }
//                if (totalProcessed >= totalRecord) {
//                    bordereauResult.setBordereauStatus(BordereauParseStatus.allUploaded);
//                    bordereauResult.setBordereauParseStatusDescription("All claims have been uploaded successfully");
//                } else if (totalProcessed < totalRecord && totalProcessed != 0) {
//                    bordereauResult.setBordereauStatus(BordereauParseStatus.partialUpload);
//                    bordereauResult.setBordereauParseStatusDescription(totalProcessed + " out of " + totalRecord + " claims have been uploaded");
//                } else if (totalProcessed == 0) {
//                    bordereauResult.setBordereauStatus(BordereauParseStatus.allRejected);
//                    bordereauResult.setBordereauParseStatusDescription("All " + totalRecord + " claims have been rejected");
//                }
//            } catch (Exception ex) {
//                bordereauResult.setBordereauStatus(BordereauParseStatus.error);
//                bordereauResult.setBordereauParseStatusDescription("Invalid Schema");
//            }
//        } else {
//            bordereauResult.setBordereauStatus(BordereauParseStatus.error);
//            bordereauResult.setBordereauParseStatusDescription("Invalid Schema");
//        }
//        } else {
//
//            bordereauResult.setBordereauStatus(BordereauParseStatus.error);
//            bordereauResult.setBordereauParseStatusDescription("Error, Please try again");
//        }
//        return bordereauResult;
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

    public void setBordereauReader(BordereauReader bordereauReader) {
        this.bordereauReader = bordereauReader;
    }

    public void setActivityFactory(ActivityFactory activityFactory) {
        this.activityFactory = activityFactory;
    }
}
