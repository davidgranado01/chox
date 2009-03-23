package scsbre.tests.sample;

import java.math.BigDecimal;

import scsbre.model.IInsurerInfo;

public class InsurerInfo implements IInsurerInfo {

    private BigDecimal adminHandlingCharge;

    /* (non-Javadoc)
     * @see scsbre.model.IInsurerInfo#getAdminHandlingCharge()
     */
    public BigDecimal getAdminHandlingCharge() {
        return adminHandlingCharge;
    }

    /* (non-Javadoc)
     * @see scsbre.model.IInsurerInfo#setAdminHandlingCharge(java.math.BigDecimal)
     */
    public void setAdminHandlingCharge(BigDecimal adminHandlingCharge) {
        this.adminHandlingCharge = adminHandlingCharge;
    }
}
