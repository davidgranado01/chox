var activityMonitor = function(){
    var interval;
    var enable = true;
    var pingServiceUrl;
    var checkStatusUrl;
    var t;
    return {
        setup : function(pingServiceUrl, checkStatusUrl, intervalTime) {
            this.pingServiceUrl = pingServiceUrl;
            this.checkStatusUrl = checkStatusUrl;
            this.interval = intervalTime;
        },
        refreshViewingStatus : function() {
            if(enable){
                var x = [];
                $("input[name='viewingId']").each(function (i) {
                    var viewingId = $(this).val();
                    x.push(viewingId);
                });
                if(x.length > 0)
                {
                    var param = {};
                    param.claimIds =  x.join(',');
                    Ext.Ajax.request({
                        url:contextPath + this.checkStatusUrl,
                        method : 'GET',
                        callback : function(options,success,response  ){
                            if(response.status==200 && response.responseText){
                                var resp = Ext.util.JSON.decode(response.responseText);
                                if(resp && resp.isValid){
                                    $.each(resp.results, function(i,result){
                                        $("#viewingLabel_" + result.claimId).html(result.status);
                                    }); 
                                }
                            }
                        },
                        params: {
                            claimIds : param.claimIds
                        }
                    });
                }
                t=setTimeout(function() { activityMonitor.refreshViewingStatus(); }, this.interval); 
            }
        },
        clearViewingStatus : function() {
            clearTimeout(t);
        },
        pingServer : function() {
            if(enable){
                Ext.Ajax.request({
                    url:contextPath + this.pingServiceUrl,
                    method : 'GET',
                    callback : function(options,success,response  ){
                        if(response.status==200 && response.responseText){
                            var resp = Ext.util.JSON.decode(response.responseText);
                            if(resp && resp.isValid){
                                if(resp.results.length > 0)
                                {
                                    $("#userViewingThisClaim").empty();
                                    $.each(resp.results, function(i,result){
                                        if(i > 0)
                                        {
                                            $("#userViewingThisClaim").append(', ');
                                        }
                                        $("#userViewingThisClaim").append(result);
                                    });
                                    $("#userViewingThisClaimDiv").show();
                                }
                                else
                                {
                                    $("#userViewingThisClaimDiv").hide();
                                } 
                            }
                        }
                    }
                    });
                t=setTimeout(function() { activityMonitor.pingServer(); }, this.interval); 
            }
        }
    };
}();
