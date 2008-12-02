<%-- 
    Document   : UploadClaims
    Created on : 09-Nov-2008, 23:28:37
    Author     : Emmanuel
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">
    
    function validateFile(){
        
        var uploadFile = document.form.upload.value;
        
        if(uploadFile==""){
            alert("No xml document selected for upload");
            return false;
        }else{
            
            if((uploadFile.lastIndexOf("."))>0){
                var filename = uploadFile.substr(uploadFile.lastIndexOf('\\')+1, uploadFile.length);
                var dot = filename.lastIndexOf("."); 
                var extension = (filename.substr(dot, filename.length)).toUpperCase(); 
            }else{
                alert("Please select a valid xml file to upload into CHOX");
                return false;
            }

            if(extension!=".XML") {
               alert("Please select a valid xml file to upload into CHOX");
               return false;
            }
            
            return true;
        }        
    }
    
</script>
<html>
    <head >
        <title>IDAS-CHOX</title >      
        <link href="<%= request.getContextPath()%>/styles/chox.css" rel="stylesheet" type="text/css" media="all"/> 
    </head>
    <body >
        
        <div class="outer">
            
            <div class="inner">        
                
                
                
                
                <div id="chox-menu">
                    <table cellpadding="0" cellspacing="0" border="0" width="100%">
                        <tr valign="middle">
                            <td>
                                <img src="<%= request.getContextPath()%>/images/chox_logo_small.jpg" style="display: inline; float: left" />
                            </td>
                            <td width="100%" align="right">
                                <div class="top-menu">
                                    <a href="<s:url action="inbox"/>">Home</a>&nbsp;|&nbsp;
                                    <a href='<s:url action="uploadClaims"/>'>XML Uploads</a>&nbsp;|&nbsp; 
                                    <a href="#">Help</a> &nbsp;|&nbsp;
                                    <a href="#">Support</a>&nbsp;|&nbsp; 
                                    <a href="#">About Chox</a>&nbsp;|&nbsp;
                                    <a href="<%=request.getContextPath()%>/j_acegi_logout">Log Off</a>
                                </div>
                            </td>
                        </tr>
                    </table>
                </div>  
                
                
                <div class="chox-claim-header x-panel-bwrap chox-form-container">
                    
                    <s:form action ="processClaimsAction" method ="POST" enctype="multipart/form-data" name="form">
                        <table>
                            <tr>
                                <td>
                                    <s:file id="fileUploader" name ="upload" label ="Claim XML File" size="55"/>   
                                </td>
                            </tr>                
                            <tr>
                                <td>
                                    <input type="submit" id="bAddAttachment" value="Submit" onclick="return validateFile();"/>
                                </td>
                            </tr>
                        </table>     
                        
                    </s:form>
                    
                </div>
                
                
                
            </div>
        </div>
        
        
    </body>
</html> 