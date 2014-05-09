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
            proxy: new Ext.data.HttpProxy({url: contextPath + config.url, method: 'POST'})
        });
        this.on('beforeload', function(store,records,options) {
                var temporaryParams = {};
                if (isCsrfParamPresent()) {
                    // http://edspencer.net/2008/08/27/how-extapply-works-and-how-to-avoid-big/
                    Ext.apply(temporaryParams, config.params, store.baseParams);
                    store.baseParams = Ext.apply(temporaryParams, csrfParam);
                }
        }, this);
    };

    Ext.extend(choxDataStore, Ext.data.Store, {
    });
    
    choxExtJsFormPanel = function(config) {
        var baseParams = Ext.apply({}, csrfParam, config.baseParams);
        Ext.apply(this, config);
        choxExtJsFormPanel.superclass.constructor.call(this, {baseParams : baseParams});
    };

    Ext.extend(choxExtJsFormPanel, Ext.FormPanel, {
    });

    choxUpdateEl = function(config) {
        isCsrfParamPresent();
        return {
            url:contextPath + config.url, 
            method: 'POST',
            params : Ext.apply({}, csrfParam, config.params),
            scripts:true, 
            text : config.text || ''
        }
    };

    Ext.reg('choxDataStore', choxDataStore);
    Ext.reg('choxExtJsFormPanel', choxExtJsFormPanel);
   
});

function isCsrfParamPresent() {
    if (!csrfParam || !csrfParam[csrfParameterName]) {
        Ext.MessageBox.show({
            title: 'Error',
            msg: 'Authentication token not found. Data can not be loaded for this request. Please try again.',
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
    var logoutURL = contextPath + '/j_spring_security_logout';
    
    var form = $('<form action="' + logoutURL + '" method="post">'+
                        '</form>');
                    $('body').append(form);
//                    $(form).submit();
                    choxJqueryHttpSubmit($(form));
                    
//    choxExtAjaxRequest({
//        url: logoutURL,
////        params: csrfParam,
//        success: function(options, success, response) {
//                    window.location = loginURL;
//                 },
//        failure: function(options, success, response) {
//                    Ext.MessageBox.show({
//                        title: 'Error',
//                        msg: 'Logout Action Failed.',
//                        width:300,
//                        buttons: Ext.MessageBox.OK,
//                        icon : Ext.MessageBox.ERROR
//                    });
//                    window.location = loginURL;
//                 }
//    });
}

// jquery way of submitting http form.
function loadHome() {
    var homeURL = contextPath + '/prv/inbox.action';
    var form = $('<form action="' + homeURL + '" method="post">' +
                        '<input type="hidden" name="showHistory" value="'+ 1 +'"/>' +
                        '</form>');
                    $('body').append(form);
                    choxJqueryHttpSubmit($(form));
}

// Extjs way of submitting http form. This can be shortened as loadHome() method.
function loadClaimDetail(claimId, tabIndex) {
    isCsrfParamPresent();
    var claimDetailPageUrl = contextPath + '/prv/openClaimDetail.action';
    var tempParams = {};
    if (claimId) {
        tempParams['id'] = claimId;
    }
    if (tabIndex) {
        tempParams['tab'] =  tabIndex;
    } 
    if (Ext.get('claimDetailScreenDiv')) {
        Ext.get('claimDetailScreenDiv').mask("Loading Please Wait...");
    }
    var claimForm = new Ext.FormPanel({
        standardSubmit: true,
        baseParams: Ext.apply(tempParams, csrfParam),
        url: claimDetailPageUrl,
        renderTo : Ext.getBody( ),
        listeners:  {
            afterrender:function(form){
                for (i in form.baseParams) {
                    form.add({
                        xtype: 'hidden',
                        name: i,
                        value: form.baseParams[i]
                    });
                }
            }
        }
    });
    claimForm.doLayout();
    claimForm.getForm().submit();
}

// Use this loading inbox function only for token missing error. Otherwise please use loadInbox function.
function loadInboxGetRequest() {
    window.location = contextPath + '/prv/inbox.action?showHistory=1';
}

// Extjs way of submitting http form. This can be shortened as loadHome() method.
function loadInbox(loadPreviouslyOpenedTabFromSession) {
    isCsrfParamPresent();
    var inboxPageUrl = contextPath + '/prv/inbox.action';
    var tempParams = {};
    if (loadPreviouslyOpenedTabFromSession) {
        tempParams['showHistory'] = 1;
    } 
    var inboxForm = new Ext.FormPanel({
        standardSubmit: true,
        baseParams: Ext.apply(tempParams, csrfParam),
        url: inboxPageUrl,
        renderTo : Ext.getBody( ),
        listeners:  {
            afterrender:function(form){
                for (i in form.baseParams) {
                    form.add({
                        xtype: 'hidden',
                        name: i,
                        value: form.baseParams[i]
                    });
                }
            }
        }
    });
    inboxForm.doLayout();
    inboxForm.getForm().submit();
}

function choxExtAjaxRequest(extAjaxconfig) {
    isCsrfParamPresent();
    var params = Ext.apply({}, csrfParam, extAjaxconfig.params);
    extAjaxconfig.params = params;
    extAjaxconfig.url = contextPath + extAjaxconfig.url;
    Ext.Ajax.request(extAjaxconfig);
}

function choxJqueryAjaxSubmit(form, config) {
    isCsrfParamPresent();
    var params = Ext.apply({}, csrfParam, config.data);
    config.data = params;
    config.type = 'POST';
    form.ajaxSubmit(config);
}

function choxJqueryHttpSubmit(form, callbackFunction) {
    isCsrfParamPresent();
    var input = $("<input>").attr("type", "hidden").attr("name", csrfParameterName).val(csrfTokenValue);
    form.append($(input));
    (callbackFunction) ? form.submit(callbackFunction) : form.submit();
}
