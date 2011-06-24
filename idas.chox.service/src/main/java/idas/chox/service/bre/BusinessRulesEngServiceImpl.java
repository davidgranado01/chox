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
import idas.chox.core.model.EngineerReport;
import idas.chox.core.model.History;
import idas.chox.core.model.VehicleClassCeiling;
import idas.chox.core.services.BusinessRulesEngService;
import idas.chox.core.services.BreBandService;
import idas.chox.core.services.ClaimService;
import idas.chox.core.services.InsurerService;
import idas.chox.core.xmlValidation.ClaimResult;

public class BusinessRulesEngServiceImpl implements BusinessRulesEngService {

    private static final Logger LOG = LoggerFactory.getLogger(BusinessRulesEngServiceImpl.class);
    private ClaimService claimService;
    private BreBandService choBandService;
    private InsurerService insurerService;
    private RulesEngine rulesEngine;

    public void setClaimService(ClaimService claimService) {
        this.claimService = claimService;
    }

    public void setBreBandService(BreBandService choBandService) {
        this.choBandService = choBandService;
    }

    public void setInsurerService(InsurerService insurerService) {
        this.insurerService = insurerService;
    }

    private void constructBreValidateObject(Claim claim) {

        Boolean isIsTotalLostCheck = false;
        if (claim.getHireMonitoringDetail() != null) {
            isIsTotalLostCheck = claim.getHireMonitoringDetail().isIsTotalLostCheck();
        }
        if (claim.getVehicleHire() != null) {
            claim.getVehicleHire().setIsTotalLoss(isIsTotalLostCheck);
        }

        if (claim.getEngineerReport() == null) {
            EngineerReport engineerreport = new EngineerReport();
            engineerreport.setDays(0);
            engineerreport.setLabourAmount(new BigDecimal("0.00"));
            engineerreport.setTotalAmount(new BigDecimal("0.00"));
            claim.setEngineerReport(engineerreport);
        }
        if (claim.isTpiClaim()) {
            if (claimService.getCountOfClaimByVRNforTPIClaim(claim.getCustomer().getVehicleRegistration(), claim) > 0) {
                claim.getCustomer().setIsVehicleRegistrationExist(true);
            }
        } else {
            if (claimService.getCountOfClaimByVRN(claim.getCustomer().getVehicleRegistration(), claim.getId()) > 0) {
                claim.getCustomer().setIsVehicleRegistrationExist(true);
            }
        }
    }

    private RulesEngineResponse validate(Claim claim) {
        RulesEngineResponse reponse = rulesEngine.validate(claim);
        return reponse;
    }

    @Override
    public void process(ClaimResult claimResult) {

        BreBand choBand = choBandService.getBreBand(claimResult.getClaim().getChorganisation().getId(), claimResult.getClaim().getInsurer().getId());

        if (choBand.getId() != null) {

            Claim claim = claimResult.getClaim();
            VehicleClassCeiling vehicleClassCeiling = insurerService.getVechileClassCeilingForClaim(claim);
            choBand.setVehicleClassCeiling(vehicleClassCeiling);
            claim.setBreBand(choBand);

            constructBreValidateObject(claim);
            RulesEngineResponse validationResult = validate(claim);

            String oldStatus = claim.getStatus();
            String newClaimStatus = validationResult.getStatus(claim.getInsurer().isEngineersEnable()).toString();

            claim.setPreviousStatus(oldStatus);
            claim.setStatus(newClaimStatus);

            if (validationResult.getResults().size() > 0) {
                List<History> histories = processBreErrorMessage(validationResult.getResults(), claimResult);
                for (History history : histories) {
                    claim.addHistory(history);
                }
            }

        } else {

            claimResult.setValid(false);
            claimResult.getMessage().add("BRE Band is Not Defined, Please contact CHOX Admin");

        }
    }

    @Override
    public RulesEngineResponse processResubmitInvoice(Claim claim) {
        LOG.debug("Processing re-submitted invoice for claim '{}'", claim.getChoReference());
        BreBand choBand = choBandService.getBreBand(claim.getChorganisation().getId(), claim.getInsurer().getId());
        LOG.debug("Got choBand: {}", choBand.getName());
        VehicleClassCeiling vehicleClassCeiling = insurerService.getVechileClassCeilingForClaim(claim);
        if (vehicleClassCeiling != null) {
            LOG.debug("Got vehicleClassCeiling: {}", vehicleClassCeiling.getHireNetCeiling());
        } else {
            LOG.info("Could not get vehicle class ceiling for claim '{}' (with vehicle class '{}')", claim.getChoReference(), claim.getCustomer().getVehicleClass());
        }
        choBand.setVehicleClassCeiling(vehicleClassCeiling);
        claim.setBreBand(choBand);

        String oldStatus = claim.getStatus();
        LOG.debug("Old claim status is '{}'", oldStatus);
        constructBreValidateObject(claim);
        LOG.debug("Validating claim...");
        RulesEngineResponse validationResult = validate(claim);
        LOG.debug("Validation result contains {} messages", validationResult.getResults().size());
        String newClaimStatus = validationResult.getStatus(claim.getInsurer().isEngineersEnable()).toString();
        LOG.debug("Validation result status is: {}", newClaimStatus);
        // at some point setting claim status need to be removed. there is no use doing it here. it's already being done in newinvoice class. 
        if (!claim.isTpiClaim()) {
            claim.setPreviousStatus(oldStatus);
            claim.setStatus(newClaimStatus);
        } else {
            claim.setTpiClaimStatus(newClaimStatus);
        }

        return validationResult;
    }

    private List<History> processBreErrorMessage(List<RuleEvaluation> results, ClaimResult claimResult) {

        List<History> histories = new ArrayList<History>();

        for (int iCount = 0; iCount < results.size(); iCount++) {

            RuleEvaluation rv = results.get(iCount);
            if (rv.getIsVisibleToCHO() && rv.getResult() == RuleEvaluationResult.RuleFailed) {
                claimResult.getMessage().add(rv.toString());
            }

            histories.add(History.New(rv));
        }

        return histories;

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
