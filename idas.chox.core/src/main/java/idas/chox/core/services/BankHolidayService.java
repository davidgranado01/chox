package idas.chox.core.services;

import java.util.Date;

/**
 *
 * @author John
 */
public interface BankHolidayService {
   int getNoHolidaysBetween(Date startDate, Date endDate);
}
