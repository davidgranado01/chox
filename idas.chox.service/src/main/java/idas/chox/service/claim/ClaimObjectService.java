/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package idas.chox.service.claim;

import idas.chox.core.model.LiabilityStatus;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author abrar
 */
public class ClaimObjectService {

    private Map dropDownMap;

    /**
     * @return the dropDownList
     */
    public Map getLiabilityStatusMap() {
        if ( dropDownMap == null ){
            dropDownMap = new HashMap();
            LiabilityStatus[] arr = LiabilityStatus.values();
            for (int i = 1; i < arr.length; i++) {
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
