package chox.model;

public class XMLParseResult {
    private long ID = -1;
    private long SupplierId = -1;
    private Boolean isSchemaValid = true;
    private Boolean isDataValid = true;
    private String SchemaValidationRemark = "";
    private String DataValidationRemark = "";
    private Rental rental;
    private String UploadType;
    
    // USE WHEN RUNING THE VALIDATION
    private Boolean isCurrentScheValid = true;
    private Boolean isCurrentDataValid = true;

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

    public String getDataValidationRemark() {
        return DataValidationRemark;
    }

    public void setDataValidationRemark(String DataValidationRemark) {
        this.DataValidationRemark = DataValidationRemark;
    }

    public String getSchemaValidationRemark() {
        return SchemaValidationRemark;
    }

    public void setSchemaValidationRemark(String SchemaValidationRemark) {
        this.SchemaValidationRemark = SchemaValidationRemark;
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

    public String getUploadType() {
        return UploadType;
    }

    public void setUploadType(String UploadType) {
        this.UploadType = UploadType;
    }
    
    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public long getSupplierId() {
        return SupplierId;
    }

    public void setSupplierId(long SupplierId) {
        this.SupplierId = SupplierId;
    }

    public Rental getRental() {
        return rental;
    }

    public void setRental(Rental rental) {
        this.rental = rental;
    }
    
    public String getSupplierReference()
    {
        if(rental != null)
        {
            return rental.getSupplierReference();
        }
        else
        {
            return "N/A";
        }
    }
            
    public String getStatus()
    {
        return (this.isDataValid && this.isSchemaValid) ? "Ok" : "Error";
    }
    
    public String[] getDataValidationRemarkInList()
    {
        return DataValidationRemark.split("\\|");
    }
    
    public String[] getSchemaValidationRemarkInList()
    {
        return SchemaValidationRemark.split("\\|");
    }
    
    
}
