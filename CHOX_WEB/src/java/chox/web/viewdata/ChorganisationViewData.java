package chox.web.viewdata;
import chox.Util.DateHelper;
import chox.model.Chorganisation;

public class ChorganisationViewData {

    private int id;
    private String name;
    private String address;
    private String vatNo;
    private String companyNo;
    private String authoritiyDelegated;
    private boolean status;
    private String statusDesc;
    private String createdBy;
    private String createdDate;

    public ChorganisationViewData(Chorganisation chorganisation) {
        
        this.id = chorganisation.getId();
        this.name = chorganisation.getName();
        this.address = chorganisation.getDisplayAddress();
        this.vatNo = chorganisation.getVatNo();
        this.companyNo = chorganisation.getCompanyNo();
        this.createdBy = chorganisation.getCreatedBy().getDisplayName();
        this.createdDate = DateHelper.GridViewDateFormat.format(chorganisation.getCreatedDate());            
        this.status = chorganisation.isStatus();
        
        if(chorganisation.getIsDelegatedAuthority()){
            this.authoritiyDelegated = "Yes";
        }else{
            this.authoritiyDelegated = "No";
        }
        
        if(chorganisation.isStatus()){
            this.statusDesc = "Active";
        }else{
            this.statusDesc = "Inactive";
        }

    }

    public String getAddress() {
        return address;
    }

    public String getAuthoritiyDelegated() {
        return authoritiyDelegated;
    }

    public String getCompanyNo() {
        return companyNo;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getVatNo() {
        return vatNo;
    }

    public boolean isStatus() {
        return status;
    }

    public String getStatusDesc() {
        return statusDesc;
    }



}
