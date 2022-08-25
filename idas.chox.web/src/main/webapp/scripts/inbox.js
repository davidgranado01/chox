var directExportToExcelStatusIntervelId, exportToExcelIntervelId;
var directTaskExportToExcelStatusIntervelId, taskExportToExcelIntervelId;
var cancelled=false;

// Change default sort function to be case insensitive
Ext.override(Ext.data.Store, {
// override
createSortFunction : function(field, direction) {
    direction = direction || "ASC";
    var directionModifier = direction.toUpperCase() === "DESC" ? -1 : 1;
    var sortType = this.fields.get(field).sortType;

    //create a comparison function. Takes 2 records, returns 1 if record 1 is greater,
    //-1 if record 2 is greater or 0 if they are equal
    return function(r1, r2) {
        var v1 = sortType(r1.data[field]),
            v2 = sortType(r2.data[field]);

        // To perform case insensitive sort
        if (v1.toLowerCase) {
            v1 = v1.toLowerCase();
            v2 = v2.toLowerCase();
        }

        return directionModifier * (v1 > v2 ? 1 : (v1 < v2 ? -1 : 0));
    };
} 
});

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
                directExportToExcelStatusIntervelId = setTimeout(loadDirectExportToExcelStatus, 1000);
            }else{
                cancelled = false;
                choxExtAjaxRequest({
                    url: '/prv/p/generateExportFile.action',
                    timeout : 3600000,
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
                exportToExcelIntervelId = setTimeout(loadLiveExportToExcelClaimCount(1000), 1000);
            }
        } else {
            Ext.Msg.alert('', 'The Export To Excel feature is restricted to exporting a maximum of 6,000 claims, please refine your search.');
        }
    }
}

function doExportExcel2(){
    if(!claimStore.getCount()){
        Ext.Msg.alert('','No record found, Please try again');
    }else{
        if( claimStore.getTotalCount()>0){
            if ( find_MSIE_version() > 0 && find_MSIE_version() < 9  ){
                Ext.MessageBox.show({
                    title        : 'Exporting Claims...',
                    msg          : "Please wait...",
                    width        : 300,
                    closable     : false
                });
                window.location = contextPath+"/prv/doExportExcel.action?directDownload="+true;
                directExportToExcelStatusIntervelId = setTimeout(loadDirectExportToExcelStatus, 1000);
            }else{
                var timeoutSeconds = (claimStore.getTotalCount()/6000 +1 ) * 3600000;

                cancelled = false;
                choxExtAjaxRequest({
                    url: '/prv/p/generateExportFile.action?newVersion='+true,
                    timeout : timeoutSeconds,
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
                exportToExcelIntervelId = setTimeout(loadLiveExportToExcelClaimCount(10000), 1000 );
            }
        }
        else{
            Ext.Msg.alert('','The Export To Excel feature is restricted to exporting a maximum of 6,000 claims, please refine your search.');
        }
    }
}

function cancelExportToExcel(btn){
    if (btn === 'cancel'){
        cancelled = true;
        Ext.MessageBox.hide();
        exportToExcelIntervelId=window.clearTimeout(exportToExcelIntervelId);
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
        
var loadLiveExportToExcelClaimCount = function updateExportedClaim(timeOut){

    var timeoutSeconds = timeOut;
    choxExtAjaxRequest({
        url: '/prv/p/updateExportClaimsCount.action',
        callback : function(options,success,response  ){
            if(response.responseText){
                var resp = Ext.util.JSON.decode(response.responseText);
                if(!cancelled && resp.isExportProcessFinished){
                    window.location= "doExportExcel.action?";
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
                }else if(resp.tooManyRows){
                    Ext.MessageBox.hide();
                    Ext.MessageBox.show({
                        title: 'Error',
                        msg: 'This data export will exceed the maximum number of allowable rows in Excel (65,536), please reduce the number of exported claims.',
                        width:300,
                        buttons: Ext.MessageBox.OK,
                        icon : Ext.MessageBox.ERROR
                    });
                }else if(!cancelled && claimStore.getTotalCount()>=resp.exportedClaimCount){
                                
                    var i = resp.exportedClaimCount/claimStore.getTotalCount();
                    if(resp.writingToFile){
                        Ext.MessageBox.updateProgress(i, (i*100).toFixed(0) + '% complete', 'Please wait - report is now being exported to an Excel file...');
                    }else{
                        Ext.MessageBox.updateProgress(i, (i*100).toFixed(0) + '% complete', resp.exportedClaimCount+' claims exported');
                    }
                    exportToExcelIntervelId = setTimeout(loadLiveExportToExcelClaimCount, timeoutSeconds);
                }
            }
        }
    });
};

function doNotShowBrowserWarning(){
       
    choxExtAjaxRequest({
        url: '/prv/p/userBrowserWarning.action',
        callback : function(options,success,response  ){
            if(response.responseText){
                var resp = Ext.util.JSON.decode(response.responseText);
                if(resp && resp.isValid){
                    if(resp.resultType && resp.resultType === 'Message')
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
                }else if(resp.exceptionThrown){
                    Ext.MessageBox.hide();
                    Ext.MessageBox.show({
                        title: 'Error',
                        msg: 'Unexpected error occurred. Please contact Chox Support.',
                        width:300,
                        buttons: Ext.MessageBox.OK,
                        icon : Ext.MessageBox.ERROR
                    });
                }else if(resp.tooManyRows){
                    Ext.MessageBox.hide();
                    Ext.MessageBox.show({
                        title: 'Error',
                        msg: 'This data export will exceed the maximum number of allowable rows in Excel (65,536), please reduce the number of exported claims.',
                        width:300,
                        buttons: Ext.MessageBox.OK,
                        icon : Ext.MessageBox.ERROR
                    });
                }else if(!resp.exportCancelled && !cancelled){
                    directExportToExcelStatusIntervelId = setTimeout(loadDirectExportToExcelStatus, 1000);
                }
            }
        }
    });
}

function doTaskExportExcel(params) {
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
                var taskExportUrl = contextPath+"/prv/doTaskExportExcel.action?directDownload="+ true + "&" +"hideCompleted="+hideCompleted + "&" +"showAssignedTasksOnly="+showAssignedTasksOnly;
                if (params) {
                    if (params.supplierClaimOwnerIds) {
                        var extraParam = '';
                        for (var i = 0; i < params.supplierClaimOwnerIds.length; i++) {
                            extraParam = extraParam + '&supplierClaimOwnerIds=' + params.supplierClaimOwnerIds[i];
                        }
                        taskExportUrl = taskExportUrl + extraParam;
                    } else {
                        if (params.workgroupIds) {
                            var extraParam2 = '';
                            for (var i = 0; i < params.workgroupIds.length; i++) {
                                extraParam2 = extraParam2 + '&workgroupIds=' + params.workgroupIds[i];
                            }
                            taskExportUrl = taskExportUrl + extraParam2;
                        }
                        if (params.claimOwnerIds) {
                            var extraParam3 = '';
                            for (var i = 0; i < params.claimOwnerIds.length; i++) {
                                extraParam3 = extraParam3 + '&claimOwnerIds=' + params.claimOwnerIds[i];
                            }
                            taskExportUrl = taskExportUrl + extraParam3;
                        }
                    }
                }
                window.location = taskExportUrl;
                directTaskExportToExcelStatusIntervelId = setTimeout(loadDirectTaskExportToExcelStatus, 1000);
            }else{
                cancelled = false;
                if (!params) {
                    params = {'hideCompleted' : hideCompleted, 'showAssignedTasksOnly' : showAssignedTasksOnly};
                }
                choxExtAjaxRequest({
                    url: "/prv/p/generateTaskExportFile.action",
                    timeout : 3600000,
                    params : params,
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
                taskExportToExcelIntervelId = setTimeout(loadLiveTaskExportToExcelClaimCount, 1000);
            }
        }
        else{
            Ext.Msg.alert('','The Export To Excel feature is restricted to exporting a maximum of 65,536 tasks, please refine your search.');
        }
    }
}
            
function cancelTaskExportToExcel(btn){
    if (btn == 'cancel'){
        cancelled = true;
        Ext.MessageBox.hide();
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
                            msg: 'Export to Excel cancel failed.',
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
                if(!cancelled && resp.isExportProcessFinished){
                    Ext.MessageBox.hide();
                    window.location= "doTaskExportExcel.action?";
                }else if(resp.exceptionThrown){
                    Ext.MessageBox.hide();
                    Ext.MessageBox.show({
                        title: 'Error',
                        msg: 'Unexpected error occurred. Please contact Chox Support.',
                        width:300,
                        buttons: Ext.MessageBox.OK,
                        icon : Ext.MessageBox.ERROR
                    });
                }else if(resp.tooManyRows){
                    Ext.MessageBox.hide();
                    Ext.MessageBox.show({
                        title: 'Error',
                        msg: 'This data export will exceed the maximum number of allowable rows in Excel (65,536), please reduce the number of exported tasks.',
                        width:300,
                        buttons: Ext.MessageBox.OK,
                        icon : Ext.MessageBox.ERROR
                    });
                }else if(!cancelled && !resp.exportCancelled && tasksDataStore.getTotalCount()>=resp.exportedTaskCount){

                    var i = resp.exportedTaskCount/tasksDataStore.getTotalCount();
                    if(resp.writingToFile){
                        Ext.MessageBox.updateProgress(i, (i*100).toFixed(0) + '% complete', 'Please wait - report is now being exported to an Excel file...');
                    }else{
                        Ext.MessageBox.updateProgress(i, (i*100).toFixed(0) + '% complete', resp.exportedTaskCount+' tasks exported');
                    }
                    if (!cancelled) {
                        taskExportToExcelIntervelId = setTimeout(loadLiveTaskExportToExcelClaimCount, 1000);
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
                }else if(resp.exceptionThrown){
                    Ext.MessageBox.hide();
                    Ext.MessageBox.show({
                        title: 'Error',
                        msg: 'Unexpected error occurred. Please contact Chox Support.',
                        width:300,
                        buttons: Ext.MessageBox.OK,
                        icon : Ext.MessageBox.ERROR
                    });
                }else if(resp.tooManyRows){
                    Ext.MessageBox.hide();
                    Ext.MessageBox.show({
                        title: 'Error',
                        msg: 'This data export will exceed the maximum number of allowable rows in Excel (65,536), please reduce the number of exported tasks.',
                        width:300,
                        buttons: Ext.MessageBox.OK,
                        icon : Ext.MessageBox.ERROR
                    });
                }else if (!resp.exportCancelled && !cancelled) {
                    directTaskExportToExcelStatusIntervelId = setTimeout(loadDirectTaskExportToExcelStatus, 1000);
                }
            }
        }
    });
}