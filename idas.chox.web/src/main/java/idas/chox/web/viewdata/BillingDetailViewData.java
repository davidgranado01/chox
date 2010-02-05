/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package idas.chox.web.viewdata;


import idas.chox.core.model.BillingDetail;
import idas.chox.core.util.DateHelper;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;

/**
 *
 * @author abrar
 */
public class BillingDetailViewData {
    private static final Logger log = Logger.getLogger(BillingDetailViewData.class);
    public static DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    private int billingDetailId;
    private String scheduleName;
    private String claimReferenceId;
    private BigDecimal itemAmount;
    private BigDecimal amountReceived;
    private String receivedDate;
    private String comment;
    private boolean reconciled;

    public BillingDetailViewData(BillingDetail record) {
        this.billingDetailId = record.getId();
        this.scheduleName = record.getBilling().getScheduleName();
        this.claimReferenceId = record.getClaim().getClaimNumber();
        this.itemAmount = record.getBillAmount();
        this.amountReceived = record.getAmountReceived();
        this.receivedDate = record.getReceivedDate() == null ? "":DateHelper.LocalDateTimeFormat.format(record.getReceivedDate());
        this.comment = record.getComment();
        this.reconciled = record.isReconciled();
    }

    public static List<BillingDetailViewData> fromJSONString(String jsonStr){
    	JSONArray json = JSONArray.fromObject( jsonStr );
    	List<BillingDetailViewData> list = new ArrayList();
    	for (Iterator iterator = json.iterator(); iterator.hasNext();) {
			JSONObject object = (JSONObject) iterator.next();
			list.add( fromJSONObject(object) );
		}
    	return list;
    }

    public static List<Map> mapListFromJsonString(String json) throws ParseException{
    	JSONArray jay = JSONArray.fromObject( json );
    	List<Map> list = new ArrayList<Map>();
    	for (Iterator iterator = jay.iterator(); iterator.hasNext();) {
			JSONObject object = (JSONObject) iterator.next();
			list.add( fromJSONObjectToMap(object) );
		}

    	return list;
    }


    public static Map fromJSONObjectToMap(JSONObject object ) throws ParseException{
    	Map map = new HashMap();

    	map.put("billingDetailId",object.getInt("billingDetailId"));    	
    	map.put("comment", object.getString("comment"));    	
    	map.put("amountReceived", new BigDecimal(object.getDouble("amountReceived")));    	
    	if ( object.getString("receivedDate").equals("")) {
            map.put("receivedDate",null);
    	}else{
            map.put("receivedDate", formatter.parse(object.getString("receivedDate")));
        }
    	return map;
    }

    public static BillingDetailViewData fromJSONObject(JSONObject object ){
    	BillingDetailViewData detail = new BillingDetailViewData();
    	detail.setBillingDetailId(object.getInt("billingDetailId"));
    	detail.setAmountReceived(object.containsValue("")  ? null : BigDecimal.valueOf(object.getDouble("amountReceived")));
    	detail.setComment(object.getString("comment"));
    	return detail;
    }

    public BillingDetailViewData() {
        
    }
    /**
     * @return the id
     */
    public int getBillingDetailId() {
        return billingDetailId;
    }

    /**
     * @param id the id to set
     */
    public void setBillingDetailId(int id) {
        this.billingDetailId = id;
    }

    /**
     * @return the scheduleName
     */
    public String getScheduleName() {
        return scheduleName;
    }

    /**
     * @param scheduleName the scheduleName to set
     */
    public void setScheduleName(String scheduleName) {
        this.scheduleName = scheduleName;
    }

    /**
     * @return the claimReferenceId
     */
    public String getClaimReferenceId() {
        return claimReferenceId;
    }

    /**
     * @param claimReferenceId the claimReferenceId to set
     */
    public void setClaimReferenceId(String claimReferenceId) {
        this.claimReferenceId = claimReferenceId;
    }

    /**
     * @return the itemAmount
     */
    public BigDecimal getItemAmount() {
        return itemAmount;
    }

    /**
     * @param itemAmount the itemAmount to set
     */
    public void setItemAmount(BigDecimal itemAmount) {
        this.itemAmount = itemAmount;
    }

    /**
     * @return the amountReceived
     */
    public BigDecimal getAmountReceived() {
        return amountReceived;
    }

    /**
     * @param amountReceived the amountReceived to set
     */
    public void setAmountReceived(BigDecimal amountReceived) {
        this.amountReceived = amountReceived;
    }

    /**
     * @return the receivedDate
     */
    public String getReceivedDate() {
        return receivedDate;
    }

    /**
     * @param receivedDate the receivedDate to set
     */
    public void setReceivedDate(String receivedDate) {
        this.receivedDate = receivedDate;
    }

    /**
     * @return the reconciled
     */
    public boolean isReconciled() {
        return reconciled;
    }

    /**
     * @param reconciled the reconciled to set
     */
    public void setReconciled(boolean reconciled) {
        this.reconciled = reconciled;
    }

    /**
     * @return the comment
     */
    public String getComment() {
        return comment;
    }

    /**
     * @param comment the comment to set
     */
    public void setComment(String comment) {
        this.comment = comment;
    }
}
