package idas.chox.service.reports.viewdata;

import java.math.BigInteger;
import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.util.DateHelper;

/**
 *
 * @author John
 */
public class ClaimStatusWorkflowLineItem {
    private static final Logger LOG = LoggerFactory.getLogger(ClaimStatusWorkflowLineItem.class);
    private String status;
    private Integer outstandingStart;
    private Integer outstanding;
    private Integer processed;
    private Integer outstanding0_5;
    private Integer outstanding5_10;
    private Integer outstanding10_15;
    private Integer outstanding15_20;
    private Integer outstanding20_25;
    private Integer outstanding25_30;
    private Integer outstanding30_;
    private double outstandingPercentage0_5;
    private double outstandingPercentage5_10;
    private double outstandingPercentage10_15;
    private double outstandingPercentage15_20;
    private double outstandingPercentage20_25;
    private double outstandingPercentage25_30;
    private double outstandingPercentage30_;
    private Date oldestDate;
    private Integer oldestDays;
    private Integer averageOutstanding;
    private Integer historicAverage;
    private String workgroup;
    private Integer workgroupId;


    public void updateObject(Map data) {
      try {
        this.setProcessed(getIntegerValue(data.get("processed")));
        this.setOutstandingStart(getIntegerValue(data.get("outstandingStart".toLowerCase())));
        this.setOutstanding(getIntegerValue(data.get("outstanding")));
        this.setOutstanding0_5(getIntegerValue(data.get("outstanding0_5")));
        this.setOutstanding5_10(getIntegerValue(data.get("outstanding5_10")));
        this.setOutstanding10_15(getIntegerValue(data.get("outstanding10_15")));
        this.setOutstanding15_20(getIntegerValue(data.get("outstanding15_20")));
        this.setOutstanding20_25(getIntegerValue(data.get("outstanding20_25")));
        this.setOutstanding25_30(getIntegerValue(data.get("outstanding25_30")));
        this.setOutstanding30_(getIntegerValue(data.get("outstanding30_")));

        // Derive % column values
        if (outstanding == 0) {
            this.setOutstandingPercentage0_5(0.0);
            this.setOutstandingPercentage5_10(0.0);
            this.setOutstandingPercentage10_15(0.0);
            this.setOutstandingPercentage15_20(0.0);
            this.setOutstandingPercentage20_25(0.0);
            this.setOutstandingPercentage25_30(0.0);
            this.setOutstandingPercentage30_(0.0);
        }
        else {
            this.setOutstandingPercentage0_5((outstanding0_5*1.0/outstanding));
            this.setOutstandingPercentage5_10((outstanding5_10*1.0/outstanding));
            this.setOutstandingPercentage10_15((outstanding10_15*1.0/outstanding));
            this.setOutstandingPercentage15_20((outstanding15_20*1.0/outstanding));
            this.setOutstandingPercentage20_25((outstanding20_25*1.0/outstanding));
            this.setOutstandingPercentage25_30((outstanding25_30*1.0/outstanding));
            this.setOutstandingPercentage30_((outstanding30_*1.0/outstanding));
        }

        if (data.get("oldestDate".toLowerCase()) != null) {
            this.setOldestDate(DateHelper.ParseDBDateTime(data.get("oldestDate".toLowerCase()).toString()));
        }
        if (data.get("oldestDays".toLowerCase()) != null) {
            this.setOldestDays(getIntegerValue(data.get("oldestDays".toLowerCase())));
        }
        if (data.get("averageOutstanding".toLowerCase()) != null) {
              this.setAverageOutstanding(getIntegerValue(data.get("averageOutstanding".toLowerCase())));
        }
        if (data.get("historicAverage".toLowerCase()) != null) {
              this.setHistoricAverage(getIntegerValue(data.get("historicAverage".toLowerCase())));
        }
      } catch (Exception ex) {
          LOG.error("Exception thrown: {}", ex.getMessage());
      }
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

    public Integer getWorkgroupId() {
        return workgroupId;
    }

    public void setWorkgroupId(Integer workgroupId) {
        this.workgroupId = workgroupId;
    }

    public String getWorkgroup() {
        return workgroup;
    }

    public void setWorkgroup(String workgroup) {
        this.workgroup = workgroup;
    }

    public Integer getAverageOutstanding() {
        return averageOutstanding;
    }

    public void setAverageOutstanding(Integer averageOutstanding) {
        this.averageOutstanding = averageOutstanding;
    }

    public Integer getHistoricAverage() {
        return historicAverage;
    }

    public void setHistoricAverage(Integer historicAverage) {
        this.historicAverage = historicAverage;
    }

    public Date getOldestDate() {
        return oldestDate;
    }

    public void setOldestDate(Date oldestDate) {
        this.oldestDate = oldestDate;
    }

    public Integer getOldestDays() {
        return oldestDays;
    }

    public void setOldestDays(Integer oldestDays) {
        this.oldestDays = oldestDays;
    }

    public Integer getOutstanding() {
        return outstanding;
    }

    public void setOutstanding(Integer outstanding) {
        this.outstanding = outstanding;
    }

    public Integer getOutstandingStart() {
        return outstandingStart;
    }

    public void setOutstandingStart(Integer outstandingStart) {
        this.outstandingStart = outstandingStart;
    }

    public Integer getOutstanding0_5() {
        return outstanding0_5;
    }

    public void setOutstanding0_5(Integer outstanding0_5) {
        this.outstanding0_5 = outstanding0_5;
    }

    public Integer getOutstanding10_15() {
        return outstanding10_15;
    }

    public void setOutstanding10_15(Integer outstanding10_15) {
        this.outstanding10_15 = outstanding10_15;
    }

    public Integer getOutstanding15_20() {
        return outstanding15_20;
    }

    public void setOutstanding15_20(Integer outstanding15_20) {
        this.outstanding15_20 = outstanding15_20;
    }

    public Integer getOutstanding20_25() {
        return outstanding20_25;
    }

    public void setOutstanding20_25(Integer outstanding20_25) {
        this.outstanding20_25 = outstanding20_25;
    }

    public Integer getOutstanding25_30() {
        return outstanding25_30;
    }

    public void setOutstanding25_30(Integer outstanding25_30) {
        this.outstanding25_30 = outstanding25_30;
    }

    public Integer getOutstanding30_() {
        return outstanding30_;
    }

    public void setOutstanding30_(Integer outstanding30_) {
        this.outstanding30_ = outstanding30_;
    }

    public Integer getOutstanding5_10() {
        return outstanding5_10;
    }

    public void setOutstanding5_10(Integer outstanding5_10) {
        this.outstanding5_10 = outstanding5_10;
    }

    public double getOutstandingPercentage0_5() {
        return outstandingPercentage0_5;
    }

    public void setOutstandingPercentage0_5(double outstandingPercentage0_5) {
        this.outstandingPercentage0_5 = outstandingPercentage0_5;
    }

    public double getOutstandingPercentage10_15() {
        return outstandingPercentage10_15;
    }

    public void setOutstandingPercentage10_15(double outstandingPercentage10_15) {
        this.outstandingPercentage10_15 = outstandingPercentage10_15;
    }

    public double getOutstandingPercentage15_20() {
        return outstandingPercentage15_20;
    }

    public void setOutstandingPercentage15_20(double outstandingPercentage15_20) {
        this.outstandingPercentage15_20 = outstandingPercentage15_20;
    }

    public double getOutstandingPercentage20_25() {
        return outstandingPercentage20_25;
    }

    public void setOutstandingPercentage20_25(double outstandingPercentage20_25) {
        this.outstandingPercentage20_25 = outstandingPercentage20_25;
    }

    public double getOutstandingPercentage25_30() {
        return outstandingPercentage25_30;
    }

    public void setOutstandingPercentage25_30(double outstandingPercentage25_30) {
        this.outstandingPercentage25_30 = outstandingPercentage25_30;
    }

    public double getOutstandingPercentage30_() {
        return outstandingPercentage30_;
    }

    public void setOutstandingPercentage30_(double outstandingPercentage30_) {
        this.outstandingPercentage30_ = outstandingPercentage30_;
    }

    public double getOutstandingPercentage5_10() {
        return outstandingPercentage5_10;
    }

    public void setOutstandingPercentage5_10(double outstandingPercentage5_10) {
        this.outstandingPercentage5_10 = outstandingPercentage5_10;
    }

    public Integer getProcessed() {
        return processed;
    }

    public void setProcessed(Integer processed) {
        this.processed = processed;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
