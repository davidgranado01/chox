package idas.chox.service.reports.viewdata;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.util.DateHelper;

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
    private Integer outstandingStart;
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
    private BigDecimal daysVolOS;
    private Date oldestDate;
    private Integer oldestDays;
    private Date lastLoginDate;
    private BigDecimal timeInService;
    private Integer averageOutstanding;
    private Integer historicAverage;
    private Integer weeksInService;
    private Integer countClaimUnacknowledgedRouted;
    private Integer countClaimRejectionContested;
    private Integer countClaimUpdatedByEngineer;
    private Integer countInvoiceReferredToClaimsHandler;
    private Integer countInvoiceEscalatedToHandler;
    private Integer countContestedInvoiceReferredToInsurer;
    private Integer countInvoiceApprovedByBre;
    private Integer countAwaitingInvoicePayment;

    public static OwnerWorkflowLineItem getObject(Map data) {
        OwnerWorkflowLineItem result = new OwnerWorkflowLineItem();
        if (data.get("workgroup") == null) {
            result.setWorkgroup("");
        }
        else {
            result.setWorkgroup(data.get("workgroup").toString());
        }
        result.setName(data.get("name").toString());
        result.setId((Integer)data.get("id"));
        LOG.debug("Creating stats for: {}", result.getName());
        return result;
    }

    public void updateObject(Map data) {
      try {
        this.setProcessed(getIntegerValue(data.get("processed")));
        this.setOutstanding(getIntegerValue(data.get("outstanding")));
        this.setOutstandingStart(getIntegerValue(data.get("outstandingStart".toLowerCase())));
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
        BigDecimal a = (BigDecimal)data.get("daysVolOS".toLowerCase());
        LOG.debug("daysVolOS: A={}", a.toString());
        try {
            this.setDaysVolOS(new BigDecimal(outstanding).divide(a, 2, RoundingMode.HALF_UP));
        } catch (ArithmeticException ex) {
            this.setDaysVolOS(BigDecimal.ZERO);
        }
        if (data.get("oldestDate".toLowerCase()) != null) {
            LOG.debug("Getting oldest date from '{}'", data.get("oldestDate".toLowerCase()));
            this.setOldestDate(DateHelper.parseDBDateTime(data.get("oldestDate".toLowerCase()).toString()));
        }
        LOG.debug("oldestDate={}", this.getOldestDate());
        if (data.get("oldestDays".toLowerCase()) != null) {
            LOG.debug("Setting oldest days to '{}'", data.get("oldestDays".toLowerCase()).toString());
            this.setOldestDays(getIntegerValue(data.get("oldestDays".toLowerCase())));
        }
        LOG.debug("oldestDays set: {}", this.getOldestDays());
        if (data.get("averageOutstanding".toLowerCase()) != null) {
              this.setAverageOutstanding(getIntegerValue(data.get("averageOutstanding".toLowerCase())));
          }
        LOG.debug("averageOutstanding set: {}", this.getAverageOutstanding());
        if (data.get("historicAverage".toLowerCase()) != null) {
              this.setHistoricAverage(getIntegerValue(data.get("historicAverage".toLowerCase())));
          }
        LOG.debug("set historicAverage: {}", this.getHistoricAverage());
        this.setCountClaimUnacknowledgedRouted(getIntegerValue(data.get("countClaimUnacknowledgedRouted".toLowerCase())));
        this.setCountClaimRejectionContested(getIntegerValue(data.get("countClaimRejectionContested".toLowerCase())));
        this.setCountClaimUpdatedByEngineer(getIntegerValue(data.get("countClaimUpdatedByEngineer".toLowerCase())));
        this.setCountInvoiceReferredToClaimsHandler(getIntegerValue(data.get("countInvoiceReferredToClaimsHandler".toLowerCase())));
        this.setCountInvoiceEscalatedToHandler(getIntegerValue(data.get("countInvoiceEscalatedToHandler".toLowerCase())));
        this.setCountContestedInvoiceReferredToInsurer(getIntegerValue(data.get("countContestedInvoiceReferredToInsurer".toLowerCase())));
        this.setCountInvoiceApprovedByBre(getIntegerValue(data.get("countInvoiceApprovedByBre".toLowerCase())));
        this.setCountAwaitingInvoicePayment(getIntegerValue(data.get("countAwaitingInvoicePayment".toLowerCase())));
        Object lastLogin = data.get("lastLoginDate".toLowerCase());
        if (lastLogin != null && lastLogin.toString().length() > 0) {
            try {
                // NB. Rstrict date to 19 characters (i.e. remove milliseconds)
                this.setLastLoginDate(DateHelper.parseDBDateTime(lastLogin.toString().substring(0, 19)));
            } catch (Exception ex) {
                LOG.warn("Exception thrown converting lastLogin '{}' to date: {}", lastLogin.toString().substring(0, 19), ex.getMessage());
            }
        }
        try {
            this.setTimeInService(((BigDecimal)data.get("timeInService".toLowerCase())));
        } catch (Exception ex) {
                LOG.warn("Exception thrown converting timeInService '{}' to BigDecima/double: {}", data.get("timeInService".toLowerCase()), ex.getMessage());
        }
        this.setWeeksInService(getIntegerValue(data.get("weeksInService".toLowerCase())));
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

    public Integer getProcessed() {
        return processed;
    }

    public void setProcessed(Integer processed) {
        this.processed = processed;
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

    public double getOutstandingPercentage0_5() {
        return outstandingPercentage0_5;
    }

    public void setOutstandingPercentage0_5(double outstandingPercentage0_5) {
        this.outstandingPercentage0_5 = outstandingPercentage0_5;
    }

    public BigDecimal getTimeInService() {
        return timeInService;
    }

    public void setTimeInService(BigDecimal timeInService) {
        this.timeInService = timeInService;
    }

    public Integer getWeeksInService() {
        return weeksInService;
    }

    public void setWeeksInService(Integer weeksInService) {
        this.weeksInService = weeksInService;
    }

    public Integer getAverageOutstanding() {
        return averageOutstanding;
    }

    public void setAverageOutstanding(Integer averageOutstanding) {
        this.averageOutstanding = averageOutstanding;
    }

    public Integer getCountAwaitingInvoicePayment() {
        return countAwaitingInvoicePayment;
    }

    public void setCountAwaitingInvoicePayment(Integer countAwaitingInvoicePayment) {
        this.countAwaitingInvoicePayment = countAwaitingInvoicePayment;
    }

    public Integer getCountClaimRejectionContested() {
        return countClaimRejectionContested;
    }

    public void setCountClaimRejectionContested(Integer countClaimRejectionContested) {
        this.countClaimRejectionContested = countClaimRejectionContested;
    }

    public Integer getCountClaimUnacknowledgedRouted() {
        return countClaimUnacknowledgedRouted;
    }

    public void setCountClaimUnacknowledgedRouted(Integer countClaimUnacknowledgedRouted) {
        this.countClaimUnacknowledgedRouted = countClaimUnacknowledgedRouted;
    }

    public Integer getCountClaimUpdatedByEngineer() {
        return countClaimUpdatedByEngineer;
    }

    public void setCountClaimUpdatedByEngineer(Integer countClaimUpdatedByEngineer) {
        this.countClaimUpdatedByEngineer = countClaimUpdatedByEngineer;
    }

    public Integer getCountContestedInvoiceReferredToInsurer() {
        return countContestedInvoiceReferredToInsurer;
    }

    public void setCountContestedInvoiceReferredToInsurer(Integer countContestedInvoiceReferredToInsurer) {
        this.countContestedInvoiceReferredToInsurer = countContestedInvoiceReferredToInsurer;
    }

    public Integer getCountInvoiceApprovedByBre() {
        return countInvoiceApprovedByBre;
    }

    public void setCountInvoiceApprovedByBre(Integer countInvoiceApprovedByBre) {
        this.countInvoiceApprovedByBre = countInvoiceApprovedByBre;
    }

    public Integer getCountInvoiceEscalatedToHandler() {
        return countInvoiceEscalatedToHandler;
    }

    public void setCountInvoiceEscalatedToHandler(Integer countInvoiceEscalatedToHandler) {
        this.countInvoiceEscalatedToHandler = countInvoiceEscalatedToHandler;
    }

    public Integer getCountInvoiceReferredToClaimsHandler() {
        return countInvoiceReferredToClaimsHandler;
    }

    public void setCountInvoiceReferredToClaimsHandler(Integer countInvoiceReferredToClaimsHandler) {
        this.countInvoiceReferredToClaimsHandler = countInvoiceReferredToClaimsHandler;
    }

    public Integer getHistoricAverage() {
        return historicAverage;
    }

    public void setHistoricAverage(Integer historicAverage) {
        this.historicAverage = historicAverage;
    }

    public Integer getOldestDays() {
        return oldestDays;
    }

    public void setOldestDays(Integer oldestDays) {
        this.oldestDays = oldestDays;
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
}
