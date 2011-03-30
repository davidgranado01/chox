<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>

<head>
    <title>IDAS-CHOX</title >   
    <script type="text/javascript">
        
        function validateFile(){
            
            var uploadFile = document.form.upload.value;
            
            if(uploadFile==""){
                
                //alert("No xml document selected for upload");
                Ext.MessageBox.show({
                        title: 'Upload failure',
                        msg: 'No xml document selected for upload',
                        width:300,
                        buttons: Ext.MessageBox.OK,
                        icon : Ext.MessageBox.ERROR
                    });
                return false;
                
            }else{
                
                if((uploadFile.lastIndexOf("."))>0){
                    var filename = uploadFile.substr(uploadFile.lastIndexOf('\\')+1, uploadFile.length);
                    var dot = filename.lastIndexOf(".");
                    var extension = (filename.substr(dot, filename.length)).toUpperCase();
                }else{
                    //alert("Please select a valid xml file to upload into CHOX");
                    Ext.MessageBox.show({
                        title: 'Upload failure',
                        msg: 'Please select a valid xml file to upload into CHOX',
                        width:300,
                        buttons: Ext.MessageBox.OK,
                        icon : Ext.MessageBox.ERROR
                    });
                    return false;
                }
                
                if(extension!=".XML") {
                   // alert("Please select a valid xml file to upload into CHOX");
                   Ext.MessageBox.show({
                        title: 'Upload failure',
                        msg: 'Please select a valid xml file to upload into CHOX',
                        width:300,
                        buttons: Ext.MessageBox.OK,
                        icon : Ext.MessageBox.ERROR
                    });
                    return false;
                }
                
                
            }
            
            return true;
        }

    </script>     
</head>


<div class="chox-claim-header x-panel-bwrap chox-form-container">
    <s:form action ="processClaimsAction" method ="POST" enctype="multipart/form-data" name="form" onsubmit="return true;">
        <fieldset class="x-fieldset">
            <legend></legend>
            <div class="status-info">
                Please use the form below to upload claims to CHOX. For further information and assistance, please see the support documentation.
            </div>
            <table>
                <tr>
                    <td>
                        <s:file id="upload" name ="upload" label ="Claim XML File" size="55" cssStyle="height: 20px;"/>
                    </td>
                </tr>
                <tr>
                    <td>
                        <s:if test="uploadFlag">
                            <s:submit onclick="javascript:return validateFile();" value="Upload Claims"/>
                        </s:if>
                        <s:else><br/>
                            <div class="action-error-msg"><b>A Credit Hire Mapping Relationship Does Not Exist. Please Contact CHOX Admin.</b></div>
                        </s:else>
                    </td>
                </tr>
            </table>
        </fieldset>
        <s:token/>
    </s:form>
</div>


