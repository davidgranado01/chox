package idas.chox.data.services;

import java.math.BigDecimal;
import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import idas.chox.core.model.BreBand;
import idas.chox.core.model.BreBandOrganisation;
import idas.chox.core.model.Insurer;
import idas.chox.core.services.BreBandOrganisationService;
import idas.chox.core.services.BreBandService;

public class BreBandServiceImpl extends SecureDataService implements BreBandService {

    private BreBandOrganisationService breBandOrganisationService;

    public void setBreBandOrganisationService(BreBandOrganisationService breBandOrganisationService) {
        this.breBandOrganisationService = breBandOrganisationService;
    }

    @Override
    public List<BreBand> getInsurerBreBandsByInsurer(int insurerId) {
        DetachedCriteria criteria = DetachedCriteria.forClass(BreBand.class);
        criteria.add(Restrictions.eq("insurer.id", insurerId));
        return findByCriteria(criteria);
    }

    @Override
    public boolean isBreBandNameExist(BreBand object) {

        boolean bFlag = false;

        DetachedCriteria criteria = DetachedCriteria.forClass(BreBand.class);
        criteria.add(Restrictions.eq("name", object.getName()));
        criteria.add(Restrictions.eq("insurer.id", object.getInsurer().getId()));

        if (object.getId() != null) {
            if (object.getId() > 0) {
                criteria.add(Restrictions.ne("id", object.getId()));
            }
        }

        List<BreBand> objects = findByCriteria(criteria);

        if (objects.size() > 0) {
            bFlag = true;
        }

        return bFlag;
    }

    @Override
    public boolean isBreBandOccupied(BreBand breBand) {
        return breBandOrganisationService.isBreBandOccupied(breBand.getId());
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    @Override
    public void createDefaultRecord(Insurer insurer) {
        BreBand object = getDummyBreBand();
        object.setName("Default");
        object.setInsurer(insurer);
        object.setIsActive(true);
        saveBreBand(object);
    }

    public BreBand getDummyBreBand() {
        BreBand object = new BreBand();
        object.setEngineerInspectionDelayDaysMobile(2);
        object.setEngineerInspectionDelayDaysNonMobile(2);
        object.setHireDayCeiling(22);
        object.setHireNetCeiling(new BigDecimal("1500.00"));
        object.setHireRateChargeTolerance(new BigDecimal("0.01"));
        object.setInspectionDelayDays(4);
        object.setIsMobileDayAllowance(2);
        object.setIsNotMobileDayAllowance(9);
        object.setRepairNetCeiling(new BigDecimal("1500.00"));
        object.setOfferMadeDays(7);
        object.setReceiptOfFinalStatementChequeDays(10);
        object.setTakeVehicleOutDays(1);
        object.setTakeVehicleToGarageDaysMobile(1);
        object.setTakeVehicleToGarageDaysNonMobile(3);
        object.setWeekendBufferDays(2);
        object.setAverageLabourHoursPerHireDay(4);
        object.setAverageLabourRateStandard(40);
        object.setAverageLabourRatePrestige(40);
        object.setNonStandardInsurancePremiumCeilingTolerance(BigDecimal.ZERO);
        return object;
    }

    @Override
    public BreBand getBreBand(int id) {
        return (BreBand) get(BreBand.class, id);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    @Override
    public void saveBreBand(BreBand breBand) {
        save(breBand);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    @Override
    public void deleteBreBand(BreBand breBand) {
        delete(breBand);
    }

    @Override
    public BreBand getBreBand(int orgId, int insurerId) {

        BreBand band = null;

        List<BreBandOrganisation> bandChorgs = breBandOrganisationService.getBreBandChorganisationsByChoOrgId(orgId);

        for (BreBandOrganisation object : bandChorgs) {
            if (object.getBreBand().getInsurer().getId() == insurerId) {
                band = object.getBreBand();
                break;
            }
        }
        return band;
    }

    @Override
    public boolean isSupplierRatesActivated(int orgId, int insurerId) {
        BreBand band = getBreBand(orgId, insurerId);

        return band.isUseSupplierRates();
    }
}
