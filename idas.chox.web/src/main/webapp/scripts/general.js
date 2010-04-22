
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

function openHelpFile(url, helpFileRoleType){

    var folderPath = url;
    var fileName = "";


    switch(helpFileRoleType)
    {
        case 1: // NORMAL INSURER ROLE
            fileName = '/download/iDAS_CHOX_IUG_3.1.pdf';
            break;
        case 2: // INSURER MANAGER ROLE
            fileName = '/download/iDAS_CHOX_IUG_AMD_3.1.pdf';
            break;
        case 3: // NORMAL CREDIT HIRE ROLE
            fileName = '/download/iDAS_CHOX_CHO_UG_3.1.pdf';
            break;
        case 4: // CREDIT HIRE MANAGER ROLE
            fileName = '/download/iDAS_CHOX_CHO_UG_ADM_3.1.pdf';
            break;
    }

    if(fileName.length>0){
        openFile(folderPath+fileName);
    }
}

function openSupportFile(url)
{
    var folderPath = url;
    folderPath = folderPath + '/chox_support.html';
    openFile(folderPath);

}

function openFile(folderPath){

    newwindow=window.open(folderPath, 'IDASCHOX');
    if (window.focus) {
        newwindow.focus()
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

    newwindow=window.open(folderPath, 'IDASCHOX');
    if (window.focus) {
        newwindow.focus()
    }
}

function onOpenAbout(){

    var msg = "<span class='aboutProductName'>Product Name: iDAS CHOX</span><br/><br/>";

    msg = msg + "<span class='acountCopyright'>Copyright Message: &copy;2010 Sherwood Compliance Services Ltd</span><br/><br/>";
    msg = msg + "<span class='acountVersionNumber'>V3.0 - 20100422</span><br/><br/>";
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

function doExportExcel(){
    window.location= "doExportExcel.action";
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

