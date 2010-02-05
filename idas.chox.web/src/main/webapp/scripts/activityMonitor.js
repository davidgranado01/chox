var activityMonitor = function(){
    var interval = 15000;
    var enable = true;
    var pingServiceUrl;
    var checkStatusUrl;
    var claimId;

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
                    ajax.loadJson(this.checkStatusUrl,param,function(data){
                        $.each(data.results, function(i,result){
                            $("#viewingLabel_" + result.claimId).html(result.status);
                        });

                    });
                }
                
                t=setTimeout('activityMonitor.refreshViewingStatus()',interval);
            }

        },
        pingServer : function(){
            
            if(enable){

                var param = {"claimId":this.claimId};

                $.post(this.pingServiceUrl, param, function(data, textStatus){

                    if(data.isValid){
                        
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
                        
                    }
                    
                    t=setTimeout("activityMonitor.pingServer()", interval);
                    
                },'json');

                /*
                ajax.loadJson(this.pingServiceUrl, {"claimId":this.claimId}, function(data){
                    
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
                t=setTimeout("activityMonitor.pingServer()", interval);
                */
                
            }
        }
    };
}();
