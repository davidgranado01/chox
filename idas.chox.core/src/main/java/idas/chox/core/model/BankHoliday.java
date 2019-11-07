package idas.chox.core.model;

import java.io.Serializable;
import java.util.Date;

public class BankHoliday implements Serializable {
    private Integer id;
    Date bankHoliday;

    public Date getBankHoliday() {
        return bankHoliday;
    }

    public void setBankHoliday(Date bankHoliday) {
        this.bankHoliday = bankHoliday;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
}
