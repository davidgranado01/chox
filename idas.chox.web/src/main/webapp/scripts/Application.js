function getRandomNumber() {
    var min = 10000000;
    var max = 99999999;
    return (Math.round((max-min) * Math.random() + min));
}

function doSectionLoad(location, action, pamareter){
    $(location).load(action+"?"+pamareter+"&urdn="+getRandomNumber());
}

var choxDataStore;
var choxUpdateEl;
var choxExtJsFormPanel;

Ext.onReady(function() {
    /*
     * 
     * Ext.data.Store use the url parameter(if found) to make the request, but we want to use  
     * HttpProxy connection object to make the request, so apply empty string '' to the url value when constructing the store. 
     * 
     * Ext.data.Store baseParams overrides/hides HttpProxy params. So we needed to 
     * apply the passed in params value to the baseParams value using by the Ext.apply method.
     * 
     */
    choxDataStore = function(config) {
        Ext.apply(this, {url : ''}, config);
        choxDataStore.superclass.constructor.call(this, { 
            proxy: new Ext.data.HttpProxy({url: contextPath + config.url, method: 'POST', timeout : config.timeout ? config.timeout : 60000})
        });
        
        // The below code is not necessery as exception is handled by the handleGeneralError method in ajax.js function. 
        /*this.on('exception', function(ex, type, action, options, response, arg) {
            // Do not show the error message if the page is refereshed or the user navigated to different page before this request get response from server. 
            if (response.status !== 0) {
                Ext.MessageBox.show({
                    title: 'Internal Error Occurred',
                    msg: "We encountered a problem processing this request, please try again.",
                    width: 300,
                    buttons: Ext.MessageBox.OK,
                    icon: Ext.MessageBox.ERROR
    //                ,fn: function(){location.reload();}
                });
            }
        });*/
        
        this.on('beforeload', function(store,records,options) {
                var temporaryParams = {};
                if (isCsrfParamActive()) {
                    // http://edspencer.net/2008/08/27/how-extapply-works-and-how-to-avoid-big/
                    Ext.apply(temporaryParams, config.params, store.baseParams);
                    store.baseParams = Ext.apply(temporaryParams, csrfParam);
                }else{
                    store.baseParams = Ext.apply(temporaryParams, config.params, store.baseParams);
                }
        }, this);
    };

    Ext.extend(choxDataStore, Ext.data.Store, {
    });
    
    choxExtJsFormPanel = function(config) {
        var baseParams;
        if (isCsrfParamActive()) { baseParams = Ext.apply({}, csrfParam, config.baseParams);}
        else {baseParams = Ext.apply({}, config.baseParams);}
        Ext.apply(this, config);
        choxExtJsFormPanel.superclass.constructor.call(this, {baseParams : baseParams});
    };

    Ext.extend(choxExtJsFormPanel, Ext.FormPanel, {
    });

    choxUpdateEl = function(config) {
        if (isCsrfParamActive()) {
            return {
                url:contextPath + config.url, 
                method: 'POST',
                params : Ext.apply({}, csrfParam, config.params),
                scripts:true, 
                text : config.text || '',
                callback : config.callback
            }
        }else{
            return {
                url:contextPath + config.url, 
                method: 'POST',
                params : config.params,
                scripts:true, 
                text : config.text || '',
                callback : config.callback
            }
        }
    };

    Ext.reg('choxDataStore', choxDataStore);
    Ext.reg('choxExtJsFormPanel', choxExtJsFormPanel);
   
});

function isCsrfParamActive() {
    return true;
}
function isCsrfParamPresent() {
    if (!isCsrfParamActive()){return true;}
    if (!csrfParam ||!csrfParameterName || !csrfParam[csrfParameterName]) {
        Ext.MessageBox.show({
            title: 'Internal Error Occurred',
            msg: 'Data can not be loaded for this request. Please try again.',
            width:300,
            buttons: Ext.MessageBox.OK,
            icon : Ext.MessageBox.ERROR
        });
        return false;
    } else {
        return true;
    }
}

function logout() {
    var logoutURL = contextPath + '/logout';
    
    var form = $('<form action="' + logoutURL + '" method="post">'+
                        '</form>');
                    $('body').append(form);
                    choxJqueryHttpSubmit($(form));
                    
}

// jquery way of submitting http form.
function loadHome() {
    var homeURL = contextPath + '/prv/inbox.action';
    var form = $('<form action="' + homeURL + '" method="post"> </form>');
    $('body').append(form);
    choxJqueryHttpSubmit($(form));
}

function loadClaimDetail(claimId) {
    var claimDetailURL = contextPath + '/prv/openClaimDetail.action?id=' + claimId;
    var form = $('<form action="' + claimDetailURL + '" method="post"> </form>');
    $('body').append(form);
    choxJqueryHttpSubmit($(form));
}


function loadInbox(loadPreviouslyOpenedTabFromSession) {
    var inboxPageUrl = contextPath + '/prv/inbox.action';
    if (loadPreviouslyOpenedTabFromSession) {
        inboxPageUrl = inboxPageUrl + '?showHistory=1'
    } 
    var form = $('<form action="' + inboxPageUrl + '" method="post"> </form>');
    $('body').append(form);
    choxJqueryHttpSubmit($(form));
}

function choxExtAjaxRequest(extAjaxconfig) {
    isCsrfParamPresent();
    var params;
    if (isCsrfParamActive()){
        params = Ext.apply({}, csrfParam, extAjaxconfig.params);
    }else{
        params = Ext.apply({}, extAjaxconfig.params);
    }
    extAjaxconfig.params = params;
    extAjaxconfig.url = contextPath + extAjaxconfig.url;
    Ext.Ajax.request(extAjaxconfig);
}

function choxJqueryAjaxSubmit(form, config) {
    isCsrfParamPresent();
    var params;
    if (isCsrfParamActive()){
        params = Ext.apply({}, csrfParam, config.data);
    }else{
        params = Ext.apply({}, config.data);
    }
    config.data = params;
    config.type = 'POST';
    form.ajaxSubmit(config);
}

function choxJqueryHttpSubmit(form, callbackFunction) {
    isCsrfParamPresent();
    if (isCsrfParamActive()){
        var input = $("<input>").attr("type", "hidden").attr("name", csrfParameterName).val(csrfTokenValue);
        form.append($(input));
    }
    (callbackFunction) ? form.submit(callbackFunction) : form.submit();
}
