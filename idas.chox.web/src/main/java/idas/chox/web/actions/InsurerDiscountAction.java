/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.web.actions;

import idas.chox.core.model.Chorganisation;
import idas.chox.core.model.InsurerDiscount;
import idas.chox.core.model.LookupItem;
import idas.chox.core.services.InsurerDiscountService;
import idas.chox.core.services.LookupService;
import idas.chox.web.viewdata.DiscountViewData;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.annotation.Secured;


public class InsurerDiscountAction extends BaseAction {
    
    private static final Logger LOG = LoggerFactory.getLogger(InsurerDiscountAction.class);
    private LookupService lookupService;
    private InsurerDiscountService insurerDiscountService;
    private List<Chorganisation> suppliers;
    private int insurerId;
    private int choId;
    private Date dateFrom;
    private Date dateTo;
    private String jsonData;
    private int discountId;
    private BigDecimal discountAmount;

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public int getDiscountId() {
        return discountId;
    }

    public void setDiscountId(int discountId) {
        this.discountId = discountId;
    }

    public String getJsonData() {
        return jsonData;
    }

    public void setJsonData(String jsonData) {
        this.jsonData = jsonData;
    }

    public Date getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    public Date getDateTo() {
        return dateTo;
    }

    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }

    public int getChoId() {
        return choId;
    }

    public void setChoId(int choid) {
        this.choId = choid;
    }

    public void setInsurerDiscountService(InsurerDiscountService insurerDiscountService) {
        this.insurerDiscountService = insurerDiscountService;
    }
    
    public int getInsurerId() {
        return insurerId;
    }

    public void setInsurerId(int insurerId) {
        this.insurerId = insurerId;
    }
    
    public void setLookupService(LookupService lookupService) {
        this.lookupService = lookupService;
    }
    
    @Override
    public String execute() throws Exception {
        
            LOG.debug("called in execute method");
            return SUCCESS;
    }
    
    public List<Chorganisation> getSuppliers() {
        if (suppliers == null) {
            if(getIsAdmin()){
               suppliers = this.lookupService.getSuppliers(this.insurerId); 
            }else if(getIsInsurer()){
               suppliers = this.lookupService.getSuppliers();  
            }
        }
        return suppliers;
    }
    
    public String getSuppliersJsonString() {
        List<LookupItem> luItems = new ArrayList<LookupItem>(getSuppliers().size());
        for (Chorganisation supplier : suppliers) {
            luItems.add(new LookupItem(supplier.getId().toString(), supplier.getName()));
        }
        return "{totalCount:" + luItems.size() + ", results:" + JSONArray.fromObject(luItems).toString() + "}";
    }
    
    @Secured ({"ROLE_CHOX_ADMIN","ROLE_INS_MNG"})
    public String addDiscount() throws Exception {
        if(getIsInsurer()){
            insurerId = getAuthenticatedUser().getInsurer().getId();
        }
        if(discountAmount.compareTo(BigDecimal.ZERO)==1){
           discountAmount = discountAmount.multiply(BigDecimal.valueOf(-1)); 
        }
        try{
        Map result = insurerDiscountService.addDiscount(insurerId, choId, dateFrom, dateTo, discountAmount);
        JSONObject jsonObject = JSONObject.fromObject(result);
        setJsonData(jsonObject.toString());
        LOG.debug("Returning json string: '{}'", jsonObject.toString());
        }catch(Exception ex){
            LOG.error("Exception in addDiscount(): {}", ex.getMessage());
            throw ex;
        }
        return SUCCESS;
    }
    
    @Secured ({"ROLE_CHOX_ADMIN","ROLE_INS_MNG"})
    public String listDiscountGridData() {
        List<DiscountViewData> viewList = new ArrayList<DiscountViewData>();
        List<InsurerDiscount> discountList = new ArrayList();
        
        discountList = insurerDiscountService.getInsurerDiscount(choId, insurerId);
        for (Iterator iterator = discountList.iterator(); iterator.hasNext();) {
            InsurerDiscount object = (InsurerDiscount) iterator.next();
            DiscountViewData dvd = new DiscountViewData(object);
            LOG.debug(dvd.toString());
            viewList.add(dvd);
        }
        Map<String, Object> context = new HashMap<String, Object>();

        //String count = "totalCount:"+ viewList.size()+ ",";
        setJsonData("{totalCount:"+ viewList.size()+", results:" + JSONArray.fromObject(viewList).toString() + "}");
        return SUCCESS;
    } 
    
    @Secured ({"ROLE_CHOX_ADMIN","ROLE_INS_MNG"})
    public String deleteInsurerDiscount() {
        try {
            LOG.debug("Delete insurer discount");
            InsurerDiscount insurerDiscount = insurerDiscountService.getInsurerDiscount(discountId);
            Map hm = insurerDiscountService.deleteInsurerDiscount(insurerDiscount);
            JSONObject jsonObject = JSONObject.fromObject(hm);
            setJsonData(jsonObject.toString());
            LOG.debug("Back from delete schedule");
        } catch (RuntimeException re) {

            LOG.error("Exception in deleteBill(): {}", re.getMessage());
            throw re;
        }
        return SUCCESS;
    } 
}
