

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


public class OwnerPerformanceLineItem {


    private static final Logger LOG = LoggerFactory.getLogger(OwnerPerformanceLineItem.class);

    private String workgroup;
    private String name;
    private Integer id;
    private Integer taskProcessedBetweenGivenPeriod;
    private double taskCompleted0_2days;
    private double taskCompleted2_5days;
    private double taskCompleted5_15days;
    private double taskCompletedAfter15days;
    private double avgInvoicePaymentDay;
    private double averageDaysToProcess;

    public double getAverageDaysToProcess() {
        return averageDaysToProcess;
    }

    public void setAverageDaysToProcess(double averageDaysToProcess) {
        this.averageDaysToProcess = averageDaysToProcess;
    }

    public double getAvgInvoicePaymentDay() {
        return avgInvoicePaymentDay;
    }

    public void setAvgInvoicePaymentDay(double avgInvoicePaymentDay) {
        this.avgInvoicePaymentDay = avgInvoicePaymentDay;
    }

    public double getTaskCompleted0_2days() {
        return taskCompleted0_2days;
    }

    public void setTaskCompleted0_2days(double taskCompleted0_2days) {
        this.taskCompleted0_2days = taskCompleted0_2days;
    }

    public double getTaskCompleted2_5days() {
        return taskCompleted2_5days;
    }

    public void setTaskCompleted2_5days(double taskCompleted2_5days) {
        this.taskCompleted2_5days = taskCompleted2_5days;
    }

    public double getTaskCompleted5_15days() {
        return taskCompleted5_15days;
    }

    public void setTaskCompleted5_15days(double taskCompleted5_15days) {
        this.taskCompleted5_15days = taskCompleted5_15days;
    }

    public double getTaskCompletedAfter15days() {
        return taskCompletedAfter15days;
    }

    public void setTaskCompletedAfter15days(double taskCompletedAfter15days) {
        this.taskCompletedAfter15days = taskCompletedAfter15days;
    }

    public Integer getTaskProcessedBetweenGivenPeriod() {
        return taskProcessedBetweenGivenPeriod;
    }

    public void setTaskProcessedBetweenGivenPeriod(Integer taskProcessedBetweenGivenPeriod) {
        this.taskProcessedBetweenGivenPeriod = taskProcessedBetweenGivenPeriod;
    }

    

     public static OwnerPerformanceLineItem getObject(Map data) {

        OwnerPerformanceLineItem result = new OwnerPerformanceLineItem();
        if (data.get("workgroup") == null)
            result.setWorkgroup("");
        else
            result.setWorkgroup(data.get("workgroup").toString());
        result.setName(data.get("name").toString());
        result.setId((Integer)data.get("id"));
        LOG.debug("Creating stats for: {}", result.getName());
        return result;
    }

     public String getWorkgroup() {
        return workgroup;
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


    public void updateObject(Map data) {

        this.setTaskProcessedBetweenGivenPeriod(getIntegerValue(data.get("taskprocessedbetweengivenperiod")));

        if (taskProcessedBetweenGivenPeriod != 0) {

            this.setTaskCompleted0_2days((getDoubleValue(data.get("taskcompleted0_2days")) * 1.0) / taskProcessedBetweenGivenPeriod);
            this.setTaskCompleted2_5days((getDoubleValue(data.get("taskcompleted2_5days")) * 1.0) / taskProcessedBetweenGivenPeriod);
            this.setTaskCompleted5_15days((getDoubleValue(data.get("taskcompleted5_15days")) * 1.0) / taskProcessedBetweenGivenPeriod);
            this.setTaskCompletedAfter15days((getDoubleValue(data.get("taskcompletedafter15days")) * 1.0) / taskProcessedBetweenGivenPeriod);

        } else {
            taskCompleted0_2days=0.0;
            taskCompleted2_5days=0.0;
            taskCompleted5_15days=0.0;
            taskCompletedAfter15days=0.0;
        }

        if (data.get("avginvoicepaymentday") != null) {
            this.setAvgInvoicePaymentDay(getDoubleValue(data.get("avginvoicepaymentday")));
        } else {
            this.setAvgInvoicePaymentDay(0);
        }
        if (data.get("averagedaystoprocess") != null) {
            this.setAverageDaysToProcess(getDoubleValue(data.get("averagedaystoprocess")));
        } else {
            this.setAverageDaysToProcess(0);
        }

        LOG.debug("  setTaskProcessedBetweenGivenPeriod : {}", taskProcessedBetweenGivenPeriod);
        LOG.debug("  taskcompleted0_2days: {}", taskCompleted0_2days);
        LOG.debug("  taskcompleted2_5days: {}", taskCompleted2_5days);
        LOG.debug("  taskcompleted5_15days: {}", taskCompleted5_15days);
        LOG.debug("  taskcompletedafter15days: {}", taskCompletedAfter15days);
        LOG.debug("  avginvoicepaymentday: {}", averageDaysToProcess);
        LOG.debug("  averagedaystoprocess: {}", avgInvoicePaymentDay);

    }

    private static Integer getIntegerValue(Object v) {
        LOG.debug("inside IntegerValue method found class for the sent value is: {}", v.getClass());
        if (v.getClass().equals(Integer.class)) {
            return (Integer) v;
        } else if (v.getClass().equals(BigInteger.class)) {
            LOG.debug("return value {}", ((BigInteger) v).intValue());
            return ((BigInteger) v).intValue();
        } else {
            LOG.debug("no class match found returning 0");
            return 0;
        }
    }

    private static double getDoubleValue(Object v) {
        LOG.debug("inside getDoubleValue method found class for the sent value is: {}", v.getClass());

        if (v.getClass().equals(BigDecimal.class)) {
            LOG.debug("return value {}", ((BigDecimal) v).doubleValue());
            return ((BigDecimal) v).doubleValue();
        } else if (v.getClass().equals(Integer.class)) {
            return (Integer) v;
        } else if (v.getClass().equals(BigInteger.class)) {
            LOG.debug("return value {}", ((BigInteger) v).intValue());
            return ((BigInteger) v).intValue();
        } else {
            LOG.debug("no class match found returning 0");
            return 0;
        }
    }
}


