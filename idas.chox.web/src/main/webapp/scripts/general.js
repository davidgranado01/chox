
var newwindow;
var strDateFormat = 'd/m/Y';

var timeout	= 500;
var closetimer	= 0;
var ddmenuitem	= 0;

function openHelpFile(url, helpFileRoleType, bespoke){
    var folderPath = url;
    var fileName = "";

    switch(helpFileRoleType) {
        case 1: // NORMAL INSURER ROLE
            if (bespoke) {
                fileName = '/download/CHOX_IUG_S.pdf';
            }
            else {
                fileName = '/download/CHOX_IUG.pdf';
            }
            break;
        case 2: // INSURER MANAGER ROLE
            if (bespoke) {
                fileName = '/download/CHOX_IUG_S_ADM.pdf';
            }
            else{
                fileName = '/download/CHOX_IUG_ADM.pdf';
            }
            break;
        case 3: // NORMAL CREDIT HIRE ROLE
            fileName = '/download/CHOX_CHO_UG.pdf';
            break;
        case 4: // CREDIT HIRE MANAGER ROLE
            fileName = '/download/CHOX_CHO_UG_ADM.pdf';
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

    newwindow=window.open('', 'CHOX');
    try {
        newwindow.document.location.href = folderPath;
    } catch(exc) {
        newwindow.close();
        newwindow = window.open(folderPath, 'CHOX');
    }

    newwindow.focus();
}


function getTodayDate(){
    var now = new Date();
    return now.format(strDateFormat);
}

function openChoxPolicyPage(url, name){
    var folderPath = url;
    if(name==='TermsOfService'){
        folderPath = folderPath + '/terms_of_service.html';
    }else if(name==='PrivacyPolicy'){
        folderPath = folderPath + '/chox_privacy_policy.html';
    }else if(name==='Copyright'){
        folderPath = folderPath + '/chox_copyright.html';
    }

    openFile(folderPath);
}

function onOpenAbout(){

    var msg = "<span class='aboutProductName'>Product Name: CHOX</span><br/><br/>";

    msg = msg + "<span class='acountCopyright'>Copyright Message: &copy;2017 Audatex (UK) Limited</span><br/><br/>";
    msg = msg + "<span class='acountVersionNumber'>" + generalChoxVersion + "</span><br/><br/>";
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
        msg:      '<span>You may experience slow response times with your current browser version.</span><br/><br/><span>Recommended browsers are: Google Chrome, Firefox and IE v9+</span><br/><br/><input id="approval" type="checkbox" /> Tick this box if you do not wish this pop-up to appear again<br/><br/>',
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
    if(ddmenuitem) {ddmenuitem.style.visibility = 'hidden';}
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

function triggerCss(target, isError) {
    $(target).html("");
    $(target).removeClass("chox-form-submit-result");
    $(target).removeClass("action-error-msg");

    if(isError){
        $(target).addClass("action-error-msg");
    }else{
        $(target).addClass("chox-form-submit-result");
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
    return true;
}
    

