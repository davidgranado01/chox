var activityMonitor = function(){
    var interval = 10000;
    var enable = false;
    return {
        refreshViewingStatus : function() {

            if(enable){
                var x = [];

                $("input[name='viewingId']").each(function (i) {
                    var claimId = $(this).val();
                    x.push(claimId);
                });

                if(x.length > 0)
                {
                    var url = 'checkViewingStatus.action';
                    var param = {};
                    param.claimIds =  x.join(',');
                    ajax.loadJson(url,param,function(data){
                        $.each(data.results, function(i,result){
                            $("#viewingLabel_" + result.claimId).html(result.status);
                        });

                    });
                }
                t=setTimeout("activityMonitor.refreshViewingStatus()",interval);
            }

        },
        pingServer : function(claimId){

            if(enable){
                var url = 'activityMonitoringAction.action';

                ajax.loadJson(url,{
                    "claimId":claimId
                },function(data){
                    if(data.results.length > 0)
                    {
                        $("#userViewingThisClaim").empty();
                        $.each(data.results, function(i,result){
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

                });
                t=setTimeout("activityMonitor.pingServer()",interval);
            }
        }
    };
}();
