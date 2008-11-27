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
import com.opensymphony.xwork2.conversion.annotations.TypeConversion;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import net.sf.json.JSONArray;
import org.apache.struts2.interceptor.SessionAware;

/**
 *
 * @author Emmanuel
 */
public class SearchClaimAction extends BaseAction implements ClaimSearchCriteria,SessionAware {

    private Map session;    
    private String supplierReference;
    private int supplierId;
    private String claimNumber;
    private int insurerId;
    private int lineOfBusinessId;
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

    public String getClaimNumber() {
        return claimNumber;
    }

    public void setClaimNumber(String claimNumber) {
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

    @TypeConversion(converter = "chox.web.data.DateConverter")
    public void setClaimUploadDateFrom(Date claimUploadDateFrom) {
        this.claimUploadDateFrom = claimUploadDateFrom;
    }

    public Date getClaimUploadDateTo() {
        return claimUploadDateTo;
    }

    @TypeConversion(converter = "chox.web.data.DateConverter")
    public void setClaimUploadDateTo(Date claimUploadDateTo) {
        this.claimUploadDateTo = claimUploadDateTo;
    }

    public Date getInvoiceUploadDateFrom() {
        return invoiceUploadDateFrom;
    }

    @TypeConversion(converter = "chox.web.data.DateConverter")
    public void setInvoiceUploadDateFrom(Date invoiceUploadDateFrom) {
        this.invoiceUploadDateFrom = invoiceUploadDateFrom;
    }

    public Date getInvoiceUploadDateTo() {
        return invoiceUploadDateTo;
    }

    @TypeConversion(converter = "chox.web.data.DateConverter")
    public void setInvoiceUploadDateTo(Date invoiceUploadDateTo) {
        this.invoiceUploadDateTo = invoiceUploadDateTo;
    }

    public Date getHireDateFrom() {
        return hireDateFrom;
    }

    @TypeConversion(converter = "chox.web.data.DateConverter")
    public void setHireDateFrom(Date hireDateFrom) {
        this.hireDateFrom = hireDateFrom;
    }

    public Date getHireDateTo() {
        return hireDateTom;
    }

    @TypeConversion(converter = "chox.web.data.DateConverter")
    public void setHireDateTo(Date hireDateTo) {
        this.hireDateTom = hireDateTo;
    }

    public List getStatuses() {
        if (statuses == null) {
            statuses = this.lookupService.getStatuses();
        }
        return statuses;
    }

    public List getLineOfBusinesses() {

        if (lineOfBusiness == null) {
            lineOfBusiness = this.lookupService.getLineOfBusinesses();
        }
        return lineOfBusiness;
    }

    public List getInsurers() {
        if (insurers == null) {
            insurers = this.lookupService.getInsurers();
        }
        return insurers;
    }

    public List getSuppliers() {
        if (suppliers == null) {
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

        ClaimSearchCriteria c = this;
        session.put("searchCriteria", c);
        
        results = this.claimService.searchClaims(c);
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

    public int getLineOfBusinessId() {
        return lineOfBusinessId;
    }

    public void setLineOfBusinessId(int lineOfBusinessId) {
        this.lineOfBusinessId = lineOfBusinessId;
    }

    public void setSession(Map session) {
        session = session;
    }

}
