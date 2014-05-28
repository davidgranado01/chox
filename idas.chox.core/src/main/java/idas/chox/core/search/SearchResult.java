package idas.chox.core.search;

import java.util.List;

/**
 *
 * @author Emmanuel
 */
public class SearchResult {

    private List result;
    private int totalCount;
    private String colorCode;

    public SearchResult(List result, int totalCount, String colorCode) {
        this.result = result;
        this.totalCount = totalCount;
        this.colorCode = colorCode;

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

    public String getColorCode() {
        return colorCode;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }
}
