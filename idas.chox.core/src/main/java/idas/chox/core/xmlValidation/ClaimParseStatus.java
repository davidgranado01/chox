package idas.chox.core.xmlValidation;

public enum ClaimParseStatus {

    NEW_CLAIM                                   (0, "New Claim"),
    NEW_SUBSCRIBER_CLAIM                        (1, "New Claim (Subscriber)"),
    EXIST_CLAIM                                 (2, "Claim Already Exists"),
    EXIST_SUBSCRIBER_CLAIM                      (3, "Claim Already Exists (Subscriber)"),
    CLAIM_NOT_EDITABLE                          (4, "Claim Closed or Pending"),
    NEW_INVOICE                                 (5, "New Invoice"),
    EXIST_INVOICE                               (6, "Invoice Already Exists"),
    INVALID_SCHEMA                              (7, "Incorrect XML Structure"),
    INVALID_HIRE_STATE                          (8, "Incorrect Hire State"),
    TPI_NOT_RECOGNIZED                          (9, "Incorrect Value Provided for Hire State"),
    TPI_INTERVENTION                            (10, "New TPI Claim"),
    TPI_NOT_ACCEPTED_BY_INSURER                 (11, "Insurer is not accepting TPI invoices"),
    SUBSCRIBER_NOT_ACCEPTED_BY_INSURER          (12, "Insurer is not accepting Subscriber Claims"),
    HIRE_MONITORING_AND_NEW_INVOICE             (13, "Hire Monitoring and New Invoice"),
    EXISTING_SUPPLEMENTARY_INVOICE              (14, "Supplementary Invoice Already Exists"),
    NEW_SUPPLEMENTARY_INVOICE                   (15, "New Supplementary Invoice"),
    HIRE_MONITORING                             (16, "Hire Monitoring"),
    INVALID_CLAIM_STATUS                        (17, "Invalid Claim Status"),
    INSURER_VS_INSURER_INVOICE                  (18, "New Invoice (Insurer vs Insurer)"),
    INSURER_INVOICE                             (19, "New Insurer Invoice"),
    NEW_FIXEDFEE_CLAIM                          (20, "New Claim (Fixed Fee)"),
    FIXEDFEE_NOT_ACCEPTED_BY_INSURER            (21, "Insurer is not accepting Fixed Fee Claims"),
    EXIST_FIXEDFEE_CLAIM                        (22, "Claim Already Exists (Fixed Fee)"),
    INSURER_CLAIM                               (23, "New Insurer Claim"),
    EXISTS_INSURER_CLAIM                        (24, "Insurer Claim Already Exists"),
    INSURER_HIRE_MONITORING_AND_NEW_INVOICE     (25, "Insurer Hire Monitoring and New Invoice"),
    INSURER_HIRE_MONITORING                     (26, "Insurer Hire Monitoring"),
    INSURER_EXIST_INVOICE                       (27, "Insurer Invoice Already Exists"),
    INSURER_NEW_SUPPLEMENTARY_INVOICE           (28, "New Insurer Supplementary Invoice"),
    EXISTS_DIFFERENT_CLAIM_TYPE                 (29, "Claim Already Exists But As A Different Claim Type"),
    NEW_COLLABORATION_CLAIM                     (30, "New Claim (Collaboration Protocol)"),
    EXIST_COLLABORATION_CLAIM                   (31, "Claim Already Exists (Collaboration Protocol)"),
    COLLABORATION_NOT_ACCEPTED_BY_INSURER       (32, "Insurer is not accepting Collaboration Protocol Claims");

    private final String description;
    private final int ClaimParseStatusValue;

    ClaimParseStatus(int claimParseStatusValue, String description) {
        this.ClaimParseStatusValue = claimParseStatusValue;
        this.description = description;
    }
    
    public int getClaimParseStatusValue() {
        return ClaimParseStatusValue;
    }

    public String getDescription() {
        return description;
    }
}