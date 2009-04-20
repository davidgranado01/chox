/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.web.viewdata;

import chox.Util.DateHelper;
import chox.model.LineOfBusiness;

public class LineOfBusinessViewData {
    
    private int id;
    private int insurerId;
    private String insurerName;
    private String name;
    private String address1;
    private String address2;
    private String address3;
    private String address4;
    private String address5;
    private String postcode;
    private String createdBy;
    private String createdDate;    
    private boolean status;
    private String statusDesc;

    
    public LineOfBusinessViewData(LineOfBusiness object) {
        
        this.id = object.getId();
        this.insurerId = object.getInsurer().getId();
        this.insurerName = object.getInsurer().getName();
        this.name = object.getName();
        this.address1 = object.getAddress1();
        this.address2 = object.getAddress2();
        this.address3 = object.getAddress3();
        this.address4 = object.getAddress4();
        this.address5 = object.getAddress5();
        this.postcode = object.getPostcode();
        this.createdBy = object.getCreatedBy().getDisplayName();
        this.createdDate = DateHelper.GridViewDateFormat.format(object.getCreatedDate());   
        this.status = object.isActive();
        
        this.statusDesc = "Active";
        if(!object.isActive()){
            this.statusDesc = "Inactive";
        }

    }

    public boolean getStatus() {
        return status;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    
    
    public String getAddress1() {
        return address1;
    }

    public String getAddress2() {
        return address2;
    }

    public String getAddress3() {
        return address3;
    }

    public String getAddress4() {
        return address4;
    }

    public String getAddress5() {
        return address5;
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

    public int getInsurerId() {
        return insurerId;
    }

    public String getInsurerName() {
        return insurerName;
    }

    public String getName() {
        return name;
    }

    public String getPostcode() {
        return postcode;
    }


    


    
}
