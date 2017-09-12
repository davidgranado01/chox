package idas.chox.service.bre;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.bre.RuleEvaluation;
import idas.chox.core.bre.RuleEvaluationResult;
import idas.chox.core.bre.RulesEngine;
import idas.chox.core.bre.RulesEngineResponse;
import idas.chox.core.model.BreBand;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.ClaimType;
import idas.chox.core.model.EngineerReport;
import idas.chox.core.model.History;
import idas.chox.core.model.VehicleClassCeiling;
import idas.chox.core.services.BreBandService;
import idas.chox.core.services.BusinessRulesEngService;
import idas.chox.core.services.ClaimService;
import idas.chox.core.services.HistoryService;
import idas.chox.core.services.InsurerService;
import idas.chox.core.xmlValidation.ClaimResult;

public class BusinessRulesEngServiceImpl implements BusinessRulesEngService {

    private static final Logger LOG = LoggerFactory.getLogger(BusinessRulesEngServiceImpl.class);
    private ClaimService claimService;
    private BreBandService choBandService;
    private InsurerService insurerService;
    private HistoryService historyService;
    private RulesEngine rulesEngine;

    public void setClaimService(ClaimService claimService) {
        this.claimService = claimService;
    }

    public void setInsurerService(InsurerService insurerService) {
        this.insurerService = insurerService;
    }

    public void setBreBandService(BreBandService choBandService) {
        this.choBandService = choBandService;
    }

    public HistoryService getHistoryService() {
        return historyService;
    }

    public void setHistoryService(HistoryService historyService) {
        this.historyService = historyService;
    }

    private void constructBreValidateObject(Claim claim) {
        if (claim.getEngineerReport() == null) {
            EngineerReport engineerreport = new EngineerReport();
            engineerreport.setDays(0);
            engineerreport.setLabourAmount(new BigDecimal("0.00"));
            engineerreport.setTotalAmount(new BigDecimal("0.00"));
            claim.setEngineerReport(engineerreport);
        }
        if (claimService.getCountOfClaimByVRN(claim.getCustomer().getVehicleRegistration(), claim) > 0) {
                claim.getCustomer().setIsVehicleRegistrationExist(true);
        }
    }

    private RulesEngineResponse validate(Claim claim) {
        RulesEngineResponse reponse = rulesEngine.validate(claim);
        return reponse;
    }

    
    @Override
    public RulesEngineResponse processResubmitInvoice(Claim claim) {
        LOG.debug("Processing re-submitted invoice for claim '{}'", claim.getChoReference());
        BreBand choBand = choBandService.getBreBand(claim.getChorganisation().getId(), claim.getInsurer().getId());
        if (choBand == null) {
            LOG.error("No BRE band available for claim '{}'", claim.getChoReference());
            return null;
        }

        VehicleClassCeiling vehicleClassCeiling = insurerService.getVechileClassCeilingForClaim(claim);
        if (vehicleClassCeiling != null) {
            LOG.debug("Got vehicleClassCeiling: {}", vehicleClassCeiling.getHireNetCeiling());
        } else {
            LOG.warn("Could not get vehicle class ceiling for claim '{}'", claim.getChoReference());
        }
        choBand.setVehicleClassCeiling(vehicleClassCeiling);
        claim.setBreBand(choBand);

        String oldStatus = claim.getStatus();
        LOG.trace("Old claim status is '{}'", oldStatus);
        constructBreValidateObject(claim);
        LOG.trace("BRE validate object constructed");
        // Mark currrent BRE history as old (if claim exists)
        if (oldStatus != null && !oldStatus.isEmpty()) {
            LOG.debug("Marking history as old for claim '{}')", claim.getChoReference());
            historyService.markHistoryAsOldByClaim(claim);
        }
        
        LOG.debug("Validating claim...");
        RulesEngineResponse validationResult = validate(claim);
        LOG.debug("Validation result contains {} messages", validationResult.getResults().size());
        String newClaimStatus = validationResult.getStatus(claim.getInsurer().isEngineersEnable());
        if (ClaimType.isSupplementaryInvoice(claim.getClaimType())
            && !ClaimType.isOriginalSupplementaryInvoice(claim.getClaimType())
            && newClaimStatus.equals(ClaimStatus.INVOICE_ESCALATED)) {
            newClaimStatus = ClaimStatus.INVOICE_ESCALATED_TO_CH;
        }
        LOG.debug("Validation result status is: {}", newClaimStatus);
        // at some point setting claim status need to be removed. there is no use doing it here. it's already being done in newinvoice class. 
        if (ClaimType.isTPI(claim.getClaimType())) {
            claim.setTpiClaimStatus(newClaimStatus);
        }  else if (!newClaimStatus.equals(oldStatus)) {
            claim.setPreviousStatus(oldStatus);
            claim.setStatus(newClaimStatus);
        }

        return validationResult;
    }

    @Override
    public void setRulesEngine(RulesEngine rulesEngine) {
        this.rulesEngine = rulesEngine;
    }

    @Override
    public RulesEngine getRulesEngine() {
        return rulesEngine;
    }
}
