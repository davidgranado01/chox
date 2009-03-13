<%@ taglib uri="/struts-tags" prefix="s" %>


<%@page contentType="text/html" pageEncoding="UTF-8"%>
<html>
    
<script type="text/javascript" src="<%= request.getContextPath()%>/adapter/jquery/jquery-1.2.6.js"></script>
<script type="text/javascript" src="<%= request.getContextPath()%>/adapter/jquery/jquery.form.js"></script>    
<script type="text/javascript" src="<%= request.getContextPath()%>/adapter/jquery/jquery.blockUI.js"></script>
<script src="<%= request.getContextPath()%>/scripts/general.js" type="text/javascript"></script> 

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
<ul id="top-menu">
    <li><a href="<s:url action="inbox"/>">&nbsp;Home&nbsp;</a></li>
    <li><a href="<s:url action="openUserAccount" />">|&nbsp;Settings&nbsp;</a></li>
    <s:if test="isCHO"><li><a href='<s:url action="uploadClaims"/>'>|&nbsp;XML Uploads&nbsp;</a></li></s:if>
    <li><s:if test="isCHO"><a href="javascript:openFile('<%= request.getContextPath()%>','ChoHelp');">|&nbsp;Help&nbsp;</a></s:if><s:else><a href="javascript:openFile('<%= request.getContextPath()%>','InsHelp');">|&nbsp;Help&nbsp;</a></s:else></li>
    <li><a href="#" onmouseover="mopen('m2')" onmouseout="mclosetime()">|&nbsp;Support&nbsp;</a>
        <div id="m2" onmouseover="mcancelclosetime()" onmouseout="mclosetime()">
        <a href="javascript:openFile('<%= request.getContextPath()%>','Support');">Support Procedure</a>
        <a href="<s:url action="onlineSupport"/>">Online Support Form</a>
        </div></li>
    <li><a href="javascript:onOpenAbout();">|&nbsp;About CHOX&nbsp;</a></li>
    <li><a href="<%=request.getContextPath()%>/j_acegi_logout" >|&nbsp;<b><s:property value="CurrentUserDesc" /></b> ( Log Off )</a></li>
</ul>
<div style="clear:both"></div>
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
                                        <s:submit onclick="javascript:validateFile();" value="Upload Claims"/>
                                    </td>
                                </tr>
                            </table>     
                        </fieldset>
                    </s:form> 
                </div>
            </div>
            
<div class="footerText">
©2009 Sherwood Compliance Services Ltd | <a href="javascript:openChoxPolicyPage('<%= request.getContextPath()%>','Copyright');" class="footerText">Copyright</a> | <a href="javascript:openChoxPolicyPage('<%= request.getContextPath()%>','PrivacyPolicy');" class="footerText">Privacy Policy</a> | <a href="javascript:openChoxPolicyPage('<%= request.getContextPath()%>','TermsOfService');" class="footerText">Terms of Service</a>
</div>

        </div>
    </body>
</html> 