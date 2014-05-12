//Javascript lib for CHOX

//Global event handle : handle jquery ajax exception
Ext.onReady(function() {
    $(this).ajaxError(ajax.handleAjaxError);
});

//Global event handle : handle ExtJs ajax exception
Ext.onReady(function(){
    Ext.Ajax.on('requestexception',ajax.handleAjaxError, this);
});

var ajax = function() {

    var SHOW_ERROR_MSG = true;
    var SHOW_AJAX_GENERAL_ERROR_MSG = false;
    var REDIRECT_ON_SESSION_TIMEOUT_URL = 'login.action';
    var REDIRECT_ON_ACCESS_DENIED = '/j_spring_security_logout';
    var AJAX_GENERAL_ERROR_MSG = 'We encountered a problem processing this request, please try again.';
    var INVALID_CSRF_TOKEN_ERROR_MSG = 'Request can not be completed. Please try again.';
    var AJAX_SESSION_TIMEOUT_ERROR_MSG = 'Your session has timed out, please login again.';
    var AJAX_DENIED_ACCESS_ERROR_MSG = 'You have been denied access. You will now be logged out - please login again.';
    var HTTP_SESSION_TIMEOUT_STATUS = 418;
    var INVALID_CSRF_TOKEN_STATUS = 417;
    var HTTP_ACCESS_DENIED_STATUS = 401;
    var HTTP_NOT_FOUND_STATUS = 404;
    var lastResponse = -1;
    function setLastResponse(resp){
        lastResponse = resp;
    }
    function checkResponse(textStatus)
    {
        if(textStatus == 'success') {
            return true;
        }
        else if (textStatus == 'session.expired') {
            return false;
        }
        else if (textStatus == 'AccessDenied') {
            return false;
        }
        else if (textStatus == 'invalid.token') {
            return false;
        }
        else if (textStatus == 'Exception') {
            return false;
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
                // ui.promptErrorMsg(AJAX_GENERAL_ERROR_MSG);
                if (SHOW_AJAX_GENERAL_ERROR_MSG) {
                    Ext.MessageBox.show({
                        title: 'Error',
                        msg: AJAX_GENERAL_ERROR_MSG,
                        width:300,
                        buttons: Ext.MessageBox.OK,
                        icon : Ext.MessageBox.ERROR
                    });
                }
            }
        }
    }

    function handleInvalidCsrfError()
    {
        Ext.MessageBox.show({
            title: 'Authentication Token Not Found',
            msg: INVALID_CSRF_TOKEN_ERROR_MSG,
            width: 300,
            buttons: Ext.MessageBox.OK,
            icon: Ext.MessageBox.ERROR,
            fn: function(){location.reload();}
        });
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
                    Ext.MessageBox.show({
                        title: 'Error',
                        msg: AJAX_GENERAL_ERROR_MSG,
                        width:300,
                        buttons: Ext.MessageBox.OK,
                        icon : Ext.MessageBox.ERROR
                    });
                }
            }
        }
    }

    function handleSessionTimeoutError()
    {
        activityMonitor.clearViewingStatus();
        Ext.MessageBox.show({
            title: 'Error',
            msg: AJAX_SESSION_TIMEOUT_ERROR_MSG,
            width:300,
            buttons: Ext.MessageBox.OK,
            icon : Ext.MessageBox.ERROR,
            fn: function redirectToLoginPage(){
                window.location = REDIRECT_ON_SESSION_TIMEOUT_URL; 
            }
        });
    }

    function handleAccessDeniedError()
    {
        activityMonitor.clearViewingStatus();
        Ext.MessageBox.show({
            title: 'Error',
            msg: AJAX_DENIED_ACCESS_ERROR_MSG,
            width:300,
            buttons: Ext.MessageBox.OK,
            icon : Ext.MessageBox.ERROR,
            fn: function redirectToAccessDeniedPage(){
//                window.location = REDIRECT_ON_ACCESS_DENIED; 
                logout();
            }
        });
    }


    function loadHtml(url,param,success,error) {
        // Add nonce value
        if (typeof(param) == typeof('')) {
            // $(form).serialize() return a string
//            param += 'nonce='+$('#uniqueNonceId').val();
            param += csrfParameterName+'='+csrfTokenValue;
        } else {
//            param['nonce'] = $('#uniqueNonceId').val();
            param[csrfParameterName] = csrfTokenValue;
        }
        url = contextPath + url;
        $.post(url,param,function(data,textStatus){
            // Hack to handle access denied returned in the ajax response
            if (typeof data.indexOf == 'function'  && data.indexOf('You have been denied access') !=-1) {
                Ext.MessageBox.show({
                    title: 'Error',
                    msg: AJAX_DENIED_ACCESS_ERROR_MSG,
                    width:300,
                    buttons: Ext.MessageBox.OK,
                    icon : Ext.MessageBox.ERROR,
                    fn: function redirectToAccessDeniedPage(){
//                        window.location = REDIRECT_ON_ACCESS_DENIED; 
                        logout();
                    }
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
                    handleAjaxError(error);
                    error(data);
                }
            }
        },'html');
    }

    function loadJson(url,param,success,error){
        // Add nonce value
        if (typeof(param) == typeof('')) {
            // $(form).serialize() return a string
//            param += 'nonce='+$('#uniqueNonceId').val();
            param += csrfParameterName+'='+csrfTokenValue;
        } else {
//            param['nonce'] = $('#uniqueNonceId').val();
            param[csrfParameterName] = csrfTokenValue;
        }
        url = contextPath + url;
        $.post(url,param,function(data,textStatus){
            if (typeof data.indexOf == 'function'  && data.indexOf('You have been denied access') !=-1) {
                Ext.MessageBox.show({
                    title: 'Error',
                    msg: AJAX_DENIED_ACCESS_ERROR_MSG,
                    width:300,
                    buttons: Ext.MessageBox.OK,
                    icon : Ext.MessageBox.ERROR,
                    fn: function redirectToAccessDeniedPage(){
//                        window.location = REDIRECT_ON_ACCESS_DENIED; 
                        logout();
                    }
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

    function handleAjaxError(conn, response, options, thrownError){

        if ( response.status == 0 && lastResponse != 0 ){
            lastResponse = response.status;
        /*
             * removed this alert message as this is poping out all the times when moving into another page when the current page is still loading.
             */
        //            alert('Possible internet/network connection error. Please check connection.');
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
        else if(response.status == INVALID_CSRF_TOKEN_STATUS){
            lastResponse = response.status;
            handleInvalidCsrfError();
        }
        else{
            lastResponse = response.status;
            handleGeneralError();
        }

    }

    return {
        loadHtml2 : loadHtml,
        loadJson2 : loadJson,
        handleAjaxError : handleAjaxError,
        setLastResponse : setLastResponse
    };
}();