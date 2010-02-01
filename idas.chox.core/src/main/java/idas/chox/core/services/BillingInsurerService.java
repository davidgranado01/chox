package idas.chox.core.services;

import idas.chox.core.model.Billing;
import idas.chox.core.model.BillingInsurer;
import idas.chox.core.model.BillingInsurerDetail;

import idas.chox.core.model.Insurer;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface BillingInsurerService {

	public abstract Map checkObject(String scheduleName, Date dateFrom,
			Date dateTo);

	public abstract BillingInsurer getObject(int id);

	public abstract Billing updateObject(BillingInsurer object);

	public abstract List<BillingInsurer> getBillingInsurers();

	public abstract void deteteObject(BillingInsurer object);

	public abstract List findClaimsBetween(Date from, Date to);

        public abstract List findClaimsforSchedule(Date from, Date to,Insurer insurer);

	public abstract Set<BillingInsurerDetail> getScheduleDetailList(final int id);

}