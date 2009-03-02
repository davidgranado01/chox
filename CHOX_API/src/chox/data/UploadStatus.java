package chox.data;

import chox.model.XMLParseResult;
import chox.model.ClaimStatus;

/*
 * PLEASE ALTER ProcessClaimsAction.java if adding new upload status
 */
public class UploadStatus {

    public static final String CLAIM_EXIST = "Claim Already Exists";
    public static final String CLAIM_UPLOAD_FAILED = "Claim Upload Failed";
    public static final String CLAIM_UPLOAD_SUCCESSFUL = "Claim Upload Successful";
    public static final String INVOICE_EXIST = "Invoice Already Exists";
    public static final String INVOICE_UPLOAD_SUCCESSFUL = "Invoice Uploaded Successful";
    public static final String INVOICE_UPLOAD_FAILED = "Invoice Upload Failed";
    public static final String INCORRECT_CLAIM_STATUS = "Unable to Upload Invoice - Incorrect Claim Status";
    
    //private static final String invoicedetailnotsave = "Invoice related details cannot be uploaded at the [1st claim notification] stage of the claim lifecycle, invoice related details have not been saved";
    
    public static XMLParseResult getUploadStatus(XMLParseResult result){
        
        Boolean isClaimExist = result.getIsClaimExist();
        Boolean isInvoiceExist = result.getIsInvoiceExist();
        String oldClaimStatus = result.getSExistingClaimStatus();
        Boolean isDataValid = result.getIsDataValid();
        Boolean isSchemaValid = result.getIsSchemaValid();
        Boolean isNewInvoiceExit = result.getIsNewInvoiceExit();
        
        String uploadStatus = "";
              
        if(!isClaimExist){
            if(isSchemaValid && isDataValid){
                uploadStatus = CLAIM_UPLOAD_SUCCESSFUL;
            }else{
                uploadStatus = CLAIM_UPLOAD_FAILED;
                result.getClaim().setStatus("-");
            }
            
        }else{
            
            // CLAIM EXIST
            if(oldClaimStatus.equalsIgnoreCase(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNROUTED)){
                
                uploadStatus = UploadStatus.CLAIM_EXIST;
                
            }else if(oldClaimStatus.equalsIgnoreCase(ClaimStatus.AWAITING_INVOICE_DATA)){
                
                if(isInvoiceExist){
                    
                    uploadStatus = UploadStatus.INVOICE_EXIST;
                    
                }else{

                    if(isSchemaValid && isDataValid){
                        uploadStatus = UploadStatus.INVOICE_UPLOAD_SUCCESSFUL;
                    }else{
                        uploadStatus = UploadStatus.INVOICE_UPLOAD_FAILED;
                    }
                }
            }else{
                if(isInvoiceExist){
                    uploadStatus = UploadStatus.INVOICE_EXIST;
                }else{
                    uploadStatus = UploadStatus.INCORRECT_CLAIM_STATUS;
                }
            }
        }
        
        result.setUploadStatus(uploadStatus);
        return result;
    }
}
