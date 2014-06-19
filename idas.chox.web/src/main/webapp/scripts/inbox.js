if (!Ext.isDefined(Ext.webKitVersion)) {
    Ext.webKitVersion = Ext.isWebKit ? parseFloat(/AppleWebKit\/([\d.]+)/.exec(navigator.userAgent)[1], 10) : NaN;
}
/*
 * Box-sizing was changed beginning with Chrome v19.  For background information, see:
 * http://code.google.com/p/chromium/issues/detail?id=124816
 * https://bugs.webkit.org/show_bug.cgi?id=78412
 * https://bugs.webkit.org/show_bug.cgi?id=87536
 * http://www.sencha.com/forum/showthread.php?198124-Grids-are-rendered-differently-in-upcoming-versions-of-Google-Chrome&p=824367
 *
 * */
if (Ext.isWebKit && Ext.webKitVersion >= 535.2) { // probably not the exact version, but the issues started appearing in chromium 19
    Ext.override(Ext.grid.ColumnModel, {
        getTotalWidth: function (includeHidden) {
            if (!this.totalWidth) {
                var boxsizeadj = 2;
                this.totalWidth = 0;
                for (var i = 0, len = this.config.length; i < len; i++) {
                    if (includeHidden || !this.isHidden(i)) {
                        this.totalWidth += (this.getColumnWidth(i) + boxsizeadj);
                    }
                }
            }
            return this.totalWidth;
        }
    });


    Ext.onReady(function() {
        Ext.get(document.body).addClass('ext-chrome-fixes');
        Ext.util.CSS.createStyleSheet('@media screen and (-webkit-min-device-pixel-ratio:0) {.x-grid3-cell{box-sizing: border-box !important;}}', 'chrome-fixes-box-sizing');
    });
}

function doExportExcel(){
    if(!claimStore.getCount()){
        Ext.Msg.alert('','No record found, Please try again');
    }else{
        if( claimStore.getTotalCount()<=6000){
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
                choxExtAjaxRequest({
                    url: '/prv/p/generateExportFile.action',
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
        }
        else{
            Ext.Msg.alert('','The Export To Excel feature is restricted to exporting a maximum of 6,000 claims, please refine your search.');
        }
    }
}
            
function cancelExportToExcel(btn){
    if (btn == 'cancel'){
        Ext.MessageBox.hide();
        exportToExcelIntervelId=window.clearInterval(exportToExcelIntervelId);
        choxExtAjaxRequest({
            url: '/prv/p/cancelExport.action',
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
                
    choxExtAjaxRequest({
        url: '/prv/p/updateExportClaimsCount.action',
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
                        msg: 'Unexpected error occurred. Please contact Chox Support.',
                        width:300,
                        buttons: Ext.MessageBox.OK,
                        icon : Ext.MessageBox.ERROR
                    });
                }else if(resp.tooManyRows){
                    Ext.MessageBox.hide();
                    exportToExcelIntervelId=window.clearInterval(exportToExcelIntervelId);
                    Ext.MessageBox.show({
                        title: 'Error',
                        msg: 'This data export will exceed the maximum number of allowable rows in Excel (65,536), please reduce the number of exported claims.',
                        width:300,
                        buttons: Ext.MessageBox.OK,
                        icon : Ext.MessageBox.ERROR
                    });
                }else if(claimStore.getTotalCount()>=resp.exportedClaimCount){
                                
                    var i = resp.exportedClaimCount/claimStore.getTotalCount();
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
       
    choxExtAjaxRequest({
        url: '/prv/p/userBrowserWarning.action',
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
                            msg: 'Unexpected Error occurred. Please report to chox admin.',
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
            
function loadDirectExportToExcelStatus(){
    choxExtAjaxRequest({
        url: '/prv/p/updateExportClaimsCount.action',
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
                        msg: 'Unexpected error occurred. Please contact Chox Support.',
                        width:300,
                        buttons: Ext.MessageBox.OK,
                        icon : Ext.MessageBox.ERROR
                    });
                }else if(resp.tooManyRows){
                    Ext.MessageBox.hide();
                    directExportToExcelStatusIntervelId=window.clearInterval(directExportToExcelStatusIntervelId);
                    Ext.MessageBox.show({
                        title: 'Error',
                        msg: 'This data export will exceed the maximum number of allowable rows in Excel (65,536), please reduce the number of exported claims.',
                        width:300,
                        buttons: Ext.MessageBox.OK,
                        icon : Ext.MessageBox.ERROR
                    });
                }
            }
        }
    });
}

function doTaskExportExcel(){
    if(!tasksDataStore.getCount()){
        Ext.Msg.alert('','No task found, Please try again');
    }else{
        if( tasksDataStore.getTotalCount()<=65536){
            if ( find_MSIE_version() > 0 && find_MSIE_version() < 9  ){
                Ext.MessageBox.show({
                    title        : 'Exporting Tasks...', 
                    msg          : "Please wait...",
                    width        : 300,
                    closable     : false
                });
                window.location = contextPath+"/prv/doTaskExportExcel.action?directDownload="+ true + "&" +"hideCompleted="+hideCompleted + "&" +"showAssignedTasksOnly="+showAssignedTasksOnly;
                directTaskExportToExcelStatusIntervelId = setInterval(loadDirectTaskExportToExcelStatus, 1500);
            }else{
                choxExtAjaxRequest({
                    url: "/prv/p/generateTaskExportFile.action",
                    params : {'hideCompleted' : hideCompleted, 'showAssignedTasksOnly' : showAssignedTasksOnly},
                    callback : function(options,success,response  ){
                    }
                });
                Ext.MessageBox.show({
                    title        : 'Generating Report...', 
                    buttons      :  Ext.Msg.CANCEL,
                    msg          : "0 tasks exported",
                    progressText : 'Export process started...',
                    width        : 300,
                    progress     : true,                                                 
                    closable     : false,
                    fn           : cancelTaskExportToExcel
                });
                taskExportToExcelIntervelId = setInterval(loadLiveTaskExportToExcelClaimCount, 1500);
            }
        }
        else{
            Ext.Msg.alert('','The Export To Excel feature is restricted to exporting a maximum of 65,536 tasks, please refine your search.');
        }
    }
}
            
function cancelTaskExportToExcel(btn){
    if (btn == 'cancel'){
        Ext.MessageBox.hide();
        taskExportToExcelIntervelId=window.clearInterval(taskExportToExcelIntervelId);
        choxExtAjaxRequest({
            url: '/prv/p/cancelTaskExport.action',
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

var loadLiveTaskExportToExcelClaimCount = function updateExportedTask(){

    choxExtAjaxRequest({
        url: '/prv/p/updateExportTasksCount.action',
        callback : function(options,success,response  ){
            if(response.responseText){
                var resp = Ext.util.JSON.decode(response.responseText);
                if(resp.isExportProcessFinished){
                    window.location= "doTaskExportExcel.action?";
                    Ext.MessageBox.hide();
                    taskExportToExcelIntervelId=window.clearInterval(taskExportToExcelIntervelId);
                }else if(resp.exceptionThrown){
                    Ext.MessageBox.hide();
                    taskExportToExcelIntervelId=window.clearInterval(taskExportToExcelIntervelId);
                    Ext.MessageBox.show({
                        title: 'Error',
                        msg: 'Unexpected error occurred. Please contact Chox Support.',
                        width:300,
                        buttons: Ext.MessageBox.OK,
                        icon : Ext.MessageBox.ERROR
                    });
                }else if(resp.tooManyRows){
                    Ext.MessageBox.hide();
                    taskExportToExcelIntervelId=window.clearInterval(taskExportToExcelIntervelId);
                    Ext.MessageBox.show({
                        title: 'Error',
                        msg: 'This data export will exceed the maximum number of allowable rows in Excel (65,536), please reduce the number of exported tasks.',
                        width:300,
                        buttons: Ext.MessageBox.OK,
                        icon : Ext.MessageBox.ERROR
                    });
                }else if(tasksDataStore.getTotalCount()>=resp.exportedTaskCount){

                    var i = resp.exportedTaskCount/tasksDataStore.getTotalCount();
                    if(resp.writingToFile){
                        Ext.MessageBox.updateProgress(i, (i*100).toFixed(0) + '% complete', 'Please wait - report is now being exported to an Excel file...');
                    }else{
                        Ext.MessageBox.updateProgress(i, (i*100).toFixed(0) + '% complete', resp.exportedTaskCount+' tasks exported');
                    }
                }
            }
        }
    });
}

function loadDirectTaskExportToExcelStatus(){
    choxExtAjaxRequest({
        url: '/prv/p/updateExportTasksCount.action',
        callback : function(options,success,response  ){
            if(response.responseText){

                var resp = Ext.util.JSON.decode(response.responseText);
                if(resp.isExportProcessFinished){
                    Ext.MessageBox.hide();
                    directTaskExportToExcelStatusIntervelId=window.clearInterval(directTaskExportToExcelStatusIntervelId);
                }else if(resp.exceptionThrown){
                    Ext.MessageBox.hide();
                    directTaskExportToExcelStatusIntervelId=window.clearInterval(directTaskExportToExcelStatusIntervelId);
                    Ext.MessageBox.show({
                        title: 'Error',
                        msg: 'Unexpected error occurred. Please contact Chox Support.',
                        width:300,
                        buttons: Ext.MessageBox.OK,
                        icon : Ext.MessageBox.ERROR
                    });
                }else if(resp.tooManyRows){
                    Ext.MessageBox.hide();
                    directTaskExportToExcelStatusIntervelId=window.clearInterval(directTaskExportToExcelStatusIntervelId);
                    Ext.MessageBox.show({
                        title: 'Error',
                        msg: 'This data export will exceed the maximum number of allowable rows in Excel (65,536), please reduce the number of exported tasks.',
                        width:300,
                        buttons: Ext.MessageBox.OK,
                        icon : Ext.MessageBox.ERROR
                    });
                }
            }
        }
    });
}