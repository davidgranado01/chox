/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package idas.chox.core.search;
import java.util.List;

/**
 *
 * @author Emmanuel
 */
public class SearchResult {
    private

    List result;
    private int totalCount;
    
    public SearchResult(List result,int totalCount)
 {
        this.result = result;
        this.totalCount = totalCount;

    }

    public List getResult() {
        return result;
    }

    public void setResult(List result) {
        this.result = result;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }


}
