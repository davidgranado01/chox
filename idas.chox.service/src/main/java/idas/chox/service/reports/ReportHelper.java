package idas.chox.service.reports;

import idas.chox.core.model.Chorganisation;
import idas.chox.core.model.Insurer;
import idas.chox.data.services.DataService;
import java.math.BigDecimal;
import java.math.BigInteger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

public class ReportHelper {

    public static Integer getIntegerValue(Object v) {
        if (v.getClass().equals(Integer.class)) {
            return (Integer) v;
        } else if (v.getClass().equals(BigInteger.class)) {
            return ((BigInteger) v).intValue();
        } else {
            return 0;
        }
    }

    public static BigDecimal getBigDecimalValue(Object v) {
        return (BigDecimal) v;
    }

    public Insurer getInsurer(Integer sObjectId, DataService dataService) {

        Insurer ins = new Insurer();

        try {

            DetachedCriteria criteria = DetachedCriteria.forClass(Insurer.class);
            criteria.add(Restrictions.eq("id", sObjectId));
            ins = (Insurer) dataService.getByCriteria(criteria);

        } catch (Throwable e) {
            e.printStackTrace();
        }

        return ins;
    }

    public Chorganisation getChorganisation(Integer sObjectId, DataService dataService) {

        Chorganisation chorg = new Chorganisation();

        try {

            DetachedCriteria criteria = DetachedCriteria.forClass(Chorganisation.class);
            criteria.add(Restrictions.eq("id", sObjectId));
            chorg = (Chorganisation) dataService.getByCriteria(criteria);

        } catch (Throwable e) {
            e.printStackTrace();
        }

        return chorg;
    }
}
