package idas.chox.service.reports.viewdata;

import java.math.BigDecimal;
import java.util.List;

public class BreInvoiceApprovalDisputedRoRData {

    private String name;
    private Integer id;
    private BigDecimal commlineData;
    private List<BigDecimal> lineData;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getCommlineData() {
        return commlineData;
    }

    public void setCommlineData(BigDecimal commlineData) {
        this.commlineData = commlineData;
    }

    public List<BigDecimal> getLineData() {
        return lineData;
    }

    public void setLineData(List<BigDecimal> lineData) {
        this.lineData = lineData;
    }

}
