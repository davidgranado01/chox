var activityMonitor = function(){
    var interval = 5000;
    var enable = true;
    var pingServiceUrl;
    var checkStatusUrl;
    var claimId;
    var t;
    return {
        setup : function(pingServiceUrl,checkStatusUrl,claimId){
            this.pingServiceUrl = pingServiceUrl;
            this.checkStatusUrl = checkStatusUrl;
            this.claimId = claimId;
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
                        url:this.checkStatusUrl,
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
                t=setTimeout('activityMonitor.refreshViewingStatus()',interval);
            }
        },
        clearViewingStatus : function() {
            clearTimeout(t);
        },
        pingServer : function(){
            if(enable){
                Ext.Ajax.request({
                    url:this.pingServiceUrl,
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
                    },
                    params: {
                        claimId :this.claimId
                    }
                });
                t=setTimeout("activityMonitor.pingServer()", interval);
            }
        }
    };
}();
