function doExportExcel(){
    if(!ds.getCount()){
        Ext.Msg.alert('','No record found, Please try again');
    }else{
        if( ds.getTotalCount()<=10000){
            if(checkTimeOfDay()=="offPeak" || ds.getTotalCount()<=3000){
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
        else if(checkTimeOfDay()=="offPeak"){
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
            
function checkTimeOfDay(){
            
    var currentTime = new Date();
    var hours = currentTime.getHours();
    var minutes = currentTime.getMinutes();
    if(hours<9 && hours>=17){
        if(hours==17 && minutes <=30){
            return "peak";
        }else{
            return "offPeak";
        }
    } else if(hours>=9 && hours <=17){
        if(hours==17 && minutes >30){
            return "offPeak";
        }else{
            return "peak";
        }
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