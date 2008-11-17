package chox.model;

import chox.model.*;

public class XMLParseResult {
    private long ID = -1;
    private long SupplierId = -1;
    private Boolean isSchemaValid = true;
    private Boolean isDataValid = true;
    private String SchemaValidationRemark = "";
    private String DataValidationRemark = "";
    private String UploadType;
    
    // USE WHEN RUNING THE VALIDATION
    private Boolean isCurrentScheValid = true;
    private Boolean isCurrentDataValid = true;
    
    // SETUP DATA - CLAIM OBJECT
    private Chorganisation chorganisation;
    private Claim claim;
    private Customer customer;
    private EngineerReport engineerreport;
    private Incident incident;

    public String getDataValidationRemark() {
        return DataValidationRemark;
    }

    public void setDataValidationRemark(String DataValidationRemark) {
        this.DataValidationRemark = DataValidationRemark;
    }

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public String getSchemaValidationRemark() {
        return SchemaValidationRemark;
    }

    public void setSchemaValidationRemark(String SchemaValidationRemark) {
        this.SchemaValidationRemark = SchemaValidationRemark;
    }

    public long getSupplierId() {
        return SupplierId;
    }

    public void setSupplierId(long SupplierId) {
        this.SupplierId = SupplierId;
    }

    public String getUploadType() {
        return UploadType;
    }

    public void setUploadType(String UploadType) {
        this.UploadType = UploadType;
    }

    public Chorganisation getChorganisation() {
        return chorganisation;
    }

    public void setChorganisation(Chorganisation chorganisation) {
        this.chorganisation = chorganisation;
    }

    public Claim getClaim() {
        return claim;
    }

    public void setClaim(Claim claim) {
        this.claim = claim;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public EngineerReport getEngineerreport() {
        return engineerreport;
    }

    public void setEngineerreport(EngineerReport engineerreport) {
        this.engineerreport = engineerreport;
    }

    public Incident getIncident() {
        return incident;
    }

    public void setIncident(Incident incident) {
        this.incident = incident;
    }

    public Injury getInjury() {
        return injury;
    }

    public void setInjury(Injury injury) {
        this.injury = injury;
    }

    public Insurer getInsurer() {
        return insurer;
    }

    public void setInsurer(Insurer insurer) {
        this.insurer = insurer;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public Boolean getIsCurrentDataValid() {
        return isCurrentDataValid;
    }

    public void setIsCurrentDataValid(Boolean isCurrentDataValid) {
        this.isCurrentDataValid = isCurrentDataValid;
    }

    public Boolean getIsCurrentScheValid() {
        return isCurrentScheValid;
    }

    public void setIsCurrentScheValid(Boolean isCurrentScheValid) {
        this.isCurrentScheValid = isCurrentScheValid;
    }

    public Boolean getIsDataValid() {
        return isDataValid;
    }

    public void setIsDataValid(Boolean isDataValid) {
        this.isDataValid = isDataValid;
    }

    public Boolean getIsSchemaValid() {
        return isSchemaValid;
    }

    public void setIsSchemaValid(Boolean isSchemaValid) {
        this.isSchemaValid = isSchemaValid;
    }

    public LineOfBusiness getLineofbusiness() {
        return lineofbusiness;
    }

    public void setLineofbusiness(LineOfBusiness lineofbusiness) {
        this.lineofbusiness = lineofbusiness;
    }

    public Solicitor getSolicitor() {
        return solicitor;
    }

    public void setSolicitor(Solicitor solicitor) {
        this.solicitor = solicitor;
    }

    public ThirdParty getThirdparty() {
        return thirdparty;
    }

    public void setThirdparty(ThirdParty thirdparty) {
        this.thirdparty = thirdparty;
    }

    public VehicleHire getVehiclehire() {
        return vehiclehire;
    }

    public void setVehiclehire(VehicleHire vehiclehire) {
        this.vehiclehire = vehiclehire;
    }

    public Witness getWitness() {
        return witness;
    }

    public void setWitness(Witness witness) {
        this.witness = witness;
    }
    private Injury injury;
    private Insurer insurer;
    private LineOfBusiness lineofbusiness;
    private Solicitor solicitor;
    private ThirdParty thirdparty;
    private Witness witness;
    
    private VehicleHire vehiclehire;
    private Invoice invoice;
    
    
    
    
    
    
    
}
