package idas.chox.core.xmlValidation;

/**
 *
 * @author Carlson
 */
public class NodeRuleModel {

    private String nodeName;
    private String nodeDesc;
    private String dataType;
    private int length;
    private boolean newClaimDataMandatory;
    private boolean newSubscriberClaimDataMandatory;
    private boolean newFixedFeeClaimDataMandatory;
    private boolean insurerInvoiceDataMandatory;
    private boolean existingClaimDataMandatory;
    private boolean existingSubscriberClaimDataMandatory;
    private boolean existingFixedFeeClaimDataMandatory;
    private boolean newInvoiceDataMandatory;
    private boolean existingInvoiceDataMandatory;
    private boolean tpiInterventionDataMandatory;
    private boolean hireMonitoringDataMandatory;
    private boolean newSupplementaryInvoiceMandatory;
    private boolean offHiredDataMandatory;
    private boolean insurerClaimDataMandatory;
    private String regExp;

    public boolean isOffHiredDataMandatory() {
        return offHiredDataMandatory;
    }

    public void setOffHiredDataMandatory(String offHiredDataMandatory) {
        this.offHiredDataMandatory = false;
        if (offHiredDataMandatory.trim().equalsIgnoreCase("t")) {
            this.offHiredDataMandatory = true;
        }
    }

    public boolean isNewSupplementaryInvoiceMandatory() {
        return newSupplementaryInvoiceMandatory;
    }

    public void setNewSupplementaryInvoiceMandatory(String newSupplementaryInvoiceMandatory) {
        this.newSupplementaryInvoiceMandatory = false;
        if (newSupplementaryInvoiceMandatory.trim().equalsIgnoreCase("t")) {
            this.newSupplementaryInvoiceMandatory = true;
        }
    }

    public boolean isHireMonitoringDataMandatory() {
        return hireMonitoringDataMandatory;
    }

    public void setHireMonitoringDataMandatory(String hireMoniteringDataMandatory) {
        this.hireMonitoringDataMandatory = false;
        if (hireMoniteringDataMandatory.trim().equalsIgnoreCase("t")) {
            this.hireMonitoringDataMandatory = true;
        }
    }

    public boolean isExistingClaimDataMandatory() {
        return existingClaimDataMandatory;
    }

    public void setExistingClaimDataMandatory(String existingClaimDataMandatory) {
        this.existingClaimDataMandatory = false;
        if (existingClaimDataMandatory.trim().equalsIgnoreCase("t")) {
            this.existingClaimDataMandatory = true;
        }
    }

    public boolean isExistingSubscriberClaimDataMandatory() {
        return existingSubscriberClaimDataMandatory;
    }

    public void setExistingSubscriberClaimDataMandatory(String existingSubscriberClaimDataMandatory) {
        this.existingSubscriberClaimDataMandatory = false;
        if (existingSubscriberClaimDataMandatory.trim().equalsIgnoreCase("t")) {
            this.existingSubscriberClaimDataMandatory = true;
        }
    }

    public boolean isExistingFixedFeeClaimDataMandatory() {
        return existingFixedFeeClaimDataMandatory;
    }

    public void setExistingFixedFeeClaimDataMandatory(String existingFixedFeeClaimDataMandatory) {
        this.existingFixedFeeClaimDataMandatory = false;
        if (existingFixedFeeClaimDataMandatory.trim().equalsIgnoreCase("t")) {
            this.existingFixedFeeClaimDataMandatory = true;
        }
    }

    public boolean isExistingInvoiceDataMandatory() {
        return existingInvoiceDataMandatory;
    }

    public void setExistingInvoiceDataMandatory(String existingInvoiceDataMandatory) {
        this.existingInvoiceDataMandatory = false;
        if (existingInvoiceDataMandatory.trim().equalsIgnoreCase("t")) {
            this.existingInvoiceDataMandatory = true;
        }
    }

    public int getLength() {
        return length;
    }

    public void setLength(String length) {
        if (length.length() > 0) {
            this.length = Integer.parseInt(length.trim());
        }
    }

    public boolean isNewClaimDataMandatory() {
        return newClaimDataMandatory;
    }

    public void setNewClaimDataMandatory(String newClaimDataMandatory) {
        this.newClaimDataMandatory = false;
        if (newClaimDataMandatory.trim().equalsIgnoreCase("t")) {
            this.newClaimDataMandatory = true;
        }
    }

    public boolean isNewSubscriberClaimDataMandatory() {
        return newSubscriberClaimDataMandatory;
    }

    public void setNewSubscriberClaimDataMandatory(String newSubscriberClaimDataMandatory) {
        this.newSubscriberClaimDataMandatory = false;
        if (newSubscriberClaimDataMandatory.trim().equalsIgnoreCase("t")) {
            this.newSubscriberClaimDataMandatory = true;
        }
    }

    public boolean isNewFixedFeeClaimDataMandatory() {
        return newFixedFeeClaimDataMandatory;
    }

    public void setNewFixedFeeClaimDataMandatory(String newFixedFeeClaimDataMandatory) {
        this.newFixedFeeClaimDataMandatory = false;
        if (newFixedFeeClaimDataMandatory.trim().equalsIgnoreCase("t")) {
            this.newFixedFeeClaimDataMandatory = true;
        }
    }

    public boolean isInsurerInvoiceDataMandatory() {
        return insurerInvoiceDataMandatory;
    }

    public void setInsurerInvoiceDataMandatory(String insurerUploadDataMandatory) {
        this.insurerInvoiceDataMandatory = false;
        if (insurerUploadDataMandatory.trim().equalsIgnoreCase("t")) {
            this.insurerInvoiceDataMandatory = true;
        }
    }

    public boolean isInsurerClaimDataMandatory() {
        return insurerClaimDataMandatory;
    }

    public void setInsurerClaimDataMandatory(String insurerClaimDataMandatory) {
        this.insurerClaimDataMandatory = false;
        if (insurerClaimDataMandatory.trim().equalsIgnoreCase("t")) {
            this.insurerClaimDataMandatory = true;
        }
    }

    public boolean isNewInvoiceDataMandatory() {
        return newInvoiceDataMandatory;
    }

    public void setNewInvoiceDataMandatory(String newInvoiceDataMandatory) {
        this.newInvoiceDataMandatory = false;
        if (newInvoiceDataMandatory.trim().equalsIgnoreCase("t")) {
            this.newInvoiceDataMandatory = true;
        }
    }

    public boolean isTpiInterventionDataMandatory() {
        return tpiInterventionDataMandatory;
    }

    public void setTpiInterventionDataMandatory(String tpiInterventionDataMandatory) {
        this.tpiInterventionDataMandatory = false;
        if (tpiInterventionDataMandatory.trim().equalsIgnoreCase("t")) {
            this.tpiInterventionDataMandatory = true;
        }
    }

    public String getNodeDesc() {
        return nodeDesc;
    }

    public void setNodeDesc(String nodeDesc) {
        this.nodeDesc = nodeDesc;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getRegExp() {
        return regExp;
    }

    public void setRegExp(String regExp) {
        this.regExp = regExp;
    }

    @Override
    public String toString() {
        return nodeDesc + " " + nodeName + " " + dataType + " " + newClaimDataMandatory + insurerInvoiceDataMandatory + newInvoiceDataMandatory + existingClaimDataMandatory + existingInvoiceDataMandatory + tpiInterventionDataMandatory+hireMonitoringDataMandatory+newSupplementaryInvoiceMandatory;
    }
}
