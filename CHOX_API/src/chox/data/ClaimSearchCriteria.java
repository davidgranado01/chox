/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chox.data;

import java.util.Date;

/**
 *
 * @author Emmanuel
 */
public interface ClaimSearchCriteria {

    public String getSupplierReference();

    public String getSupplierName();

    public String getClaimNumber();

    public String getInsurerName();

    public String getVrn();

    public String getInvoiceNumber();

    public Date getClaimUploadDateFrom();

    public Date getClaimUploadDateTo();

    public void setClaimUploadDateTo(Date claimUploadDateTo);

    public Date getInvoiceUploadDateFrom();

    public Date getInvoiceUploadDateTo();

    public Date getHireDateFrom();

    public Date getHireDateTom();
}
