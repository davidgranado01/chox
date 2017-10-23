package idas.chox.web.actions;

import java.util.HashMap;
import java.util.Map;

import idas.chox.core.model.LiabilityStatus;

/**
 *
 * @author abrar
 */
public class LiabilityStatusDropDownAction extends BaseAction{

    private Map dropDownMap;

    @Override
    public String execute() throws Exception {
        return SUCCESS;
    }

    /**
     * @return the dropDownList
     */
    public Map getDropDownMap() {
        if ( dropDownMap == null ){
            dropDownMap = new HashMap();
            LiabilityStatus[] arr = LiabilityStatus.values();
            for (LiabilityStatus arr1 : arr) {
                dropDownMap.put(arr1.getLiablityValue(), arr1);
            }            
        }
        return dropDownMap;
    }

    /**
     * @param dropDownMap the dropDownList to set
     */
    public void setDropDownMap(Map dropDownMap) {
        this.dropDownMap = dropDownMap;
    }

}
