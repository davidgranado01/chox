package idas.chox.core.services;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import idas.chox.core.model.Billing;
import idas.chox.core.model.BillingInsurer;
import idas.chox.core.model.BillingInsurerDetail;
import idas.chox.core.model.Claim;
import idas.chox.core.model.Insurer;

public interface BillingInsurerService {

	Map checkObject(String scheduleName, Date dateFrom,
			Date dateTo, int insurerId, boolean manualClaims);

	BillingInsurer getObject(int id);

	Billing updateObject(BillingInsurer object);

	List<BillingInsurer> getBillingInsurers();

        void deleteObject(BillingInsurer object);

        List<Claim> findClaimsforSchedule(Date from, Date to,Insurer insurer,
                                          boolean excludeSupplmntInv, boolean manualInvoicesOnly);

	Set<BillingInsurerDetail> getScheduleDetailList(final int id);

        List searchBills(String choReference, String claimNumber);
}