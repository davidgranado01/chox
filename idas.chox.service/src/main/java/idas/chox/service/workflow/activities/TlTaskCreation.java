package idas.chox.service.workflow.activities;

import java.util.Date;

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
    static final Logger LOG = LoggerFactory.getLogger(LouUpdate.class);
    private TaskService taskService;
    private UserService userService;
    private String imsReference;
    private String registrationNumber;
    private String make;
    private String model;
    private String chassisNumber;
    private String preAccidentValue;
    private String salvageAmmount;
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
        this.imsReference = imsReference;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getChassisNumber() {
        return chassisNumber;
    }

    public void setChassisNumber(String chassisNumber) {
        this.chassisNumber = chassisNumber;
    }

    public String getPreAccidentValue() {
        return preAccidentValue;
    }

    public void setPreAccidentValue(String preAccidentValue) {
        this.preAccidentValue = preAccidentValue;
    }

    public String getSalvageAmmount() {
        return salvageAmmount;
    }

    public void setSalvageAmmount(String salvageAmmount) {
        this.salvageAmmount = salvageAmmount;
    }

    public String getSalvageCategory() {
        return salvageCategory;
    }

    public void setSalvageCategory(String salvageCategory) {
        this.salvageCategory = salvageCategory;
    }

    public String getAmountToPay() {
        return amountToPay;
    }

    public void setAmountToPay(String amountToPay) {
        this.amountToPay = amountToPay;
    }

    public String getThirdPartyName() {
        return thirdPartyName;
    }

    public void setThirdPartyName(String thirdPartyName) {
        this.thirdPartyName = thirdPartyName;
    }

    public String getThirdPartyReg() {
        return thirdPartyReg;
    }

    public void setThirdPartyReg(String thirdPartyReg) {
        this.thirdPartyReg = thirdPartyReg;
    }

    public String getThirdPartyClaimNumber() {
        return thirdPartyClaimNumber;
    }

    public void setThirdPartyClaimNumber(String thirdPartyClaimNumber) {
        this.thirdPartyClaimNumber = thirdPartyClaimNumber;
    }

    public String getThirdPartyAgentName() {
        return thirdPartyAgentName;
    }

    public void setThirdPartyAgentName(String thirdPartyAgentName) {
        this.thirdPartyAgentName = thirdPartyAgentName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDriverFirstName() {
        return driverFirstName;
    }

    public void setDriverFirstName(String driverFirstName) {
        this.driverFirstName = driverFirstName;
    }

    public String getDriverLastName() {
        return driverLastName;
    }

    public void setDriverLastName(String driverLastName) {
        this.driverLastName = driverLastName;
    }

    public String getPayee() {
        return payee;
    }

    public void setPayee(String payee) {
        this.payee = payee;
    }

    public String getLine1() {
        return line1;
    }

    public void setLine1(String line1) {
        this.line1 = line1;
    }

    public String getLine2() {
        return line2;
    }

    public void setLine2(String line2) {
        this.line2 = line2;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getVehicleStatus() {
        return vehicleStatus;
    }

    public void setVehicleStatus(String vehicleStatus) {
        this.vehicleStatus = vehicleStatus;
    }

    public String getArea1Severity() {
        return area1Severity;
    }

    public void setArea1Severity(String area1Severity) {
        this.area1Severity = area1Severity;
    }

    public String getArea1Damage() {
        return area1Damage;
    }

    public void setArea1Damage(String area1Damage) {
        this.area1Damage = area1Damage;
    }

    public String getArea2Severity() {
        return area2Severity;
    }

    public void setArea2Severity(String area2Severity) {
        this.area2Severity = area2Severity;
    }

    public String getArea2Damage() {
        return area2Damage;
    }

    public void setArea2Damage(String area2Damage) {
        this.area2Damage = area2Damage;
    }

    public String getTotalLossDate() {
        return totalLossDate;
    }

    public void setTotalLossDate(String totalLossDate) {
        this.totalLossDate = totalLossDate;
    }


    @Override
    protected void validate(Claim claim) throws Exception {
        StringBuilder statusString = new StringBuilder();
        super.validate(claim);
        if (imsReference != null) {
            String clean = Jsoup.clean(imsReference, Whitelist.basic());
            if (!clean.equals(imsReference)) {
                LOG.warn("IMS Reference contains forbidden content - possible XSS attack: {}", imsReference);
                statusString.append("IMS Reference contains forbidden content.");
            }
        }
        if (registrationNumber != null) {
            String clean = Jsoup.clean(registrationNumber, Whitelist.basic());
            if (!clean.equals(registrationNumber)) {
                LOG.warn("Registration Number contains forbidden content - possible XSS attack: {}", registrationNumber);
                statusString.append("Registration Number contains forbidden content.");
            }
        }
        if (make != null) {
            String clean = Jsoup.clean(make, Whitelist.basic());
            if (!clean.equals(make)) {
                LOG.warn("Make contains forbidden content - possible XSS attack: {}", make);
                statusString.append("Make contains forbidden content.");
            }
        }
        if (model != null) {
            String clean = Jsoup.clean(model, Whitelist.basic());
            if (!clean.equals(model)) {
                LOG.warn("Model contains forbidden content - possible XSS attack: {}", model);
                statusString.append("Model Number contains forbidden content.");
            }
        }
        if (chassisNumber != null) {
            String clean = Jsoup.clean(chassisNumber, Whitelist.basic());
            if (!clean.equals(chassisNumber)) {
                LOG.warn("Chassis Number contains forbidden content - possible XSS attack: {}", chassisNumber);
                statusString.append("Chassis Number contains forbidden content.");
            }
        }
        if (preAccidentValue != null) {
            String clean = Jsoup.clean(preAccidentValue, Whitelist.basic());
            if (!clean.equals(preAccidentValue)) {
                LOG.warn("Pre Accident Value contains forbidden content - possible XSS attack: {}", preAccidentValue);
                statusString.append("Pre Accident Value contains forbidden content.");
            }
        }
        if (salvageAmmount != null) {
            String clean = Jsoup.clean(salvageAmmount, Whitelist.basic());
            if (!clean.equals(salvageAmmount)) {
                LOG.warn("Salvage Amount contains forbidden content - possible XSS attack: {}", salvageAmmount);
                statusString.append("Salvage Amount contains forbidden content.");
            }
        }
        if (salvageCategory != null) {
            String clean = Jsoup.clean(salvageCategory, Whitelist.basic());
            if (!clean.equals(salvageCategory)) {
                LOG.warn("Salvage Category contains forbidden content - possible XSS attack: {}", salvageCategory);
                statusString.append("Salvage Category contains forbidden content.");
            }
        }
        if (amountToPay != null) {
            String clean = Jsoup.clean(amountToPay, Whitelist.basic());
            if (!clean.equals(amountToPay)) {
                LOG.warn("Amount To Pay contains forbidden content - possible XSS attack: {}", amountToPay);
                statusString.append("Amount To Pay contains forbidden content.");
            }
        }
        if (thirdPartyName != null) {
            String clean = Jsoup.clean(thirdPartyName, Whitelist.basic());
            if (!clean.equals(thirdPartyName)) {
                LOG.warn("Third Party Name contains forbidden content - possible XSS attack: {}", thirdPartyName);
                statusString.append("Third Party Name contains forbidden content.");
            }
        }
        if (thirdPartyReg != null) {
            String clean = Jsoup.clean(thirdPartyReg, Whitelist.basic());
            if (!clean.equals(thirdPartyReg)) {
                LOG.warn("Third Party Reg contains forbidden content - possible XSS attack: {}", thirdPartyReg);
                statusString.append("Third Party Reg contains forbidden content.");
            }
        }
        if (thirdPartyClaimNumber != null) {
            String clean = Jsoup.clean(thirdPartyClaimNumber, Whitelist.basic());
            if (!clean.equals(thirdPartyClaimNumber)) {
                LOG.warn("Third Party Claim Number contains forbidden content - possible XSS attack: {}", thirdPartyClaimNumber);
                statusString.append("Third Party Claim Number contains forbidden content.");
            }
        }
        if (thirdPartyAgentName != null) {
            String clean = Jsoup.clean(thirdPartyAgentName, Whitelist.basic());
            if (!clean.equals(thirdPartyAgentName)) {
                LOG.warn("Third Party Agent Name contains forbidden content - possible XSS attack: {}", thirdPartyAgentName);
                statusString.append("Third Party Agent Name contains forbidden content.");
            }
        }
        if (title != null) {
            String clean = Jsoup.clean(title, Whitelist.basic());
            if (!clean.equals(title)) {
                LOG.warn("Title contains forbidden content - possible XSS attack: {}", title);
                statusString.append("Title contains forbidden content");
            }
        }
        if (driverFirstName != null) {
            String clean = Jsoup.clean(driverFirstName, Whitelist.basic());
            if (!clean.equals(driverFirstName)) {
                LOG.warn("Driver First Name contains forbidden content - possible XSS attack: {}", driverFirstName);
                statusString.append("Driver First Name contains forbidden content.");
            }
        }
        if (driverLastName != null) {
            String clean = Jsoup.clean(driverLastName, Whitelist.basic());
            if (!clean.equals(driverLastName)) {
                LOG.warn("Driver Last Name contains forbidden content - possible XSS attack: {}", driverLastName);
                statusString.append("Driver Last Name contains forbidden content.");
            }
        }
        if (payee != null) {
            String clean = Jsoup.clean(payee, Whitelist.basic());
            if (!clean.equals(payee)) {
                LOG.warn("Payee contains forbidden content - possible XSS attack: {}", payee);
                statusString.append("Payee contains forbidden content.");
            }
        }
        if (line1 != null) {
            String clean = Jsoup.clean(line1, Whitelist.basic());
            if (!clean.equals(line1)) {
                LOG.warn("Line1 contains forbidden content - possible XSS attack: {}", line1);
                statusString.append("Line1 contains forbidden content.");
            }
        }
        if (line2 != null) {
            String clean = Jsoup.clean(line2, Whitelist.basic());
            if (!clean.equals(line2)) {
                LOG.warn("Line2 contains forbidden content - possible XSS attack: {}", line2);
                statusString.append("Line2 contains forbidden content.");
            }
        }
        if (town != null) {
            String clean = Jsoup.clean(town, Whitelist.basic());
            if (!clean.equals(town)) {
                LOG.warn("Town contains forbidden content - possible XSS attack: {}", town);
                statusString.append("Town contains forbidden content.");
            }
        }
        if (postcode != null) {
            String clean = Jsoup.clean(postcode, Whitelist.basic());
            if (!clean.equals(postcode)) {
                LOG.warn("Postcode contains forbidden content - possible XSS attack: {}", postcode);
                statusString.append("Postcode contains forbidden content.");
            }
        }
        if (vehicleStatus != null) {
            String clean = Jsoup.clean(vehicleStatus, Whitelist.basic());
            if (!clean.equals(vehicleStatus)) {
                LOG.warn("Vehicle Status contains forbidden content - possible XSS attack: {}", vehicleStatus);
                statusString.append("Vehicle Status contains forbidden content.");
            }
        }
        if (area1Severity != null) {
            String clean = Jsoup.clean(area1Severity, Whitelist.basic());
            if (!clean.equals(area1Severity)) {
                LOG.warn("Area 1 Severity contains forbidden content - possible XSS attack: {}", area1Severity);
                statusString.append("Area 1 Severity contains forbidden content.");
            }
        }
        if (area1Damage != null) {
            String clean = Jsoup.clean(area1Damage, Whitelist.basic());
            if (!clean.equals(area1Damage)) {
                LOG.warn("Area 1 Damage contains forbidden content - possible XSS attack: {}", area1Damage);
                statusString.append("Area 1 Damage contains forbidden content.");
            }
        }
        if (area2Severity != null) {
            String clean = Jsoup.clean(area2Severity, Whitelist.basic());
            if (!clean.equals(area2Severity)) {
                LOG.warn("Area 2 Severity contains forbidden content - possible XSS attack: {}", area2Severity);
                statusString.append("Area 2 Severity contains forbidden content.");
            }
        }
        if (area2Damage != null) {
            String clean = Jsoup.clean(area2Damage, Whitelist.basic());
            if (!clean.equals(area2Damage)) {
                LOG.warn("Area 2 Damage contains forbidden content - possible XSS attack: {}", area2Damage);
                statusString.append("Area 2 Damage contains forbidden content.");
            }
        }
        if (totalLossDate != null) {
            String clean = Jsoup.clean(totalLossDate, Whitelist.basic());
            if (!clean.equals(totalLossDate)) {
                LOG.warn("Total Loss Date contains forbidden content - possible XSS attack: {}", totalLossDate);
                statusString.append("Total Loss Date contains forbidden content.");
            }
        }
        
        if (!statusString.toString().isEmpty()) {
            throw new Exception(statusString.toString());
        }
    }


    @Override
    protected void doProcess(Claim claim) throws Exception {
        Task task = new Task();

        task.setClaim(claim);
        task.setComplete(Boolean.FALSE);
        task.setDueDate(DateHelper.addDay(new Date(), 1));
        task.setDescription("Total loss: VRN is " + registrationNumber + ", amount to pay: " + amountToPay);
        task.setRaisedBy(userService.findByUserName("system"));
        task.setType("Total Loss");
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
