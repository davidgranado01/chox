var reportGenerationStatusIntervelId, directReportGenerationStatusIntervelId;
var cancelled=false;
function generateReport(queryString){
       
       
    if ( find_MSIE_version() > 0 && find_MSIE_version() < 9 && !cancelled ){
        
        Ext.MessageBox.show({
            title        : 'Generating Report...', 
            msg          : "Please wait...",
            width        : 300,
            closable     : false
        });
        
        directReportGenerationStatusIntervelId = setTimeout(loadLiveDirectReportGenerationStatus, 1000);
//        window.location = contextPath+'/prv/p/downloadExcelReport.action?reportName='+ reportName + "&" +"directDownload="+true + "&" + Ext.urlEncode(queryString);
        window.location = contextPath+'/prv/p/downloadExcelReport.action?reportName='+ reportName + "&" +"directDownload="+true + "&"  + csrfParameterName + "=" + csrfTokenValue + "&" + Ext.urlEncode(queryString);
    }else if ( !cancelled ) {
        choxExtAjaxRequest({
            url: '/prv/p/generateReportFile.action',
            timeout : 3600000,
            params : Ext.apply({'reportName' : reportName}, queryString),
            callback : function(options,success,response  ){
            }
        });
        if(reportName==='ClaimFileReport-Excel'){
            Ext.MessageBox.show({
                title        : 'Generating Report...', 
                buttons      :  Ext.Msg.CANCEL,
                msg          : "Please wait...",
                width        : 300,
                //                            wait         : true,                                                 
                closable     : false,
                fn           : cancelReportGeneration
            });
        }else{
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
        reportGenerationStatusIntervelId = setTimeout(loadLiveReportGenerationStatus, 1500);
    }
}


function cancelReportGeneration(btn){
    if (btn === 'cancel'){
        cancelled = true;
        reportGenerationStatusIntervelId=window.clearTimeout(reportGenerationStatusIntervelId);
        Ext.MessageBox.hide();
        choxExtAjaxRequest({
            url: '/prv/p/cancelReportGenerationExport.action',
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
                            msg: 'Export to Excel cancel failed.',
                            width:300,
                            buttons: Ext.MessageBox.OK,
                            icon : Ext.MessageBox.ERROR
                        });
                    }
                    cancelled = false;
                }
            }
        });
                    
    }
}
            
var loadLiveReportGenerationStatus = function updateExportedClaim(){
                
    choxExtAjaxRequest({
        url: '/prv/p/getReportGenerationStatus.action',
        callback : function(options,success,response  ){
            if(response.responseText){
                var resp = Ext.util.JSON.decode(response.responseText);
                if(resp.isExportProcessFinished && !resp.exceptionThrown){
                    Ext.MessageBox.hide();
                    if (!cancelled && !resp.exportCancelled){
//                        var $form=$(document.createElement('form')).css({display:'none'}).attr("method","POST").attr("action",contextPath+"/prv/p/downloadExcelReport.action").attr("name", csrfParameterName).val(csrfTokenValue);
                        var $form=$(document.createElement('form')).css({display:'none'}).attr("method","POST").attr("action",contextPath+"/prv/p/downloadExcelReport.action?" + csrfParameterName + "=" + csrfTokenValue);
                        $("body").append($form);
                        $form.submit();
                    }
                }else if(resp.exceptionThrown){
                    Ext.MessageBox.hide();
                    Ext.MessageBox.show({
                        title: 'Error',
                        msg: 'Unexpected error occurred. Please contact Chox Support.',
                        width:300,
                        buttons: Ext.MessageBox.OK,
                        icon : Ext.MessageBox.ERROR
                    });
                }else if (!resp.exportCancelled && !cancelled){
                    reportGenerationStatusIntervelId = setTimeout(loadLiveReportGenerationStatus, 1000);
                }                                        
            }
        }
    });
                
};


function generateReport1(queryString,reportName)
{
       
    choxExtAjaxRequest({
        url: '/prv/p/generateReportFile.action',
        timeout : 3600000,
        params : Ext.apply({'reportName' : reportName}, queryString),
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
                
    reportGenerationStatusIntervelId = setTimeout(loadLiveReportGenerationStatus, 1000);

}

function find_MSIE_version(){
    var ua = window.navigator.userAgent;
    var msie = ua.indexOf ( "MSIE " );

    if ( msie > 0 )      // If Internet Explorer, return version number
        return parseInt (ua.substring (msie+5, ua.indexOf (".", msie )));
    else                 // If another browser, return 0
        return 0;

}

function loadLiveDirectReportGenerationStatus(){
    choxExtAjaxRequest({
        url: '/prv/p/getReportGenerationStatus.action',
        callback : function(options,success,response  ){
            if(response.responseText){
                
                var resp = Ext.util.JSON.decode(response.responseText);
                if(resp.isExportProcessFinished){
                    Ext.MessageBox.hide();
                }else if(resp.exceptionThrown){
                    Ext.MessageBox.hide();
                    Ext.MessageBox.show({
                        title: 'Error',
                        msg: 'Unexpected error occurred. Please contact Chox Support.',
                        width:300,
                        buttons: Ext.MessageBox.OK,
                        icon : Ext.MessageBox.ERROR
                    });
                }else{
                    directReportGenerationStatusIntervelId = setTimeout(loadLiveDirectReportGenerationStatus, 1000);
                }
            }
        }
    });
}