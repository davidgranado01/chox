package idas.chox.core.services;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author John
 */
public interface AdminFeeService {
   public BigDecimal getAdminFee(Date startDate, boolean coverNoteRequired, boolean managingRepair) throws Exception;
}
