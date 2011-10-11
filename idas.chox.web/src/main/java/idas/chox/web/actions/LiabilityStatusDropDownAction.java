package idas.chox.web.actions;

import idas.chox.core.model.LiabilityStatus;
import java.util.HashMap;
import java.util.Map;

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
            for (int i = 0; i < arr.length; i++) {
                dropDownMap.put(arr[i].ordinal(),arr[i]);
            }            
        }
        return dropDownMap;
    }

    /**
     * @param dropDownList the dropDownList to set
     */
    public void setDropDownMap(Map dropDownMap) {
        this.dropDownMap = dropDownMap;
    }

}
