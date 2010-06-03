//Javascript lib for CHOX

//Global event handle : handle jquery ajax exception
$(function(){
    $(this).ajaxError(ajax.handleAjaxError);
});

//Global event handle : handle ExtJs ajax exception
Ext.onReady(function(){
    Ext.Ajax.on('requestexception',ajax.handleAjaxError, this);
});

var ajax = function() {

    var SHOW_ERROR_MSG = true;
    var SHOW_AJAX_GENERAL_ERROR_MSG = true;
    var REDIRECT_ON_SESSION_TIMEOUT_URL = 'login.action';
    var REDIRECT_ON_ACCESS_DENIED = '/j_spring_security_logout';
    var REDIRECT_ON_ACCESS_DENIED1 = 'j_spring_security_logout';
    var AJAX_GENERAL_ERROR_MSG = 'We encountered a problem processing this request, please try again.';
    var AJAX_SESSION_TIMEOUT_ERROR_MSG = 'Your session has timed out, please login again.';
    var AJAX_DENIED_ACCESS_ERROR_MSG = 'You have been denied access. You will now be logged out - please login again.';
    var HTTP_SESSION_TIMEOUT_STATUS = 401;
    var HTTP_ACCESS_DENIED_STATUS = 403;
    var HTTP_NOT_FOUND_STATUS = 404;
    var lastResponse = -1;
    function setLastResponse(resp){
        lastResponse = resp;
    }
    function checkResponse(textStatus)
    {
//        console.log("In checkResponse: " + textStatus);
        if(textStatus == 'success') {
            return true;
        }
//        console.log("Non-'success' encountered in checkResponse: " + textStatus);
        handleGeneralError(textStatus);
        return false;
    }
    
    function checkJSONResponse(response)
    {
//        console.log("In checkJSONResponse: " + response);
        if(response.isValid) {
            
            return true;
        }
//        console.log("response not valid encountered in checkJSONResponse: " + response);
        handleGeneralErrors(response.Errors);
        return false;
    }

    function handleGeneralErrors(errors)
    {
//        console.log("In handleGeneralErrors: " + errors);
        if(SHOW_ERROR_MSG)
        {
            if(errors){
                ui.promptErrorsMsg(errors);
            }
            else{
                // ui.promptErrorMsg(AJAX_GENERAL_ERROR_MSG);
//        console.log("In handleGeneralErrors with no errors");
                if (SHOW_AJAX_GENERAL_ERROR_MSG) {
                    alert(AJAX_GENERAL_ERROR_MSG);
                }
            }
        }
    }

    function handleGeneralError(msg)
    {
//        console.log("In handleGeneralError: " + msg);
        if(SHOW_ERROR_MSG)
        {
            if(msg){
                ui.promptErrorMsg(msg);
            }
            else{
                // ui.promptErrorMsg(AJAX_GENERAL_ERROR_MSG);
//        console.log("In handleGeneralError with no message");
                if (SHOW_AJAX_GENERAL_ERROR_MSG) {
                    alert(AJAX_GENERAL_ERROR_MSG);
                }
            }
        }
    }

    function handleSessionTimeoutError()
    {
        alert(AJAX_SESSION_TIMEOUT_ERROR_MSG);
        window.location = REDIRECT_ON_SESSION_TIMEOUT_URL;
    }

    function handleAccessDeniedError()
    {
        alert(AJAX_DENIED_ACCESS_ERROR_MSG);
        window.location = REDIRECT_ON_ACCESS_DENIED;

    }

    function loadHtml(url,param,success,error) {

        $.post(url,param,function(data,textStatus){
            // Hack to handle access denied returned in the ajax response
            if (data.indexOf('You have been denied access') !=-1) {
//                console.log("Access Denied detected");
                Ext.MessageBox.alert('Error', 'You have been denied access and will now be logged out', function() {
                    window.location = '/j_spring_security_logout';
                    return;
                });
            }
            else if(checkResponse(textStatus)){
                lastResponse = 1;
                if(success){

                    success(data);

                }
            }
            else{
                if(error){

                    error(data);
                }
            }
        },'html');
    }

    function loadJson(url,param,success,error){
        $.post(url,param,function(data,textStatus){
            if (data.indexOf('You have been denied access') !=-1) {
                Ext.MessageBox.alert('Error', 'You have been denied access and will now be logged out', function() {
                    window.location = '/j_spring_security_logout';
                    return;
                });
            }
            else if(checkResponse(textStatus) && checkJSONResponse(data)){
                lastResponse = 1;
                if(success){
                    success(data);
                }
            }
            else{
                if(error){

                    error(data);
                }
            }
        },'json');
    }

    function handleAjaxError(conn, response, options){
//        console.log("handleAjaxError: response status is:" + response.status);
        if ( response.status == 0 && lastResponse != 0 ){
            lastResponse = response.status;
            alert('Possible internet/network connection error. Please check connection.');
        } else if (response.status == 0 && lastResponse == 0 ){
            
        }
        //if session time out : server return error status 401
        else if(response.status == HTTP_SESSION_TIMEOUT_STATUS){
            lastResponse = response.status;
            //redirect user back to login page
            handleSessionTimeoutError();
        }
        else if(response.status == HTTP_ACCESS_DENIED_STATUS){
            lastResponse = response.status;
            handleAccessDeniedError();
        }
        else if(response.status == HTTP_NOT_FOUND_STATUS){ // We'll treat this as an access denied error (for now)'
            lastResponse = response.status;
            handleAccessDeniedError();
        }
        else{
            lastResponse = response.status;
//          console.log("handleAjaxError called with response status: " + response.status);
            handleGeneralError();
        }

    }

    return {
        loadHtml : loadHtml,
        loadJson : loadJson,
        handleAjaxError : handleAjaxError,
        setLastResponse : setLastResponse
    };
}();