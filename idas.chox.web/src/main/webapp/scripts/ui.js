var ui = function(){

    var dateFormat = 'd/m/Y';
    var elementToBlock;
    var hasFormUnderSubmission = false;

    function onBeforeSubmit(formData, jqForm, options) {

        if(!hasFormUnderSubmission){

            if(elementToBlock != undefined){
                outputDiv = elementToBlock.find('div.chox-form-submit-result');
                outputDiv.text("");
                outputDiv.removeClass("submit-error");
            }

            hasFormUnderSubmission = true;
            elementToBlock = jqForm.find('div.form-container');
            elementToBlock.block({
                message: "Please wait.."
            });

            return true;

        }else{
            alert("Please wait until other save operations have completed");
        }

        return false;
    }

    function onSubmitResponseReceived(responseText, statusText)  {

        elementToBlock.unblock();
        response = eval('(' + responseText.trim() + ')');
        var outputDiv =  elementToBlock.find('div.chox-form-submit-result');
        outputDiv.html('');

        if(response)
        {
            if(response.isValid){

                if(response.resultType && response.resultType == 'New')
                {
                    var newObjectId =  parseInt(response.result);
                    var hvObjectId = elementToBlock.find("input[name='objectId']");
                    hvObjectId.val(newObjectId);
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
                outputDiv.append("<p>There was an error:</p><ul>");

                jQuery.each(response.errors, function() {
                    outputDiv.append("<li>");
                    outputDiv.append(this);
                    outputDiv.append("</li>");
                });

                outputDiv.append("</ul>");
                outputDiv.addClass("submit-error");
            }
        }
        else
        {
            outputDiv.append("Unknown Error Encountered, please try again.");
            outputDiv.addClass("submit-error");
        }

        hasFormUnderSubmission = false;
    }

    function onSubmitError(XMLHttpRequest, textStatus, errorThrown) {
        elementToBlock.unblock();
        hasFormUnderSubmission = false;
    }

    return {
        dateField : function(name,defaultValue,target) {

            var dateField = new Ext.form.DateField({
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

            return dateField;
        },
        ajaxForm : function(form,successCallBack){

            function onAfterSubmit(responseText, statusText){
                onSubmitResponseReceived(responseText, statusText);
                if(successCallBack){
                    successCallBack(responseText, statusText);
                }
            }

            form.submit(function(){

                if(form.valid()){
                    var options = {
                        beforeSubmit:  onBeforeSubmit,  // pre-submit callback
                        success:       onAfterSubmit,  // post-submit callback
                        timeout: 3000,
                        error: onSubmitError
                    };
                    $(this).ajaxSubmit(options);
                }
                return false;
            });
        }
    };
}();
