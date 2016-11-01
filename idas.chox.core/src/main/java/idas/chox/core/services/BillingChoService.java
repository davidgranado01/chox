package idas.chox.core.services;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import idas.chox.core.model.BillingCho;
import idas.chox.core.model.BillingChoDetail;
import idas.chox.core.model.Chorganisation;

public interface BillingChoService {

    Map checkObject(String scheduleName, Date dateFrom,
			Date dateTo, int choId);

    BillingCho getObject(int id);

    BillingCho updateObject(BillingCho object);

    List<BillingCho> getBillingChos();

    void deteteObject(BillingCho object);

    List<BillingChoDetail> findClaimsforSchedule(Date from, Date to, Chorganisation cho);

    Set<BillingChoDetail> getScheduleDetailList(final int id);

    List searchBills(String choReference, String claimNumber);

    int getNumberInvoicesSubmitted(Date dateFrom, Date dateTo, Chorganisation cho);

}