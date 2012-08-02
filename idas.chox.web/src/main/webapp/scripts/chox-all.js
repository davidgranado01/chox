
var newwindow;
var strDateFormat = 'd/m/Y';

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

    overlayCSS:  {
        backgroundColor:'#6c8cbe',
        opacity:        '0.5'
    },

    baseZ: 1000,
    centerX: true,
    centerY: true,
    allowBodyStretch: true,
    constrainTabKey: true,
    fadeOut:  0,
    applyPlatformOpacityRules: true
};

var timeout	= 500;
var closetimer	= 0;
var ddmenuitem	= 0;

function openHelpFile(url, helpFileRoleType, bespoke){

    var folderPath = url;
    var fileName = "";



    switch(helpFileRoleType) {
        case 1: // NORMAL INSURER ROLE
            if (bespoke)
                fileName = '/download/CHOX_IUG_S_3.5.pdf';
            else
                fileName = '/download/CHOX_IUG_3.5.pdf';
            break;
        case 2: // INSURER MANAGER ROLE
            if (bespoke)
                fileName = '/download/CHOX_IUG_S_ADM_3.5.pdf';
            else
                fileName = '/download/CHOX_IUG_ADM_3.5.pdf';
            break;
        case 3: // NORMAL CREDIT HIRE ROLE
            fileName = '/download/CHOX_CHO_UG_3.6.pdf';
            break;
        case 4: // CREDIT HIRE MANAGER ROLE
            fileName = '/download/CHOX_CHO_UG_ADM_3.6.pdf';
            break;
    }

    if(fileName.length>0){
        openFile(folderPath+fileName);
    }
}

function openSupportFile(url, supportFile) {
    var folderPath = url+ supportFile;
    openFile(folderPath);
}

function openFile(folderPath){

    newwindow=window.open(folderPath, 'CHOX');
    if (window.focus) {
        newwindow.focus();
    }

}

function getTodayDate(){
    var now = new Date();
    return now.format(strDateFormat);
}

function openChoxPolicyPage(url, name){
    var folderPath = url;
    if(name=='TermsOfService'){
        folderPath = folderPath + '/terms_of_service.html';
    }else if(name=='PrivacyPolicy'){
        folderPath = folderPath + '/chox_privacy_policy.html';
    }else if(name=='Copyright'){
        folderPath = folderPath + '/chox_copyright.html';
    }

    newwindow=window.open(folderPath, 'CHOX');
    if (window.focus) {
        newwindow.focus();
    }
}

function onOpenAbout(){

    var msg = "<span class='aboutProductName'>Product Name: CHOX</span><br/><br/>";

    msg = msg + "<span class='acountCopyright'>Copyright Message: &copy;2012 Sherwood Compliance Services Ltd</span><br/><br/>";
    msg = msg + "<span class='acountVersionNumber'>V5.15 - 20120728</span><br/><br/>";
    msg = msg + "<input type='button' value='Close' onclick='javascript:$.unblockUI();'>";
    
    $.blockUI({
        message: $(msg),
        css: {
            backgroundColor: '#FFFFFF',
            height:'auto',
            padding:'10px'
        }
    });

    setTimeout($.unblockUI, 5000);
}

function onShowBrowserWarning(){
    
    Ext.MessageBox.show({
        title:    'CHOX Message',
        msg:      '<span>You may experience slow response times with your current browser version.</span><br/><br/><span>Recommended browsers are: Google Chrome, Firefox and IE v7+</span><br/><br/><input id="approval" type="checkbox" /> Tick this box if you do not wish this pop-up to appear again<br/><br/>',
        buttons:  Ext.MessageBox.OK,
        fn: function(btn) {
            if( btn == 'ok') {
                if (document.getElementById("approval").checked){
                    doNotShowBrowserWarning();
                } 
            }
        }
    });
}

function mopen(id)
{
    mcancelclosetime();
    if(ddmenuitem) ddmenuitem.style.visibility = 'hidden';
    ddmenuitem = document.getElementById(id);
    ddmenuitem.style.display = 'block';
    ddmenuitem.style.visibility = 'visible';

}

function mclose()
{
    if(ddmenuitem){
        ddmenuitem.style.visibility = 'hidden';
        ddmenuitem.style.display = 'none';
    }
}

function mclosetime()
{
    closetimer = window.setTimeout(mclose, timeout);
}

function mcancelclosetime()
{
    if(closetimer)
    {
        window.clearTimeout(closetimer);
        closetimer = null;
    }
}

function random_number() {
    var min = 10000000;
    var max = 99999999;
    return (Math.round((max-min) * Math.random() + min));
}

function uniqeToken(){
    return "&rdt="+random_number();
}

function propmtMsg(title, msg){

    Ext.MessageBox.show({
        title: title,
        msg: msg,
        width : 400,
        buttons: Ext.MessageBox.OK
    });
}

function propmtErrorMsg(msg){

    Ext.Msg.show({
        title: 'Error',
        msg:Ext.util.Format.ellipsis(msg, 2000),
        icon:Ext.Msg.ERROR,
        buttons:Ext.Msg.OK,
        width : 400
    });
}

function formErrorMessage(errors)
{
    var errorMsg = "<ul>";
    jQuery.each(errors, function() {
        errorMsg += "<li>";
        errorMsg +=this;
        errorMsg +="</li>";
    });

    errorMsg +="</ul>";

    return errorMsg;
}

function propmtErrors(errors){

    var errorMsg = formErrorMessage(errors);
    propmtErrorMsg(errorMsg);


    Ext.Msg.show({
        title: 'Error',
        msg:Ext.util.Format.ellipsis(msg, 2000),
        icon:Ext.Msg.ERROR,
        buttons:Ext.Msg.OK,
        width : 400
    });
}

function getDate(sdate){

    var date;

    if(sdate!=null && sdate!=""){

        var sDay = sdate.substring(0,2);
        var sMonth = sdate.substring(3,5);
        var sYear = sdate.substring(6,10);

        var sDate = sMonth+"/"+sDay+"/"+sYear;
        date = new Date(sDate);
    }

    return date;
}



function isTrue(a){
    if(a=='true' || a=='True' || a==1){
        return 1;
    }else{
        return 0;
    }
}

function triggerCss(targer, isError){
    $(targer).html("");
    $(targer).removeClass("chox-form-submit-result");
    $(targer).removeClass("action-error-msg");

    if(isError){
        $(targer).addClass("action-error-msg");
    }else{
        $(targer).addClass("chox-form-submit-result");
    }
}

       
      
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//                               decimal places restriction function                                                        /////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


function extractNumber(obj, decimalPlaces, allowNegative)
{
    var temp = obj.value;

    // avoid changing things if already formatted correctly
    var reg0Str = '[0-9]*';
    if (decimalPlaces > 0) {
        reg0Str += '\\.?[0-9]{0,' + decimalPlaces + '}';
    } else if (decimalPlaces < 0) {
        reg0Str += '\\.?[0-9]*';
    }
    reg0Str = allowNegative ? '^-?' + reg0Str : '^' + reg0Str;
    reg0Str = reg0Str + '$';
    var reg0 = new RegExp(reg0Str);
    if (reg0.test(temp)) return true;

    // first replace all non numbers
    var reg1Str = '[^0-9' + (decimalPlaces != 0 ? '.' : '') + (allowNegative ? '-' : '') + ']';
    var reg1 = new RegExp(reg1Str, 'g');
    temp = temp.replace(reg1, '');

    if (allowNegative) {
        // replace extra negative
        var hasNegative = temp.length > 0 && temp.charAt(0) == '-';
        var reg2 = /-/g;
        temp = temp.replace(reg2, '');
        if (hasNegative) temp = '-' + temp;
    }

    if (decimalPlaces != 0) {
        var reg3 = /\./g;
        var reg3Array = reg3.exec(temp);
        if (reg3Array != null) {
            // keep only first occurrence of .
            //  and the number of places specified by decimalPlaces or the entire string if decimalPlaces < 0
            var reg3Right = temp.substring(reg3Array.index + reg3Array[0].length);
            reg3Right = reg3Right.replace(reg3, '');
            reg3Right = decimalPlaces > 0 ? reg3Right.substring(0, decimalPlaces) : reg3Right;
            temp = temp.substring(0,reg3Array.index) + '.' + reg3Right;
        }
    }

    obj.value = temp;
}
    

function getRandomNumber() {
    var min = 10000000;
    var max = 99999999;
    return (Math.round((max-min) * Math.random() + min));
}

function doSectionLoad(location, action, pamareter){
    $(location).load(action+"?"+pamareter+"&urdn="+getRandomNumber());
}

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
                window.location = '/j_spring_security_logout';
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
            renderTo:target
        });

        return dateField;
    }

    function ajaxForm(form, successCallBack, responseType){

        function onAfterSubmit(responseText, statusText){
            onSubmitCompleted(responseText, statusText,form,responseType);
            if(successCallBack){
                successCallBack(responseText, statusText,form,responseType);
            }
        }

        form.submit(function(){

            if(form.valid()){
                var options = {
                    beforeSubmit: onBeforeSubmit,  // pre-submit callback
                    success: onAfterSubmit,  // post-submit callback
                    timeout: 3000,
                    error: onSubmitError
                };
                $(this).ajaxSubmit(options);
            }
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

    overlayCSS:  {
        backgroundColor:'#6c8cbe',
        opacity:        '0.5'
    },

    baseZ: 1000,
    centerX: true,
    centerY: true,
    allowBodyStretch: true,
    constrainTabKey: true,
    fadeOut:  0,
    applyPlatformOpacityRules: true
};

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
  var SHOW_AJAX_GENERAL_ERROR_MSG = false;
  var REDIRECT_ON_SESSION_TIMEOUT_URL = 'login.action';
  var REDIRECT_ON_ACCESS_DENIED = '/j_spring_security_logout';
  var AJAX_GENERAL_ERROR_MSG = 'We encountered a problem processing this request, please try again.';
  var AJAX_SESSION_TIMEOUT_ERROR_MSG = 'Your session has timed out, please login again.';
  var AJAX_DENIED_ACCESS_ERROR_MSG = 'You have been denied access. You will now be logged out - please login again.';
  var HTTP_SESSION_TIMEOUT_STATUS = 418;
  var HTTP_ACCESS_DENIED_STATUS = 401;
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
              window.location = REDIRECT_ON_ACCESS_DENIED; 
          }
      });
  }


  function loadHtml(url,param,success,error) {
      // Add nonce value
      //        console.log('loadHtml: NonceId value is: ' + $('#nonceId').val());
      //        console.log('loadHtml: param is: ' + param);
      if (typeof(param) == typeof('')) {
          // $(form).serialize() return a string
          //            console.log('Adding nonce to existing param string');
          param += 'nonce='+$('#nonceId').val();
      } else {
          //            console.log('Adding nonce to existing params.');
          param['nonce'] = $('#nonceId').val();
      }
      //        console.log('loadHtml: Nonce added to parameters: ' + param);
      $.post(url,param,function(data,textStatus){
          // Hack to handle access denied returned in the ajax response
          if (typeof data.indexOf == 'function'  && data.indexOf('You have been denied access') !=-1) {
              //                console.log("Access Denied detected");
              Ext.MessageBox.show({
                  title: 'Error',
                  msg: AJAX_DENIED_ACCESS_ERROR_MSG,
                  width:300,
                  buttons: Ext.MessageBox.OK,
                  icon : Ext.MessageBox.ERROR,
                  fn: function redirectToAccessDeniedPage(){
                      window.location = REDIRECT_ON_ACCESS_DENIED; 
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
                  error(data);
              }
          }
      },'html');
  }

  function loadJson(url,param,success,error){
      // Add nonce value
      //        console.log('loadJson: NonceId value is: ' + $('#nonceId').val());
      if (typeof(param) == typeof('')) {
          // $(form).serialize() return a string
          //            console.log('Adding nonce to existing param string');
          param += 'nonce='+$('#nonceId').val();
      } else {
          //            console.log('Adding nonce to existing params.');
          param['nonce'] = $('#nonceId').val();
      }
      $.post(url,param,function(data,textStatus){
          if (typeof data.indexOf == 'function'  && data.indexOf('You have been denied access') !=-1) {
              Ext.MessageBox.show({
                  title: 'Error',
                  msg: AJAX_DENIED_ACCESS_ERROR_MSG,
                  width:300,
                  buttons: Ext.MessageBox.OK,
                  icon : Ext.MessageBox.ERROR,
                  fn: function redirectToAccessDeniedPage(){
                      window.location = REDIRECT_ON_ACCESS_DENIED; 
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
//console.log("*******handleAjaxError: conn is:" + conn);
//var output = '';
//for (property in conn) {
//output += property + ': ' + conn[property]+'; ';
//}
//console.log("handleAjaxError: conn properties are - " + output);

//console.log("*******handleAjaxError: response is:" + response);
//console.log("*******handleAjaxError: response.status is:" + response.status);
//console.log("*******handleAjaxError: response.statusText is:" + response.statusText);
//output = '';
//for (property in response) {
//output += property + ': ' + response[property]+'; ';
//}
//console.log("handleAjaxError: response properties are - " + output);

//console.log("*******handleAjaxError: options are:" + options);
//output = '';
//for (property in options) {
//output += property + ': ' + options[property]+'; ';
//}
//console.log("handleAjaxError: options properties are - " + output);

//console.log("*******handleAjaxError: thrownError is:" + thrownError);
//output = '';
//for (property in thrownError) {
//output += property + ': ' + thrownError[property]+'; ';
//}
//console.log("handleAjaxError: thrownError properties are - " + output);



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
      else{
          lastResponse = response.status;
          //          console.log("handleAjaxError called with response status: " + response.status);
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

//------------------------------HAHA.js-------------------------------
Ext.ux.JSLoader = function(options) {

  Ext.ux.JSLoader.scripts[++Ext.ux.JSLoader.index] = {
    url: options.url,
    success: true,
    options: options,
    onLoad: options.onLoad || Ext.emptyFn,
    onError: options.onError || Ext.ux.JSLoader.stdError
  };

  Ext.Ajax.request({
    url: options.url,
    scriptIndex: Ext.ux.JSLoader.index,
    success: function(response, options) {
      var script = 'Ext.ux.JSLoader.scripts[' + options.scriptIndex + ']';
      window.setTimeout('try { ' + response.responseText + ' } catch(e) { '+script+'.success = false; '+script+'.onError('+script+'.options, e); }; if ('+script+'.success) '+script+'.onLoad('+script+'.options);', 0);
    },
    failure: function(response, options) {
      var script = Ext.ux.JSLoader.scripts[options.scriptIndex];
      script.success = false;
      script.onError(script.options, response.status);
    }
  });

}

Ext.ux.JSLoader.index = 0;
Ext.ux.JSLoader.scripts = [];


var reportGenerationStatusIntervelId;
function generateReport(queryString)
{
       
    if ( find_MSIE_version() > 0 && find_MSIE_version() < 9  ){
        
        Ext.MessageBox.show({
            title        : 'Generating Report...', 
            msg          : "Please wait...",
            width        : 300,
            closable     : false
        });
        
        window.location = contextPath+'/prv/p/downloadExcelReport.action?reportName='+ reportName + "&" +"directDownload="+true + "&" + queryString;
        directReportGenerationStatusIntervelId = setInterval(loadLiveDirectReportGenerationStatus, 1500);
            
    }else{
        
        Ext.Ajax.request({
            url:contextPath+'/prv/p/generateReportFile.action?reportName='+ reportName + "&" + queryString,
            callback : function(options,success,response  ){
                            
            }
        });
        if(reportName=='ClaimFileReport-Excel'){
            Ext.MessageBox.show({
                title        : 'Generating Report...', 
                buttons      :  Ext.Msg.CANCEL,
                msg          : "Please wait...",
                width        : 300,
                //                            wait         : true,                                                 
                closable     : false,
                fn           : cancelReportGeneration
            });
        } else{
            Ext.MessageBox.show({
                title        : 'Generating Report', 
                buttons      :  Ext.Msg.CANCEL,
                msg          : "Please be patient...large reports may take some time to generate.",
                width        : 300,
                //                            wait         : true,                                                 
                closable     : false,
                fn           : cancelReportGeneration
            });
        }      
    
                
        reportGenerationStatusIntervelId = setInterval(loadLiveReportGenerationStatus, 1500);
    }
}
    
function cancelReportGeneration(btn){
    if (btn == 'cancel'){
        Ext.MessageBox.hide();
        reportGenerationStatusIntervelId=window.clearInterval(reportGenerationStatusIntervelId);
        Ext.Ajax.request({
            url:contextPath+'/prv/p/cancelReportGenerationExport.action',
            callback : function(options,success,response  ){
                if(response.responseText){
                    var resp = Ext.util.JSON.decode(response.responseText);
                    if(resp.exportCancelled){
                        Ext.MessageBox.show({
                            title: '',
                            msg: 'Report generation cancelled.',
                            width:300,
                            buttons: Ext.MessageBox.OK
                        });
                    }else{
                        Ext.MessageBox.show({
                            title: 'Error',
                            msg: 'Export to Excel cancel failed. Please contact Chox Support.',
                            width:300,
                            buttons: Ext.MessageBox.OK,
                            icon : Ext.MessageBox.ERROR
                        });
                    }
                           
                }
            }
        });
                    
    }
}
            
var loadLiveReportGenerationStatus = function updateExportedClaim(){
                
    Ext.Ajax.request({
        url:contextPath+'/prv/p/getReportGenerationStatus.action',
        callback : function(options,success,response  ){
            if(response.responseText){
                var resp = Ext.util.JSON.decode(response.responseText);
                if(resp.isExportProcessFinished){
                    window.location = contextPath+"/prv/p/downloadExcelReport.action?";
                    Ext.MessageBox.hide();
                    reportGenerationStatusIntervelId=window.clearInterval(reportGenerationStatusIntervelId);
                               
                }else if(resp.exceptionThrown){
                    Ext.MessageBox.hide();
                    reportGenerationStatusIntervelId=window.clearInterval(reportGenerationStatusIntervelId);
                    Ext.MessageBox.show({
                        title: 'Error',
                        msg: 'Unexpected error occured. Please contact Chox Support.',
                        width:300,
                        buttons: Ext.MessageBox.OK,
                        icon : Ext.MessageBox.ERROR
                    });
                }
                                                       
            }
        }
    });
                
}


function generateReport1(queryString,reportName)
{
       
    Ext.Ajax.request({
        url:contextPath+'/prv/p/generateReportFile.action?reportName='+ reportName + "&" + queryString,
        callback : function(options,success,response  ){
                            
        }
    });
                
    Ext.MessageBox.show({
        title        : 'Generating Report...', 
        buttons      :  Ext.Msg.CANCEL,
        msg          : "Please be patient...large reports may take some time to process.",
        width        : 300,
        //                            wait         : true,                                                 
        closable     : false,
        fn           : cancelReportGeneration
    });
                
    reportGenerationStatusIntervelId = setInterval(loadLiveReportGenerationStatus, 1500);

}

function find_MSIE_version()
{
    var ua = window.navigator.userAgent
    var msie = ua.indexOf ( "MSIE " )

    if ( msie > 0 )      // If Internet Explorer, return version number
        return parseInt (ua.substring (msie+5, ua.indexOf (".", msie )))
    else                 // If another browser, return 0
        return 0

}

function loadLiveDirectReportGenerationStatus(){
    Ext.Ajax.request({
        url:contextPath+'/prv/p/getReportGenerationStatus.action',
        callback : function(options,success,response  ){
            if(response.responseText){
                
                var resp = Ext.util.JSON.decode(response.responseText);
                if(resp.isExportProcessFinished){
                    Ext.MessageBox.hide();
                    directReportGenerationStatusIntervelId=window.clearInterval(directReportGenerationStatusIntervelId);
                }else if(resp.exceptionThrown){
                    Ext.MessageBox.hide();
                    directReportGenerationStatusIntervelId=window.clearInterval(directReportGenerationStatusIntervelId);
                    Ext.MessageBox.show({
                        title: 'Error',
                        msg: 'Unexpected error occured. Please contact Chox Support.',
                        width:300,
                        buttons: Ext.MessageBox.OK,
                        icon : Ext.MessageBox.ERROR
                    });
                }
            }
        }
    });
}

function doExportExcel(){
    if(!ds.getCount()){
        Ext.Msg.alert('','No record found, Please try again');
    }else{
        if( ds.getTotalCount()<=10000){
            if(isOffPeak() || ds.getTotalCount()<=3000){
                if ( find_MSIE_version() > 0 && find_MSIE_version() < 9  ){
                    Ext.MessageBox.show({
                        title        : 'Exporting Claims...', 
                        msg          : "Please wait...",
                        width        : 300,
                        closable     : false
                    });
                    window.location = contextPath+"/prv/doExportExcel.action?directDownload="+true;
                    directExportToExcelStatusIntervelId = setInterval(loadDirectExportToExcelStatus, 1500);
                }else{
                    Ext.Ajax.request({
                        url:contextPath+'/prv/p/generateExportFile.action',
                        callback : function(options,success,response  ){
                        }
                    });
                    Ext.MessageBox.show({
                        title        : 'Generating Report...', 
                        buttons      :  Ext.Msg.CANCEL,
                        msg          : "0 claims exported",
                        progressText : 'Export process started...',
                        width        : 300,
                        progress     : true,                                                 
                        closable     : false,
                        fn           : cancelExportToExcel
                    });
                    exportToExcelIntervelId = setInterval(loadLiveExportToExcelClaimCount, 1500);
                }
            } else {
                Ext.Msg.alert('','The Export To Excel feature is restricted to exporting a maximum of 3,000 claims between 9 a.m - 5.30 p.m, please refine your search.');
            }
        }
        else if(isOffPeak()){
            Ext.Msg.alert('','The Export To Excel feature is restricted to exporting a maximum of 10,000 claims, please refine your search.');
        }else{
            Ext.Msg.alert('','The Export To Excel feature is restricted to exporting a maximum of 3,000 claims between 9 a.m - 5.30 p.m, please refine your search.');
        }
    }
}
            
function cancelExportToExcel(btn){
    if (btn == 'cancel'){
        Ext.MessageBox.hide();
        exportToExcelIntervelId=window.clearInterval(exportToExcelIntervelId);
        Ext.Ajax.request({
            url:contextPath+'/prv/p/cancelExport.action',
            callback : function(options,success,response  ){
                if(response.responseText){
                    var resp = Ext.util.JSON.decode(response.responseText);
                    if(resp.exportCancelled){
                        Ext.MessageBox.show({
                            title: '',
                            msg: 'Export To Excel cancelled.',
                            width:300,
                            buttons: Ext.MessageBox.OK
                        });
                    }else{
                        Ext.MessageBox.show({
                            title: 'Error',
                            msg: 'Export to Excel cancel failed. Please contact Chox Support.',
                            width:300,
                            buttons: Ext.MessageBox.OK,
                            icon : Ext.MessageBox.ERROR
                        });
                    }
                           
                }
            }
        });
                    
    }
}
        
var loadLiveExportToExcelClaimCount = function updateExportedClaim(){
                
    Ext.Ajax.request({
        url:contextPath+'/prv/p/updateExportClaimsCount.action',
        callback : function(options,success,response  ){
            if(response.responseText){
                var resp = Ext.util.JSON.decode(response.responseText);
                if(resp.isExportProcessFinished){
                    window.location= "doExportExcel.action?";
                    Ext.MessageBox.hide();
                    exportToExcelIntervelId=window.clearInterval(exportToExcelIntervelId);
                }else if(resp.exceptionThrown){
                    Ext.MessageBox.hide();
                    exportToExcelIntervelId=window.clearInterval(exportToExcelIntervelId);
                    Ext.MessageBox.show({
                        title: 'Error',
                        msg: 'Unexpected error occured. Please contact Chox Support.',
                        width:300,
                        buttons: Ext.MessageBox.OK,
                        icon : Ext.MessageBox.ERROR
                    });
                }
                else if(ds.getTotalCount()>=resp.exportedClaimCount){
                                
                    var i = resp.exportedClaimCount/ds.getTotalCount();
                    if(resp.writingToFile){
                        Ext.MessageBox.updateProgress(i, (i*100).toFixed(0) + '% complete', 'Please wait - report is now being exported to an Excel file...');
                    }else{
                        Ext.MessageBox.updateProgress(i, (i*100).toFixed(0) + '% complete', resp.exportedClaimCount+' claims exported');
                    }
                }
            }
        }
    });
}
function doNotShowBrowserWarning(){
       
    Ext.Ajax.request({
        url:contextPath+'/prv/p/userBrowserWarning.action',
        callback : function(options,success,response  ){
            if(response.responseText){
                var resp = Ext.util.JSON.decode(response.responseText);
                if(resp && resp.isValid){
                    if(resp.resultType && resp.resultType == 'Message')
                    {
//                        Ext.MessageBox.show({
//                            title: '',
//                            msg: resp.result,
//                            width:300,
//                            buttons: Ext.MessageBox.OK
//                        });
                    }else if(!resp.result){

                        Ext.MessageBox.show({
                            title: 'failure',
                            msg: 'Unexpected Error occured. Please report to chox admin.',
                            width:300,
                            buttons: Ext.MessageBox.OK,
                            icon : Ext.MessageBox.ERROR
                        });
                    }
                }
            }
        },
        params: {
            showSplash : false
        }
    });
}
            
function isOffPeak(){
            
    var currentTime = new Date();
    var hours = currentTime.getHours();
    var minutes = currentTime.getMinutes();

    if (hours < 9 || hours > 17 || (hours == 17 && minutes >= 30)) {
            return true;
    } else{ 
            return false;
    }
        
    
}

function loadDirectExportToExcelStatus(){
    Ext.Ajax.request({
        url:contextPath+'/prv/p/updateExportClaimsCount.action',
        callback : function(options,success,response  ){
            if(response.responseText){
                
                var resp = Ext.util.JSON.decode(response.responseText);
                if(resp.isExportProcessFinished){
                    Ext.MessageBox.hide();
                    directExportToExcelStatusIntervelId=window.clearInterval(directExportToExcelStatusIntervelId);
                }else if(resp.exceptionThrown){
                    Ext.MessageBox.hide();
                    directExportToExcelStatusIntervelId=window.clearInterval(directExportToExcelStatusIntervelId);
                    Ext.MessageBox.show({
                        title: 'Error',
                        msg: 'Unexpected error occured. Please contact Chox Support.',
                        width:300,
                        buttons: Ext.MessageBox.OK,
                        icon : Ext.MessageBox.ERROR
                    });
                }
            }
        }
    });
}
