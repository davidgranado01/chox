var ui = function(){

    var dateFormat = 'd/m/Y';
    var elementToBlock;
    var hasFormUnderSubmission = false;

    function onBeforeSubmit(formData, jqForm, options) {
        if(!hasFormUnderSubmission){

            if(elementToBlock){
                outputDiv = elementToBlock.find('div.chox-form-submit-result');
                outputDiv.text("");
                outputDiv.removeClass("submit-error");
            }

            hasFormUnderSubmission = true;

            elementToBlock = jqForm.find('div.form-container');
            if(elementToBlock)
            {
                elementToBlock.block({
                    message: "Please wait.."
                });
            }

            return true;

        }else{
            return false;
//            Ext.MessageBox.alert('',"Please wait until other save operations have completed");
        }

        return false;
    }

    function onSubmitCompleted(responseText, statusText, form, responseType)  {
//        console.log('In onSubmitCompleted');
        // Hack to handle access denied returned in the ajax response
        if (responseText.indexOf('You have been denied access') !=-1) {
            Ext.MessageBox.alert('Error', 'You have been denied access and will now be logged out', function() {
//                window.location = '/j_spring_security_logout';
                logout();
                return;
            });
        }

        if(elementToBlock)
        {
            elementToBlock.unblock();
            var outputDiv =  elementToBlock.find('div.chox-form-submit-result');

            if(responseText && responseType == 'html')
            {
                $(form).parent().html(responseText);
            }
            else if(responseText)
            {
                var response = eval('(' + responseText.trim() + ')');
                if(response && response.isValid){
                    if(response.resultType && response.resultType == 'New')
                    {
                        var newObjectId =  parseInt(response.result);
                        var hvObjectId = elementToBlock.find("input[name='objectId']");
                        hvObjectId.val(newObjectId);
                        outputDiv.append("<p>Your changes have been saved.</p>");
                    }
                    else if(response.resultType && response.resultType == 'Message')
                    {
                        outputDiv.append("<p>" + response.result + "</p>");
                    }
                    else
                    {
                        outputDiv.append("<p>Your changes have been saved.</p>");
                    }

                }
                else
                {
                    outputDiv.append('<p>There was an error:</p><ul class="submit-error">');

                    $.each(response.errors, function() {
                        outputDiv.append("<li>");
                        outputDiv.append(this.toString());
                        outputDiv.append("</li>");
                    });

                    outputDiv.append("</ul>");
                }
            }
            else
            {
                outputDiv.append("Unknown Error Encountered, please try again.");
                outputDiv.addClass("submit-error");
            }
        }

        hasFormUnderSubmission = false;
    }

    function onSubmitError(XMLHttpRequest, textStatus, errorThrown) {
        if(elementToBlock)
        {
            elementToBlock.unblock();
            var outputDiv =  elementToBlock.find('div.chox-form-submit-result');
            outputDiv.html('');
        }

        hasFormUnderSubmission = false;
        ajax.handleAjaxError(null, XMLHttpRequest);
    }

    function parseErrors(errors)
    {
        var errorMsg = "<ul>";
        $.each(errors, function() {
            errorMsg += "<li>";
            errorMsg += this;
            errorMsg +="</li>";
        });
        errorMsg +="</ul>";
        return errorMsg;
    }

    function unvalidatedDateField(name,defaultValue,target) {
    	return new Ext.form.DateField({
            name: name,
            id: name,
            width: 100,
            allowBlank: true,
            format: dateFormat,
            showWeekNumber: true,
            validationEvent : false,
            value: defaultValue,
            renderTo:target
        });
    }
    
    function createDateField(name,defaultValue,target) {

        var dateField = new Ext.form.DateField({
            name: name,
            id: name,
            width: 100,
            allowBlank: true,
            format: dateFormat,
            showWeekNumber: true,
            validationEvent : true,
            value: defaultValue,
            renderTo:target,
            msgTarget : 'qtip'
        });

        return dateField;
    }

    // refer the below link to know how the jquery ajax form submit callback handler works 
    // http://api.jquery.com/submit/
    function ajaxForm(form, successCallBack, responseType){

        function onAfterSubmit(responseText, statusText){
            onSubmitCompleted(responseText, statusText,form,responseType);
            if(successCallBack){
                successCallBack(responseText, statusText,form,responseType);
            }
        }

        form.submit(function(event){
            
            if(form.valid()){
                var options = {
                    beforeSubmit: onBeforeSubmit,  // pre-submit callback
                    success: onAfterSubmit,  // post-submit callback
                    timeout: 3000,
                    error: onSubmitError,
                    data: csrfParam,
                    type : 'POST'
                };
                $(this).ajaxSubmit(options);
            }
//            event.preventDefault(); This can also be used to prevent default form submission.
            return false;
        });
    }

    function promptMsg(title,msg)
    {
        Ext.MessageBox.show({
            title: title,
            msg: msg,
            width : 400,
            buttons: Ext.MessageBox.OK
        });
    }

    function promptErrorMsg(errorMsg)
    {
        Ext.Msg.show({
            title: 'Error',
            msg:Ext.util.Format.ellipsis(errorMsg, 2000),
            icon:Ext.Msg.ERROR,
            buttons:Ext.Msg.OK,
            width : 400
        });
    }

    function promptErrorsMsg(errors)
    {
        var errorMsg = parseErrors(errors);
        ui.promptErrorMsg(errorMsg);
    }

    return {
        dateField : createDateField,
        unvalidatedDateField : unvalidatedDateField,
        ajaxForm : ajaxForm,
        promptMsg : promptMsg,
        promptErrorMsg :promptErrorMsg,
        promptErrorsMsg : promptErrorsMsg
    };
}();

Ext.onReady(function(){
    var blockUIBackground = {
            backgroundColor:'#6c8cbe',
            opacity:        '0.5'
    };
        
    if ((isFullBranding != undefined && isFullBranding) || (isBrandingClaim != undefined && isBrandingClaim)) {
        blockUIBackground = {
            backgroundColor:'#198A6C',
            opacity:        '0.5'
        };
    }

    $.blockUI.defaults = {
        message:  '<h1 class="block">Please wait...</h1>',

        css: {
            padding:        '10px',
            margin:         0,
            width:          '30%',
            top:            '10%',
            left:           '35%',
            textAlign:      'center',
            color:          '#000',
            border:         '3px solid #aaa',
            backgroundColor:'#fff',
            cursor:         'wait' ,
            height: 'auto'
        },

        overlayCSS:  blockUIBackground,

        baseZ: 1000,
        centerX: true,
        centerY: true,
        allowBodyStretch: true,
        constrainTabKey: true,
        fadeOut:  0,
        applyPlatformOpacityRules: true
    };
    
})

