package idas.chox.service.workflow.activities;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringEscapeUtils;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.model.Claim;
import idas.chox.core.model.Task;
import idas.chox.core.model.WebUserRole;
import idas.chox.core.services.TaskService;
import idas.chox.core.services.UserService;
import idas.chox.core.util.DateHelper;


/**
 *
 * @author John
 */
public class TlTaskCreation extends BaseActivity {
    static final Logger LOG = LoggerFactory.getLogger(TlTaskCreation.class);
    private TaskService taskService;
    private UserService userService;
    private String imsReference;
    private String registrationNumber;
    private String make;
    private String model;
    private String chassisNumber;
    private String preAccidentValue;
    private String salvageAmount;
    private String salvageCategory;
    private String amountToPay;
    private String thirdPartyName;
    private String thirdPartyReg;
    private String thirdPartyClaimNumber;
    private String thirdPartyAgentName;
    private String title;
    private String driverFirstName;
    private String driverLastName;
    private String payee;
    private String line1;
    private String line2;
    private String town;
    private String postcode;
    private String vehicleStatus;
    private String area1Severity;
    private String area1Damage;
    private String area2Severity;
    private String area2Damage;
    private String totalLossDate;
    
    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public String getImsReference() {
        return imsReference;
    }

    public void setImsReference(String imsReference) {
        this.imsReference = imsReference == null ? null : imsReference.trim().replaceAll("\\s+", " ");
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber == null ? null : registrationNumber.trim().replaceAll("\\s+", " ");
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make == null ? null : make.trim().replaceAll("\\s+", " ");
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model == null ? null : model.trim().replaceAll("\\s+", " ");
    }

    public String getChassisNumber() {
        return chassisNumber;
    }

    public void setChassisNumber(String chassisNumber) {
        this.chassisNumber = chassisNumber == null ? null : chassisNumber.trim().replaceAll("\\s+", " ");
    }

    public String getPreAccidentValue() {
        return preAccidentValue;
    }

    public void setPreAccidentValue(String preAccidentValue) {
        this.preAccidentValue = preAccidentValue == null ? null : preAccidentValue.trim().replaceAll("\\s+", " ");
    }

    public String getSalvageAmmount() {
        return salvageAmount;
    }

    public void setSalvageAmmount(String salvageAmmount) {
        this.salvageAmount = salvageAmmount == null ? null : salvageAmmount.trim().replaceAll("\\s+", " ");
    }

    public String getSalvageCategory() {
        return salvageCategory;
    }

    public void setSalvageCategory(String salvageCategory) {
        this.salvageCategory = salvageCategory == null ? null : salvageCategory.trim().replaceAll("\\s+", " ");
    }

    public String getAmountToPay() {
        return amountToPay;
    }

    public void setAmountToPay(String amountToPay) {
        this.amountToPay = amountToPay == null ? null : amountToPay.trim().replaceAll("\\s+", " ");
    }

    public String getThirdPartyName() {
        return thirdPartyName;
    }

    public void setThirdPartyName(String thirdPartyName) {
        this.thirdPartyName = thirdPartyName == null ? null : thirdPartyName.trim().replaceAll("\\s+", " ");
    }

    public String getThirdPartyReg() {
        return thirdPartyReg;
    }

    public void setThirdPartyReg(String thirdPartyReg) {
        this.thirdPartyReg = thirdPartyReg == null ? null : thirdPartyReg.trim().replaceAll("\\s+", " ");
    }

    public String getThirdPartyClaimNumber() {
        return thirdPartyClaimNumber;
    }

    public void setThirdPartyClaimNumber(String thirdPartyClaimNumber) {
        this.thirdPartyClaimNumber = thirdPartyClaimNumber == null ? null : thirdPartyClaimNumber.trim().replaceAll("\\s+", " ");
    }

    public String getThirdPartyAgentName() {
        return thirdPartyAgentName;
    }

    public void setThirdPartyAgentName(String thirdPartyAgentName) {
        this.thirdPartyAgentName = thirdPartyAgentName == null ? null : thirdPartyAgentName.trim().replaceAll("\\s+", " ");
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim().replaceAll("\\s+", " ");
    }

    public String getDriverFirstName() {
        return driverFirstName;
    }

    public void setDriverFirstName(String driverFirstName) {
        this.driverFirstName = driverFirstName == null ? null : driverFirstName.trim().replaceAll("\\s+", " ");
    }

    public String getDriverLastName() {
        return driverLastName;
    }

    public void setDriverLastName(String driverLastName) {
        this.driverLastName = driverLastName == null ? null : driverLastName.trim().replaceAll("\\s+", " ");
    }

    public String getPayee() {
        return payee;
    }

    public void setPayee(String payee) {
        this.payee = payee == null ? null : payee.trim().replaceAll("\\s+", " ");
    }

    public String getLine1() {
        return line1;
    }

    public void setLine1(String line1) {
        this.line1 = line1 == null ? null : line1.trim().replaceAll("\\s+", " ");
    }

    public String getLine2() {
        return line2;
    }

    public void setLine2(String line2) {
        this.line2 = line2 == null ? null : line2.trim().replaceAll("\\s+", " ");
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town == null ? null : town.trim().replaceAll("\\s+", " ");
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode == null ? null : postcode.trim().replaceAll("\\s+", " ");
    }

    public String getVehicleStatus() {
        return vehicleStatus;
    }

    public void setVehicleStatus(String vehicleStatus) {
        this.vehicleStatus = vehicleStatus == null ? null : vehicleStatus.trim().replaceAll("\\s+", " ");
    }

    public String getArea1Severity() {
        return area1Severity;
    }

    public void setArea1Severity(String area1Severity) {
        this.area1Severity = area1Severity == null ? null : area1Severity.trim().replaceAll("\\s+", " ");
    }

    public String getArea1Damage() {
        return area1Damage;
    }

    public void setArea1Damage(String area1Damage) {
        this.area1Damage = area1Damage == null ? null : area1Damage.trim().replaceAll("\\s+", " ");
    }

    public String getArea2Severity() {
        return area2Severity;
    }

    public void setArea2Severity(String area2Severity) {
        this.area2Severity = area2Severity == null ? null : area2Severity.trim().replaceAll("\\s+", " ");
    }

    public String getArea2Damage() {
        return area2Damage;
    }

    public void setArea2Damage(String area2Damage) {
        this.area2Damage = area2Damage == null ? null : area2Damage.trim().replaceAll("\\s+", " ");
    }

    public String getTotalLossDate() {
        return totalLossDate;
    }

    public void setTotalLossDate(String totalLossDate) {
        this.totalLossDate = totalLossDate == null ? null : totalLossDate.trim().replaceAll("\\s+", " ");
    }


    @Override
    protected void validate(Claim claim) throws Exception {
        StringBuilder statusString = new StringBuilder();
        super.validate(claim);
        String empty="";
        
        // First, lets check if an open 'Total Loss Payment' type-task is already present on the claim
        List<Task> tasks = taskService.getAllTasksByClaim(claim.getId());
        for (Task task : tasks) {
            if ("Total Loss Payment".equals(task.getType()) && task.getDescription().startsWith("A Total Loss payment is required and the following details apply:")
                    && !task.getComplete()) {
                statusString.append("A Total Loss Payment Task already exists on this claim.");
                break;
            }
        }

        if (imsReference != null) {
            String clean = StringEscapeUtils.unescapeHtml4(Jsoup.clean(imsReference, Whitelist.basic()));
            if (!clean.equals(imsReference)) {
                LOG.warn("IMS Reference contains forbidden content - possible XSS attack: '{}'!='{}'", imsReference, clean);
                imsReference = clean;
//                statusString.append("IMS Reference contains forbidden content.");
            }
        } else {
            imsReference=empty;
        }
        if (registrationNumber != null) {
            String clean = StringEscapeUtils.unescapeHtml4(Jsoup.clean(registrationNumber, Whitelist.basic()));
            if (!clean.equals(registrationNumber)) {
                LOG.warn("Registration Number contains forbidden content - possible XSS attack: '{}'!='{}'", registrationNumber, clean);
                registrationNumber = clean;
//                statusString.append("Registration Number contains forbidden content.");
            }
        } else {
            registrationNumber=empty;
        }
        if (make != null) {
            String clean = StringEscapeUtils.unescapeHtml4(Jsoup.clean(make, Whitelist.basic()));
            if (!clean.equals(make)) {
                LOG.warn("Make contains forbidden content - possible XSS attack: '{}'!='{}'", make, clean);
                make = clean;
//                statusString.append("Make contains forbidden content.");
            }
        } else {
            make=empty;
        }
        if (model != null) {
            String clean = StringEscapeUtils.unescapeHtml4(Jsoup.clean(model, Whitelist.basic()));
            if (!clean.equals(model)) {
                LOG.warn("Model contains forbidden content - possible XSS attack: '{}'!='{}'", model, clean);
                model = clean;
//                statusString.append("Model Number contains forbidden content.");
            }
        } else {
            model=empty;
        }
        if (chassisNumber != null) {
            String clean = StringEscapeUtils.unescapeHtml4(Jsoup.clean(chassisNumber, Whitelist.basic()));
            if (!clean.equals(chassisNumber)) {
                LOG.warn("Chassis Number contains forbidden content - possible XSS attack: '{}'!='{}'", chassisNumber, clean);
                chassisNumber = clean;
//                statusString.append("Chassis Number contains forbidden content.");
            }
        } else {
            chassisNumber=empty;
        }
        if (preAccidentValue != null) {
            String clean = StringEscapeUtils.unescapeHtml4(Jsoup.clean(preAccidentValue, Whitelist.basic()));
            if (!clean.equals(preAccidentValue)) {
                LOG.warn("Pre Accident Value contains forbidden content - possible XSS attack: '{}'!='{}'", preAccidentValue, clean);
                preAccidentValue = clean;
//                statusString.append("Pre Accident Value contains forbidden content.");
            }
        } else {
            preAccidentValue=empty;
        }
        if (salvageAmount != null) {
            String clean = StringEscapeUtils.unescapeHtml4(Jsoup.clean(salvageAmount, Whitelist.basic()));
            if (!clean.equals(salvageAmount)) {
                LOG.warn("Salvage Amount contains forbidden content - possible XSS attack: '{}'!='{}'", salvageAmount, clean);
                salvageAmount = clean;
//                statusString.append("Salvage Amount contains forbidden content.");
            }
        } else {
            salvageAmount=empty;
        }
        if (salvageCategory != null) {
            String clean = StringEscapeUtils.unescapeHtml4(Jsoup.clean(salvageCategory, Whitelist.basic()));
            if (!clean.equals(salvageCategory)) {
                LOG.warn("Salvage Category contains forbidden content - possible XSS attack: '{}'!='{}'", salvageCategory, clean);
                salvageCategory = clean;
//                statusString.append("Salvage Category contains forbidden content.");
            }
        } else {
            salvageCategory=empty;
        }
        if (amountToPay != null) {
            String clean = StringEscapeUtils.unescapeHtml4(Jsoup.clean(amountToPay, Whitelist.basic()));
            if (!clean.equals(amountToPay)) {
                LOG.warn("Amount To Pay contains forbidden content - possible XSS attack: '{}'!='{}'", amountToPay, clean);
                amountToPay = clean;
//                statusString.append("Amount To Pay contains forbidden content.");
            }
        } else {
            amountToPay=empty;
        }
        if (thirdPartyName != null) {
            String clean = StringEscapeUtils.unescapeHtml4(Jsoup.clean(thirdPartyName, Whitelist.basic()));
            if (!clean.equals(thirdPartyName)) {
                LOG.warn("Third Party Name contains forbidden content - possible XSS attack: '{}'!='{}'", thirdPartyName, clean);
                thirdPartyName = clean;
//                statusString.append("Third Party Name contains forbidden content.");
            }
        } else {
            thirdPartyName=empty;
        }
        if (thirdPartyReg != null) {
            String clean = StringEscapeUtils.unescapeHtml4(Jsoup.clean(thirdPartyReg, Whitelist.basic()));
            if (!clean.equals(thirdPartyReg)) {
                LOG.warn("Third Party Reg contains forbidden content - possible XSS attack: '{}'!='{}'", thirdPartyReg, clean);
                thirdPartyReg = clean;
//                statusString.append("Third Party Reg contains forbidden content.");
            }
        } else {
            thirdPartyReg=empty;
        }
        if (thirdPartyClaimNumber != null) {
            String clean = StringEscapeUtils.unescapeHtml4(Jsoup.clean(thirdPartyClaimNumber, Whitelist.basic()));
            if (!clean.equals(thirdPartyClaimNumber)) {
                LOG.warn("Third Party Claim Number contains forbidden content - possible XSS attack: '{}'!='{}'", thirdPartyClaimNumber, clean);
                thirdPartyClaimNumber = clean;
//                statusString.append("Third Party Claim Number contains forbidden content.");
            }
        } else {
            thirdPartyClaimNumber=empty;
        }
        if (thirdPartyAgentName != null) {
            String clean = StringEscapeUtils.unescapeHtml4(Jsoup.clean(thirdPartyAgentName, Whitelist.basic()));
            if (!clean.equals(thirdPartyAgentName)) {
                LOG.warn("Third Party Agent Name contains forbidden content - possible XSS attack: '{}'!='{}'", thirdPartyAgentName, clean);
                thirdPartyAgentName = clean;
//                statusString.append("Third Party Agent Name contains forbidden content.");
            }
        } else {
            thirdPartyAgentName=empty;
        }
        if (title != null) {
            String clean = StringEscapeUtils.unescapeHtml4(Jsoup.clean(title, Whitelist.basic()));
            if (!clean.equals(title)) {
                LOG.warn("Title contains forbidden content - possible XSS attack: '{}'!='{}'", title, clean);
                title = clean;
//                statusString.append("Title contains forbidden content");
            }
        } else {
            title=empty;
        }
        if (driverFirstName != null) {
            String clean = StringEscapeUtils.unescapeHtml4(Jsoup.clean(driverFirstName, Whitelist.basic()));
            if (!clean.equals(driverFirstName)) {
                LOG.warn("Driver First Name contains forbidden content - possible XSS attack: '{}'!='{}'", driverFirstName, clean);
                driverFirstName = clean;
//                statusString.append("Driver First Name contains forbidden content.");
            }
        } else {
            driverFirstName=empty;
        }
        if (driverLastName != null) {
            String clean = StringEscapeUtils.unescapeHtml4(Jsoup.clean(driverLastName, Whitelist.basic()));
            if (!clean.equals(driverLastName)) {
                LOG.warn("Driver Last Name contains forbidden content - possible XSS attack: '{}'!='{}'", driverLastName, clean);
                driverLastName = clean;
//                statusString.append("Driver Last Name contains forbidden content.");
            }
        } else {
            driverLastName=empty;
        }
        if (payee != null) {
            String clean = StringEscapeUtils.unescapeHtml4(Jsoup.clean(payee, Whitelist.basic()));
            if (!clean.equals(payee)) {
                LOG.warn("Payee contains forbidden content - possible XSS attack: '{}'!='{}'", payee, clean);
                payee = clean;
//                statusString.append("Payee contains forbidden content.");
            }
        } else {
            payee=empty;
        }
        if (line1 != null) {
            String clean = StringEscapeUtils.unescapeHtml4(Jsoup.clean(line1, Whitelist.basic()));
            if (!clean.equals(line1)) {
                LOG.warn("Line1 contains forbidden content - possible XSS attack: '{}'!='{}'", line1, clean);
                line1 = clean;
//                statusString.append("Line1 contains forbidden content.");
            }
        } else {
            line1=empty;
        }
        if (line2 != null) {
            String clean = StringEscapeUtils.unescapeHtml4(Jsoup.clean(line2, Whitelist.basic()));
            if (!clean.equals(line2)) {
                LOG.warn("Line2 contains forbidden content - possible XSS attack: '{}'!='{}'", line2, clean);
                line2 = clean;
//                statusString.append("Line2 contains forbidden content.");
            }
        } else {
            line2=empty;
        }
        if (town != null) {
            String clean = StringEscapeUtils.unescapeHtml4(Jsoup.clean(town, Whitelist.basic()));
            if (!clean.equals(town)) {
                LOG.warn("Town contains forbidden content - possible XSS attack: '{}'!='{}'", town, clean);
                town = clean;
//                statusString.append("Town contains forbidden content.");
            }
        } else {
            town=empty;
        }
        if (postcode != null) {
            String clean = StringEscapeUtils.unescapeHtml4(Jsoup.clean(postcode, Whitelist.basic()));
            if (!clean.equals(postcode)) {
                LOG.warn("Postcode contains forbidden content - possible XSS attack: '{}'!='{}'", postcode, clean);
                postcode = clean;
//                statusString.append("Postcode contains forbidden content.");
            }
        } else {
            postcode=empty;
        }
        if (vehicleStatus != null) {
            String clean = StringEscapeUtils.unescapeHtml4(Jsoup.clean(vehicleStatus, Whitelist.basic()));
            if (!clean.equals(vehicleStatus)) {
                LOG.warn("Vehicle Status contains forbidden content - possible XSS attack: '{}'!='{}'", vehicleStatus, clean);
                vehicleStatus = clean;
//                statusString.append("Vehicle Status contains forbidden content.");
            }
        } else {
            vehicleStatus=empty;
        }
        if (area1Severity != null) {
            String clean = StringEscapeUtils.unescapeHtml4(Jsoup.clean(area1Severity, Whitelist.basic()));
            if (!clean.equals(area1Severity)) {
                LOG.warn("Area 1 Severity contains forbidden content - possible XSS attack: '{}'!='{}'", area1Severity, clean);
                area1Severity = clean;
//                statusString.append("Area 1 Severity contains forbidden content.");
            }
        } else {
            area1Severity=empty;
        }
        if (area1Damage != null) {
            String clean = StringEscapeUtils.unescapeHtml4(Jsoup.clean(area1Damage, Whitelist.basic()));
            if (!clean.equals(area1Damage)) {
                LOG.warn("Area 1 Damage contains forbidden content - possible XSS attack: '{}'!='{}'", area1Damage, clean);
                area1Damage = clean;
//                statusString.append("Area 1 Damage contains forbidden content.");
            }
        } else {
            area1Damage=empty;
        }
        if (area2Severity != null) {
            String clean = StringEscapeUtils.unescapeHtml4(Jsoup.clean(area2Severity, Whitelist.basic()));
            if (!clean.equals(area2Severity)) {
                LOG.warn("Area 2 Severity contains forbidden content - possible XSS attack: '{}'!='{}'", area2Severity, clean);
                area2Severity = clean;
//                statusString.append("Area 2 Severity contains forbidden content.");
            }
        } else {
            area2Severity=empty;
        }
        if (area2Damage != null) {
            String clean = StringEscapeUtils.unescapeHtml4(Jsoup.clean(area2Damage, Whitelist.basic()));
            if (!clean.equals(area2Damage)) {
                LOG.warn("Area 2 Damage contains forbidden content - possible XSS attack: '{}'!='{}'", area2Damage, clean);
                area2Damage = clean;
//                statusString.append("Area 2 Damage contains forbidden content.");
            }
        } else {
            area2Damage=empty;
        }
        if (totalLossDate != null) {
            String clean = StringEscapeUtils.unescapeHtml4(Jsoup.clean(totalLossDate, Whitelist.basic()));
            if (!clean.equals(totalLossDate)) {
                LOG.warn("Total Loss Date contains forbidden content - possible XSS attack: '{}'!='{}'", totalLossDate, clean);
                totalLossDate = clean;
//                statusString.append("Total Loss Date contains forbidden content.");
            }
        } else {
            totalLossDate=empty;
        }
        
        if (!statusString.toString().isEmpty()) {
            throw new Exception(statusString.toString());
        }
    }


    @Override
    protected void doProcess(Claim claim) throws Exception {
        Task task = new Task();

        task.setClaim(claim);
        task.setDueDate(DateHelper.addDay(new Date(), 1));
        StringBuilder description = new StringBuilder();
        description.append("A Total Loss payment is required and the following details apply:<br>")
                .append("IMS Reference: ").append(imsReference).append("<br>")
                .append("Registration Number: ").append(registrationNumber).append("<br>")
                .append("Make: ").append(make).append("<br>")
                .append("Model: ").append(model).append("<br>")
                .append("Chassis Number: ").append(chassisNumber).append("<br>")
                .append("Pre Accident Value: ").append(preAccidentValue).append("<br>")
                .append("Salvage Amount: ").append(salvageAmount).append("<br>")
                .append("Salvage Category: ").append(salvageCategory).append("<br>")
                .append("Amount to Pay: ").append(amountToPay).append("<br>")
                .append("Third Party Name: ").append(thirdPartyName).append("<br>")
                .append("Third Party Reg: ").append(thirdPartyReg).append("<br>")
                .append("Third Party Claim Number: ").append(thirdPartyClaimNumber).append("<br>")
                .append("Third Party Agent Name: ").append(thirdPartyAgentName).append("<br>")
                .append("Title: ").append(title).append("<br>")
                .append("Driver First Name: ").append(driverFirstName).append("<br>")
                .append("Driver Last Name: ").append(driverLastName).append("<br>")
                .append("Payee: ").append(payee).append("<br>")
                .append("Line1: ").append(line1).append("<br>")
                .append("Line2: ").append(line2).append("<br>")
                .append("Town: ").append(town).append("<br>")
                .append("Postcode: ").append(postcode).append("<br>")
                .append("Vehicle Status: ").append(vehicleStatus).append("<br>")
                .append("Area 1 Severity: ").append(area1Severity).append("<br>")
                .append("Area 1 Damage: ").append(area1Damage).append("<br>")
                .append("Area 2 Severity: ").append(area2Severity).append("<br>")
                .append("Area 2 Damage: ").append(area2Damage).append("<br>")
                .append("Total Loss Date: ").append(totalLossDate);
        task.setDescription(description.toString());
        task.setRaisedBy(userService.findByUserName("system"));
        task.setType("Total Loss Payment");
        // CHO External task
        task.setInsurer(Boolean.FALSE);
        task.setVisibility(3);
        task.setVisibilityRole(WebUserRole.ROLE_INS_CH);

        try {
            taskService.createNewTask(task);
        } catch (Exception ex) {
            LOG.error("Error creating Total Loss task on claim with choref '{}'", claim.getChoReference());
        }
    }

}
