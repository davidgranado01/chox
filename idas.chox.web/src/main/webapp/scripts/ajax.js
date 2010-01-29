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
    var REDIRECT_ON_SESSION_TIMEOUT_URL = 'login.action';
    var AJAX_GENERAL_ERROR_MSG = 'We encountered a problem processing this request, please try again.';
    var AJAX_SESSION_TIMEOUT_ERROR_MSG = 'Your session has timed out, please login again.';
    var HTTP_SESSION_TIMEOUT_STATUS = 401;

    function checkResponse(textStatus)
    {
        if(textStatus == 'success') {
            return true;
        }
        handleGeneralError(textStatus);
        return false;
    }
    
    function checkJSONResponse(response)
    {
        if(response.isValid) {
            return true;
        }
        handleGeneralErrors(response.Errors);
        return false;
    }

    function handleGeneralErrors(errors)
    {
        if(SHOW_ERROR_MSG)
        {
            if(errors){
                ui.promptErrorsMsg(errors);
            }
            else{
                ui.promptErrorMsg(AJAX_GENERAL_ERROR_MSG);
            }
        }
    }

    function handleGeneralError(msg)
    {
        if(SHOW_ERROR_MSG)
        {
            if(msg){
                ui.promptErrorMsg(msg);
            }
            else{
                ui.promptErrorMsg(AJAX_GENERAL_ERROR_MSG);
            }
        }
    }

    function handleSessionTimeoutError()
    {
        if(confirm(AJAX_SESSION_TIMEOUT_ERROR_MSG)){
            window.location = REDIRECT_ON_SESSION_TIMEOUT_URL;
        }
    }

    function loadHtml(url,param,success,error) {

        $.post(url,param,function(data,textStatus){
            if(checkResponse(textStatus)){               
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

            if(checkResponse(textStatus) && checkJSONResponse(data)){
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

        //if session time out : server return error status 401
        if(response.status == HTTP_SESSION_TIMEOUT_STATUS){
            //redirect user back to login page
            handleSessionTimeoutError();
        }
        else{
            handleGeneralError();
        }
    }

    return {
        loadHtml : loadHtml,
        loadJson : loadJson,
        handleAjaxError : handleAjaxError
    };
}();





