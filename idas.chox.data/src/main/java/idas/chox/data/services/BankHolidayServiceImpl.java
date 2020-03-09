package idas.chox.data.services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import idas.chox.core.model.BankHoliday;
import idas.chox.core.services.BankHolidayService;

/**
 *
 * @author john
 */
public class BankHolidayServiceImpl extends SecureDataService implements BankHolidayService {
    
    @Override
    public int getNoHolidaysBetween(Date startDate, Date endDate) {
        // Set time to 00:00:00.000
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(startDate);
        cal1.set(Calendar.HOUR_OF_DAY, 0);
        cal1.set(Calendar.MINUTE, 0);
        cal1.set(Calendar.SECOND, 0);
        cal1.set(Calendar.MILLISECOND, 0);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(endDate);
        cal2.set(Calendar.HOUR_OF_DAY, 0);
        cal2.set(Calendar.MINUTE, 0);
        cal2.set(Calendar.SECOND, 0);
        cal2.set(Calendar.MILLISECOND, 0);
        
        DetachedCriteria criteria = DetachedCriteria.forClass(BankHoliday.class);
        criteria.add(Restrictions.ge("bankHoliday", cal1.getTime()));
        criteria.add(Restrictions.le("bankHoliday", cal2.getTime()));

        List<BankHoliday> holidays = new ArrayList<>();
        try {
            holidays = this.findByCriteria(criteria);
        } catch (Exception ex) {
        }
        
        return holidays.size();
    }
    
}
