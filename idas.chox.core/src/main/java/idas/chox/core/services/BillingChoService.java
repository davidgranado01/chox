package idas.chox.core.services;

import idas.chox.core.model.BillingCho;
import idas.chox.core.model.BillingChoDetail;

import idas.chox.core.model.Chorganisation;
import idas.chox.core.model.Claim;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface BillingChoService {

	public abstract Map checkObject(String scheduleName, Date dateFrom,
			Date dateTo, int choId);

	public abstract BillingCho getObject(int id);

	public abstract BillingCho updateObject(BillingCho object);

	public abstract List<BillingCho> getBillingChos();

	public abstract void deteteObject(BillingCho object);

        List<Claim> findClaimsforSchedule(Date from, Date to, Chorganisation cho);

	public abstract Set<BillingChoDetail> getScheduleDetailList(final int id);

//        List findInvoiceforSchedule(Date dateFrom, Date dateTo, Chorganisation cho);

    public List searchBills(String choReference, String claimNumber);

}