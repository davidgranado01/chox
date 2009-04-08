  
    var newwindow;
    var strDateFormat = 'd/m/Y'; 
    
    function openFile(url, name)
    {
        var folderPath = url;        
        if(name=='ChoHelp'){
            folderPath = folderPath + '/download/iDAS_CHOX_CHO_UG_1.1-1.pdf';
        }else if(name=='InsHelp'){
            folderPath = folderPath + '/download/iDAS_CHOX_IUG_1.0-1.pdf';
        }else if(name=='Support'){
            folderPath = folderPath + '/jsp/chox_support.jsp';
        }
        
        newwindow=window.open(folderPath, 'IDASCHOX');
        if (window.focus) {newwindow.focus()}
    }
       
    function getTodayDate(){
        var now = new Date();
        return now.format(strDateFormat);
    }
    
    function openChoxPolicyPage(url, name){
        var folderPath = url;        
        if(name=='TermsOfService'){
            folderPath = folderPath + '/jsp/terms_of_service.jsp';
        }else if(name=='PrivacyPolicy'){
            folderPath = folderPath + '/jsp/chox_privacy_policy.jsp';
        }else if(name=='Copyright'){
            folderPath = folderPath + '/jsp/chox_copyright.jsp';
        }
        
        newwindow=window.open(folderPath, 'IDASCHOX');
        if (window.focus) {newwindow.focus()}
    }
    
    function onOpenAbout(){
        
        var msg = "<span class='aboutProductName'>Product Name: iDAS CHOX</span><br/><br/>";
        msg = msg + "<span class='acountCopyright'>Copyright Message: ©2009 Sherwood Compliance Services Ltd</span><br/><br/>";
        msg = msg + "<span class='acountVersionNumber'>V2.5.4 - 20090407</span><br/><br/>";
        msg = msg + "<input type='button' value='Close' onclick='javascript:$.unblockUI();'>";
        
        $.blockUI({message: $(msg), css: { backgroundColor: '#FFFFFF', height:'auto', padding:'10px'}});
        setTimeout($.unblockUI, 5000);
    }
    
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
        