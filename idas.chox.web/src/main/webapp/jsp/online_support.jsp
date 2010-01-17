<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>

<head>
    <title>IDAS-CHOX</title>    
    <script type="text/javascript">
        
        $(document).ready(function () {
            
            var op = { 
                beforeSubmit:  onBeforeSubmit,
                success:       onSupportSubmitResponseReceived,
                timeout: 3000,
                error: getAcknowledgementMsg
            };

            $("#supportMessageForm").validate(
            {
                errorLabelContainer: "#errorMessageBox",                
                rules: {
                    iSubject:{
                        required:true
                    },
                    iMessage:{
                        required:true
                    }
                },
                messages: {
                    iSubject:{
                        required:"You must supply a value for 'Subject'"
                    },
                    iMessage:{
                        required:"You must supply a value for 'Message'"
                    }
                },
                submitHandler: function(form) {
                    $(form).ajaxSubmit(op);
                }
            });
        });
        
        function onBeforeSubmit(formData, jqForm, options) { 
            $.blockUI();
        }
        
        function onSupportSubmitResponseReceived(responseText, statusText)  {

            var response = eval('(' + responseText.trim() + ')');
            if(response.isValid)
            {
                getAcknowledgementMsg();
            }
            else
            {
                propmtErrors(response.errors);
            }
        }

        function onSubmitError(XMLHttpRequest, textStatus, errorThrown) {
            getAcknowledgementMsg();
        }
       
        function doFinalReset(){
            $('#iSupplierReference').val("");
            $('#iSubject').val("");
            $('#iMessage').val("");
            $.unblockUI();
            location="<%=request.getContextPath()%>/prv/inbox.action";
        }
        
        function getAcknowledgementMsg(){
            $(".block").html("<span id='online-message-acknowledgement'>Your support request has been sent successfully. A member of the CHOX support team will be in touch shortly.</span><span id='onlineMsgAck'><input type='button' value='Close' onclick='javascript:doFinalReset();'></span>");
        }
        
    </script>    
</head>

<div class="chox-claim-header x-panel-bwrap chox-form-container">   
    <fieldset class="x-fieldset">
        <legend>Online Support Form</legend>
        <form onsubmit="return true;" action="<%= request.getContextPath()%>/prv/p/submitSupportMessage.action" class="XXentity-form" method="post" id="supportMessageForm">
            <div class="form-container">
                <div class="chox-form-item">
                    <label class="chox-form-std-label">Supplier Reference</label>
                    <input type="text" class="chox-ttxt" name="iSupplierReference" id="iSupplierReference" size="10" maxlength="10" /></div>
                <div class="chox-form-item">
                    <label class="chox-form-std-label">Subject<span class="mandatory">*</span></label>
                    <input type="text" class="chox-textarea" name="iSubject" id="iSubject" size="20" maxlength="100" />
                </div>
                <div class="chox-form-item">
                    <label class="chox-form-std-label">Message<span class="mandatory">*</span></label>
                    <textarea class="chox-canote" cols="20" rows="5" name="iMessage" id="iMessage"></textarea>
                </div>
                <div class="chox-form-button">
                    <input type="submit" value="Submit"/>
                </div>
                <div id="submitResult" class="chox-form-submit-result"></div>
                <div class="action-error-msg" id="errorMessageBox"></div>
            </div>
        </form>
    </fieldset>
</div>