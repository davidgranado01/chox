/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.services;

import chox.model.Claim;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Emmanuel
 */
public class SearchResult {
    private

    List<HashMap> result;
    private int totalCount;
    
    public SearchResult(List<HashMap> result,int totalCount)
 {
        this.result = result;
        this.totalCount = totalCount;

    }

    public List<HashMap> getResult() {
        return result;
    }

    public void setResult(List<HashMap> result) {
        this.result = result;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }


}
