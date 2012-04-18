package idas.chox.service.filters;

import java.util.Arrays;
import java.util.HashSet;

import idas.chox.core.search.ClaimSearchCriteria;

public class InvoiceEscalatedToSupervisor extends BaseFilter {

	private String name;
	private String key;

	@Override
	public ClaimSearchCriteria getClaimSearchCriteria(int insurerId, int choId) {

		ClaimSearchCriteria claimSearchCriteria = new ClaimSearchCriteria();
		claimSearchCriteria.setEscalatedToSupervisor(true);
		claimSearchCriteria.setIsWorkgroupCheck(getIsFilterWorkGroup());
		claimSearchCriteria.setIsOwnerShipCheck(getIsFilterOwnership());
		claimSearchCriteria
				.setIsSupplierOwnerShipCheck(getIsFilterSupplierOwnership());
		if (insurerId > -1)
			claimSearchCriteria.setInsurerIds(new HashSet<Integer>(Arrays.asList(insurerId)));
		if (choId > -1)
			claimSearchCriteria.setSupplierIds(new HashSet<Integer>(Arrays.asList(choId)));

		return claimSearchCriteria;
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

}
