var reportGenerationStatusIntervelId;
function generateReport(queryString)
    {
       
            Ext.Ajax.request({
                    url:contextPath+'/prv/p/generateReportFile.action?reportName='+ reportName + "&" + queryString,
                    callback : function(options,success,response  ){
                            
                    }
                });
                
                Ext.MessageBox.show({
                            title        : 'Generating Report...', 
                            buttons      :  Ext.Msg.CANCEL,
                            msg          : "Please wait...",
                            width        : 300,
                            wait         : true,                                                 
                            closable     : false,
                            fn           : cancelReportGeneration
                        });
                
                reportGenerationStatusIntervelId = setInterval(loadLiveReportGenerationStatus, 1500);

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
                                        msg: 'Export operation cancelled.',
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
                               
                            }
                                                       
                        }
                    }
                });
                
            }


