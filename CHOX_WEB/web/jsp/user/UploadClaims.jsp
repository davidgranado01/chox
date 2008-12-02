<%@ taglib uri="/struts-tags" prefix="s" %>


<%@page contentType="text/html" pageEncoding="UTF-8"%>
<html>
    

    
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
        
    <title>IDAS-CHOX</title >      
        
    <link href="<%= request.getContextPath()%>/styles/chox.css" rel="stylesheet" type="text/css" media="all"/>        
    <link href="<%= request.getContextPath()%>/css/ext-all.css" rel="stylesheet" type="text/css" media="all"/>
    </head>
    <body>
        
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
                                    <s:if test="isCHO">
                                    <a href='<s:url action="uploadClaims"/>'>XML Uploads</a>&nbsp;|&nbsp;
                                    </s:if> 
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
                        
                        <fieldset class="x-fieldset">
                                
                            <div class="status-info">
                                Please use the form below to upload claims to CHOX. For further information and assistance, please see the support documentation.
                            </div>                        
                            <table>
                                <tr>
                                    <td>
                                        <s:file id="fileUploader" name ="upload" label ="Claim XML File" size="55"/>   
                                    </td>
                                </tr>                
                                <tr>
                                    <td>
                                        <s:submit onclick="javascript:validateFile();" value="Upload claims"/>
                                    </td>
                                </tr>
                            </table>     
                        </fieldset>
                    </s:form> 
                </div>
            </div>
        </div>
    </body>
</html> 