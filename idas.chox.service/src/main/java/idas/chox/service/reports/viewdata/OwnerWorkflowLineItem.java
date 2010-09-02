/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package idas.chox.service.reports.viewdata;

import idas.chox.core.util.DateHelper;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author John
 */
public class OwnerWorkflowLineItem {
    private static final Logger LOG = LoggerFactory.getLogger(OwnerWorkflowLineItem.class);
    private String workgroup;
    private String name;
    private Integer id;
    private Integer outstanding;
    private Integer outstanding0_5;
    private Integer outstanding5_15;
    private Integer outstanding15_;
    private double outstandingPercentage0_5;
    private double outstandingPercentage5_15;
    private double outstandingPercentage15_;
    private BigDecimal daysVolOS;
    private Date oldestDate;
    private Date lastLoginDate;
    private double timeInService;
    private Integer weeksInService;

    public static OwnerWorkflowLineItem getObject(Map data) {
        OwnerWorkflowLineItem result = new OwnerWorkflowLineItem();
        if (data.get("workgroup") == null)
            result.setWorkgroup("");
        else
            result.setWorkgroup(data.get("workgroup").toString());
        result.setName(data.get("name").toString());
        result.setId((Integer)data.get("id"));
        LOG.debug("Creating stats for: {}", result.getName());
        return result;
    }

    public void updateObject(Map data) {
        Iterator it = data.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry)it.next();
            LOG.debug("{} = {}", pairs.getKey(), pairs.getValue());
        }

        this.setOutstanding(getIntegerValue(data.get("outstanding")));
        this.setOutstanding0_5(getIntegerValue(data.get("outstanding0_5")));
        this.setOutstanding5_15(getIntegerValue(data.get("outstanding5_15")));
        this.setOutstanding15_(getIntegerValue(data.get("outstanding15_")));
        // Derive % column values
        if (outstanding == 0) {
            this.setOutstandingPercentage0_5(0.0);
            this.setOutstandingPercentage5_15(0.0);
            this.setOutstandingPercentage15_(0.0);
        }
        else {
            this.setOutstandingPercentage0_5((outstanding0_5*1.0/outstanding));
            this.setOutstandingPercentage5_15((outstanding5_15*1.0/outstanding));
            this.setOutstandingPercentage15_((outstanding15_*1.0/outstanding));
        }
        BigDecimal a = (BigDecimal)data.get("daysColOS".toLowerCase());
        LOG.debug("daysColOS: A={}", a.toString());
        try {
            this.setDaysVolOS(new BigDecimal(outstanding).divide(a, 2, RoundingMode.HALF_UP));
        } catch (ArithmeticException ex) {
            this.setDaysVolOS(BigDecimal.ZERO);
        }
        if (data.get("oldestDate".toLowerCase()) != null)
            this.setOldestDate(DateHelper.ParseDBDateTime(data.get("oldestDate".toLowerCase()).toString()));
        if (data.get("lastLoginDate".toLowerCase()) != null)
            this.setLastLoginDate(DateHelper.ParseDBDateTime(data.get("lastLoginDate".toLowerCase()).toString()));
        this.setTimeInService(((BigDecimal)data.get("timeInService".toLowerCase())).doubleValue());
        this.setWeeksInService(getIntegerValue(data.get("weeksInService".toLowerCase())));
    }

    private static Integer getIntegerValue(Object v) {
        if (v.getClass().equals(Integer.class)) {
            return (Integer) v;
        } else if (v.getClass().equals(BigInteger.class)) {
            return ((BigInteger) v).intValue();
        } else {
            return 0;
        }
    }
    
    public String getWorkgroup() {
        return workgroup;
    }

    public Date getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(Date lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    public void setWorkgroup(String workgroup) {
        this.workgroup = workgroup;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getDaysVolOS() {
        return daysVolOS;
    }

    public void setDaysVolOS(BigDecimal daysVolOS) {
        this.daysVolOS = daysVolOS;
    }

    public Date getOldestDate() {
        return oldestDate;
    }

    public void setOldestDate(Date oldestDate) {
        this.oldestDate = oldestDate;
    }

    public Integer getOutstanding() {
        return outstanding;
    }

    public void setOutstanding(Integer outstanding) {
        this.outstanding = outstanding;
    }

    public Integer getOutstanding0_5() {
        return outstanding0_5;
    }

    public void setOutstanding0_5(Integer outstanding0_5) {
        this.outstanding0_5 = outstanding0_5;
    }

    public Integer getOutstanding15_() {
        return outstanding15_;
    }

    public void setOutstanding15_(Integer outstanding15_) {
        this.outstanding15_ = outstanding15_;
    }

    public Integer getOutstanding5_15() {
        return outstanding5_15;
    }

    public void setOutstanding5_15(Integer outstanding5_15) {
        this.outstanding5_15 = outstanding5_15;
    }

    public double getOutstandingPercentage0_5() {
        return outstandingPercentage0_5;
    }

    public void setOutstandingPercentage0_5(double outstandingPercentage0_5) {
        this.outstandingPercentage0_5 = outstandingPercentage0_5;
    }

    public double getOutstandingPercentage15_() {
        return outstandingPercentage15_;
    }

    public void setOutstandingPercentage15_(double outstandingPercentage15_) {
        this.outstandingPercentage15_ = outstandingPercentage15_;
    }

    public double getOutstandingPercentage5_15() {
        return outstandingPercentage5_15;
    }

    public void setOutstandingPercentage5_15(double outstandingPercentage5_15) {
        this.outstandingPercentage5_15 = outstandingPercentage5_15;
    }

    public double getTimeInService() {
        return timeInService;
    }

    public void setTimeInService(double timeInService) {
        this.timeInService = timeInService;
    }

    public Integer getWeeksInService() {
        return weeksInService;
    }

    public void setWeeksInService(Integer weeksInService) {
        this.weeksInService = weeksInService;
    }
}
