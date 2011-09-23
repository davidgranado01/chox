/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package idas.chox.web.viewdata;

import idas.chox.core.model.AutomaticRoutingPrice;
import idas.chox.core.util.DateHelper;
import java.math.BigDecimal;

/**
 *
 * @author rajareddydodda
 */
public class InsurerAutomaticRoutingByPriceViewData {


    private int id;
    private int insurerId;
    private String insurerName;
    private int workgroupId;
    private String workgroupName;
    private BigDecimal price;
    private String createdBy;
    private String createdDate;



    public InsurerAutomaticRoutingByPriceViewData(AutomaticRoutingPrice automaticRouting) {
        this.id = automaticRouting.getId();
        this.price = automaticRouting.getPrice();
        this.insurerId = automaticRouting.getInsurer().getId();
        this.insurerName = automaticRouting.getInsurer().getName();
        this.workgroupId = automaticRouting.getWorkgroup().getId();
        this.workgroupName = automaticRouting.getWorkgroup().getName();
        this.createdBy = automaticRouting.getCreatedBy().getDisplayName();
        this.createdDate = DateHelper.getLocalDateTimeFormat().format(automaticRouting.getCreatedDate());
        
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the insurerId
     */
    public int getInsurerId() {
        return insurerId;
    }

    /**
     * @param insurerId the insurerId to set
     */
    public void setInsurerId(int insurerId) {
        this.insurerId = insurerId;
    }

    /**
     * @return the insurerName
     */
    public String getInsurerName() {
        return insurerName;
    }

    /**
     * @param insurerName the insurerName to set
     */
    public void setInsurerName(String insurerName) {
        this.insurerName = insurerName;
    }

    /**
     * @return the workgroupId
     */
    public int getWorkgroupId() {
        return workgroupId;
    }

    /**
     * @param workgroupId the workgroupId to set
     */
    public void setWorkgroupId(int workgroupId) {
        this.workgroupId = workgroupId;
    }

    /**
     * @return the workgroupName
     */
    public String getWorkgroupName() {
        return workgroupName;
    }

    /**
     * @param workgroupName the workgroupName to set
     */
    public void setWorkgroupName(String workgroupName) {
        this.workgroupName = workgroupName;
    }

    /**
     * @return the price
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /**
     * @return the createdBy
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * @param createdBy the createdBy to set
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * @return the createdDate
     */
    public String getCreatedDate() {
        return createdDate;
    }

    /**
     * @param createdDate the createdDate to set
     */
    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }


}
