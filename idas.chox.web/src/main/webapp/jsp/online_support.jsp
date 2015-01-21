<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>

<head>
    <title>CHOX</title>
    <script type="text/javascript">


        Ext.onReady(function() {

            Ext.QuickTips.init();

            var SupplierReferenceField = new Ext.form.TextField({

                name             : 'iSupplierReference',
                id               : 'iSupplierReference',
                width            :  200,
                allowBlank       :  true,
                renderTo         : 'SupplierReferenceId'
            }).show();

            var phoneField = new Ext.form.TextField({
                name             : 'iPhone',
                id               : 'iPhone',
                width            :  200,
                allowBlank       :  false,
                renderTo         : 'phoneAddressId'
               
            });

            var EmailField = new Ext.form.TextField({
                name             : 'iEmail',
                id               : 'iEmail',
                width            :  200,
                allowBlank       :  false,
                renderTo         : 'EmailAddressId'
            });

            var SubjectField = new Ext.form.TextField({
                name             : 'iSubject',
                id               : 'iSubject',
                width            :  300,
                allowBlank       :  false,
                renderTo         : 'SubjectId'
            });

            var MessageField = new Ext.form.TextArea({
                name             : 'iMessage',
                id               : 'iMessage',
                width            :  500,
                height           :  200,
                allowBlank       :  false,
                renderTo         :  'MessageAreaId'
            });

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
                    },
                    iPhone:{
                        required:true
                    },
                    iEmail:{
                        required:true
                    }
                },
                messages: {
                    iSubject:{
                        required:"You must supply a value for 'Subject'"
                    },
                    iMessage:{
                        required:"You must supply a value for 'Message'"
                    },
                    iPhone:{
                        required:"You must supply a value for 'Phone'"
                    },
                    iEmail:{
                        required:"You must supply a value for 'Email'"
                    }
                },
                submitHandler: function(form) {
//                    $(form).ajaxSubmit(op);
                    choxJqueryAjaxSubmit($(form), op);
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
            $('#iPhone').val("");
            $('#iEmail').val("");
            $.unblockUI();
            window.close();
        }

        function closeForm(){
            Ext.MessageBox.confirm('Confirm', 'Are you sure you want to close the form?',doClose);
            function doClose(btn){
                if(btn==='yes') {
                    window.close();
                }
                return false;
            }
        }

        function getAcknowledgementMsg(){
            $(".block").html("<span id='online-message-acknowledgement'>Your support request has been sent successfully. A member of the CHOX support team will be in touch shortly.</span><span id='onlineMsgAck'><input type='button' value='Close' onclick='javascript:doFinalReset();'></span>");
        }

    </script>
</head>

<div class="chox-form-container">
    <fieldset class="x-fieldset">
        <legend>Online Support Form</legend>
        <form action="<%= request.getContextPath()%>/prv/p/submitSupportMessage.action" class="XXentity-form" method="post" id="supportMessageForm">
            <div class="form-container">
                <div class="chox-form-item">
                    <label class="chox-form-std-label">Supplier Reference</label>
                    <span id="SupplierReferenceId"></span>
                </div>
                <div class="chox-form-item">
                    <label class="chox-form-std-label">Phone<span class="mandatory">*</span></label>
                    <span  id="phoneAddressId" ></span>

                </div>
                <div class="chox-form-item">
                    <label class="chox-form-std-label">Email<span class="mandatory">*</span></label>
                    <span id="EmailAddressId"></span>
                </div>
                <div class="chox-form-item">
                    <label class="chox-form-std-label">Subject<span class="mandatory">*</span></label>
                    <span id="SubjectId"></span>
                </div>
                <div class="chox-form-item">
                    <label class="chox-form-std-label">Message<span class="mandatory">*</span></label>
                    <span id="MessageAreaId"></span>
                </div>
                <div class="chox-form-button">
                    <input type="submit" id="onlineSupportSubmitButtonId" value="Submit"/>
                    <input type="button" value="close" onclick=" closeForm();"/>
                </div>
                <div id="submitResult" class="chox-form-submit-result"></div>
                <div class="action-error-msg" id="errorMessageBox"></div>
            </div>
        </form>
    </fieldset>
</div>