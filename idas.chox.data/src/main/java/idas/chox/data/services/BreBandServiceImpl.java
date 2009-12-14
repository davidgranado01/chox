package idas.chox.data.services;

import idas.chox.core.model.BreBand;
import idas.chox.core.model.BreBandOrganisation;
import idas.chox.core.model.Insurer;
import idas.chox.core.services.BreBandOrganisationService;
import idas.chox.core.services.BreBandService;
import org.hibernate.criterion.Restrictions;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;

public class BreBandServiceImpl extends SecureDataService implements BreBandService {

    private BreBandOrganisationService breBandOrganisationService;

    public void setBreBandOrganisationService(BreBandOrganisationService breBandOrganisationService) {
        this.breBandOrganisationService = breBandOrganisationService;
    }

    public List<BreBand> getInsurerBreBand(int insurerId) {

        List<BreBand> breBand = new ArrayList<BreBand>();

        try {

            DetachedCriteria criteria = DetachedCriteria.forClass(BreBand.class);

            if (insurerId > 0) {

                criteria.add(Restrictions.eq("insurer.id", insurerId));

            }

            criteria.addOrder(Order.asc("name"));
            breBand = findByCriteria(criteria);

        } catch (Throwable e) {
            e.printStackTrace();
        }

        return breBand;
    }

    public boolean isBreBandNameExist(BreBand object) {

        boolean bFlag = false;

        List<BreBand> objects = new ArrayList<BreBand>();

        try {

            DetachedCriteria criteria = DetachedCriteria.forClass(BreBand.class);
            criteria.add(Restrictions.eq("name", object.getName()));
            criteria.add(Restrictions.eq("insurer.id", object.getInsurer().getId()));

            if (object.getId() != null) {
                if (object.getId() > 0) {
                    criteria.add(Restrictions.ne("id", object.getId()));
                }
            }

            objects = findByCriteria(criteria);

        } catch (Throwable e) {
            e.printStackTrace();
        }

        if (objects.size() > 0) {
            bFlag = true;
        }

        return bFlag;
    }

    public boolean isBreBandOccupied(BreBand object) {
        return breBandOrganisationService.isBreBandOccupied(object.getId());
    }

    public void createDefaultRecord(Insurer insurer) {
        BreBand object = getDummyBreBand();
        object.setName("Default");
        object.setInsurer(insurer);
        object.setIsActive(true);
        updateObject(object);
    }

    public BreBand getDummyBreBand() {
        BreBand object = new BreBand();
        object.setEngineerInspectionDelayDays(2);
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
        object.setAverageLabourRate(40);
        return object;
    }

    public BreBand getObject(int id) {
        return (BreBand) get(BreBand.class, id);
    }

    public void updateObject(BreBand object) {

        save(object);
    }

    public boolean deleteObject(BreBand object) {

        boolean bFlag = false;

        try {

            if (breBandOrganisationService.deleteBreBandOrganisationByBandId(object.getId())) {

                delete(object);
                bFlag = true;
            }

        } catch (Throwable e) {
            bFlag = false;
        }

        return bFlag;
    }

    public BreBand getBreBandByChorganisationIdAndInsurerId(int orgId, int insurerId) {

        BreBand band = new BreBand();

        try {

            List<BreBandOrganisation> bandChorgs = breBandOrganisationService.getBreBandChorganisationsByChoOrgId(orgId);

            for (BreBandOrganisation object : bandChorgs) {
                if (object.getBreBand().getInsurer().getId() == insurerId) {
                    band = object.getBreBand();
                    break;
                }
            }

        } catch (Throwable e) {
            e.printStackTrace();
        }

        return band;
    }

}
