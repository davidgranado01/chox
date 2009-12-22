var menu = function(){

    var timeout	= 500;
    var closetimer	= 0;
    var ddmenuitem	= 0;
    var newwindow;

    return {

        openHelpFile : function(url, helpFileRoleType){

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
        },

        openSupportFile : function(url)
        {
            var folderPath = url;
            folderPath = folderPath + '/jsp/chox_support.jsp';
            openFile(folderPath);

        },

        openFile : function(folderPath){

            newwindow=window.open(folderPath, 'IDASCHOX');
            if (window.focus) {
                newwindow.focus()
            }
    
        },
    
        openChoxPolicyPage : function(url, name){
            var folderPath = url;
            if(name=='TermsOfService'){
                folderPath = folderPath + '/jsp/terms_of_service.jsp';
            }else if(name=='PrivacyPolicy'){
                folderPath = folderPath + '/jsp/chox_privacy_policy.jsp';
            }else if(name=='Copyright'){
                folderPath = folderPath + '/jsp/chox_copyright.jsp';
            }
        
            newwindow=window.open(folderPath, 'IDASCHOX');
            if (window.focus) {
                newwindow.focus()
            }
        },

        onOpenAbout : function(){
        
            var msg = "<span class='aboutProductName'>Product Name: iDAS CHOX</span><br/><br/>";
            msg = msg + "<span class='acountCopyright'>Copyright Message: ©2009 Sherwood Compliance Services Ltd</span><br/><br/>";
            msg = msg + "<span class='acountVersionNumber'>V2.8.2.34 - 20091201</span><br/><br/>";
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
        },

        mopen : function(id)
        {
            mcancelclosetime();
            if(ddmenuitem) ddmenuitem.style.visibility = 'hidden';
            ddmenuitem = document.getElementById(id);
            ddmenuitem.style.display = 'block';
            ddmenuitem.style.visibility = 'visible';

        },

        mclose : function()
        {
            if(ddmenuitem){
                ddmenuitem.style.visibility = 'hidden';
                ddmenuitem.style.display = 'none';
            }
        },

        mclosetime : function()
        {
            closetimer = window.setTimeout(mclose, timeout);
        },

        mcancelclosetime : function()
        {
            if(closetimer)
            {
                window.clearTimeout(closetimer);
                closetimer = null;
            }
        }
    }
}