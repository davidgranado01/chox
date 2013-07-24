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
    if (btn === 'cancel'){
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
                
};


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

function find_MSIE_version(){
    var ua = window.navigator.userAgent;
    var msie = ua.indexOf ( "MSIE " );

    if ( msie > 0 )      // If Internet Explorer, return version number
        return parseInt (ua.substring (msie+5, ua.indexOf (".", msie )));
    else                 // If another browser, return 0
        return 0;

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