/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chox.web.actions;

import chox.data.ClaimSearchCriteria;
import chox.model.Claim;
import chox.services.ClaimService;
import chox.services.LookupService;
import chox.web.viewdata.claimGridViewData;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import net.sf.json.JSONArray;

/**
 *
 * @author Emmanuel
 */
public class SearchClaimAction extends BaseAction implements ClaimSearchCriteria {

    private String supplierReference;
    private int supplierId;
    private int claimNumber;
    private int insurerId;
    private String vrn;
    private String invoiceNumber;
    private String status;
    private Date claimUploadDateFrom;
    private Date claimUploadDateTo;
    private Date invoiceUploadDateFrom;
    private Date invoiceUploadDateTo;
    private Date hireDateFrom;
    private Date hireDateTom;
    private List statuses;
    private List lineOfBusiness;
    private List insurers;
    private List suppliers;
    private LookupService lookupService;
    private ClaimService claimService;
    private List results;
    private int totalCount;

    public String getSupplierReference() {
        return supplierReference;
    }

    public void setSupplierReference(String supplierReference) {
        this.supplierReference = supplierReference;
    }

    public int getSupplierId() {
        return supplierId;
    }

    public void setSupplierName(int supplierId) {
        this.supplierId = supplierId;
    }

    public int getClaimNumber() {
        return claimNumber;
    }

    public void setClaimNumber(int claimNumber) {
        this.claimNumber = claimNumber;
    }

    public int getInsurerId() {
        return this.insurerId;
    }

    public void setInsurerId(int insurerId) {
        this.insurerId = insurerId;
    }

    public String getVrn() {
        return vrn;
    }

    public void setVrn(String vrn) {
        this.vrn = vrn;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public Date getClaimUploadDateFrom() {
        return claimUploadDateFrom;
    }

    public void setClaimUploadDateFrom(Date claimUploadDateFrom) {
        this.claimUploadDateFrom = claimUploadDateFrom;
    }

    public Date getClaimUploadDateTo() {
        return claimUploadDateTo;
    }

    public void setClaimUploadDateTo(Date claimUploadDateTo) {
        this.claimUploadDateTo = claimUploadDateTo;
    }

    public Date getInvoiceUploadDateFrom() {
        return invoiceUploadDateFrom;
    }

    public void setInvoiceUploadDateFrom(Date invoiceUploadDateFrom) {
        this.invoiceUploadDateFrom = invoiceUploadDateFrom;
    }

    public Date getInvoiceUploadDateTo() {
        return invoiceUploadDateTo;
    }

    public void setInvoiceUploadDateTo(Date invoiceUploadDateTo) {
        this.invoiceUploadDateTo = invoiceUploadDateTo;
    }

    public Date getHireDateFrom() {
        return hireDateFrom;
    }

    public void setHireDateFrom(Date hireDateFrom) {
        this.hireDateFrom = hireDateFrom;
    }

    public Date getHireDateTo() {
        return hireDateTom;
    }

    public void setHireDateTom(Date hireDateTom) {
        this.hireDateTom = hireDateTom;
    }

    public List getStatuses() {
        if(statuses == null)
        {
            statuses = this.lookupService.getStatuses();
        }
        return statuses;
    }

    public List getLineOfBusinesses() {
        
        if(lineOfBusiness == null)
        {
            lineOfBusiness = this.lookupService.getLineOfBusinesses();
        }
        return lineOfBusiness;
    }

    public List getInsurers() {
        if(insurers == null)
        {
            insurers = this.lookupService.getInsurers();
        }
        return insurers;
    }

    public List getSuppliers() {
        if(suppliers == null)
        {
            suppliers = this.lookupService.getSuppliers();
        }
        return suppliers;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public String getJsonData() {
        try {
            List<claimGridViewData> viewData = new ArrayList<claimGridViewData>();

            for (Object c : results) {
                viewData.add(new claimGridViewData((Claim) c));
            }

            JSONArray jsonArray = JSONArray.fromObject(viewData);
            return "{totalCount:" + this.getTotalCount() + ",results:" + jsonArray.toString() + "}";
        } catch (Exception ex) {
            return null;
        }
    }

    public void setLookupService(LookupService service) {
        this.lookupService = service;
    }

    public void setClaimService(ClaimService service) {
        this.claimService = service;
    }

    public String doSearchClaim() throws Exception {
       
        results = this.claimService.searchClaims(this);
        totalCount = results.size();
        return SUCCESS;

    }

    @Override
    public String execute() throws Exception {
      
        return SUCCESS;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
