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

    public Map checkObject(String scheduleName, Date dateFrom,
			Date dateTo, int choId);

    public BillingCho getObject(int id);

    public BillingCho updateObject(BillingCho object);

    public List<BillingCho> getBillingChos();

    public void deteteObject(BillingCho object);

    List<Claim> findClaimsforSchedule(Date from, Date to, Chorganisation cho, boolean excludeSupplmntInv);

    public Set<BillingChoDetail> getScheduleDetailList(final int id);

    public List searchBills(String choReference, String claimNumber);

    public int getNumberInvoicesSubmitted(Date dateFrom, Date dateTo, Chorganisation cho);

}