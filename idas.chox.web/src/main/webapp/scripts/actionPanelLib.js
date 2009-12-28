var actionPanel = function(){

    function isContainsSpecialCharacter(source){

        if(source.length>0){
            var iChars = "!£$%^&*+=-_{[}]#~'@;:/?.>,<"+'"';
            for (var i = 0; i < source.length; i++) {
                if (iChars.indexOf(source.charAt(i)) != -1) {
                    return true;
                }
            }
        }
        return false;
    }

    function checkClaimNumberDuplicationAndSubmit(sClaimNumber, sClaimId, form)
    {
        if(sClaimNumber && sClaimNumber.length > 0)
        {
            var url = "<%=request.getContextPath()%>/ajax/checkIsClaimNumberDuplicated.action";
            var param = {
                claimNumber: sClaimNumber,
                claimId: sClaimId
            };

            ajax.loadJson(url,param,function(data){
                if(data.result && data.result == "yes"){
                    if(confirm("The claim number you have supplied is already associated with another claim(s). Do you wish to continue?"))
                    {
                        form.submit();
                    }
                }
                else form.submit();
            });

        }
        else{
            form.submit();
        }
    }

    function registerAction(actionName)
    {
        $("#actionName").val(actionName);
    }

    function handleExtraActionChange(){
        var selectedAction = $("#extraAction").val();
        $(".extraActionClass").slideUp();
        $(".extraActionClass").css("display:", "none");
        if(selectedAction!=null && selectedAction!=""){
            $("#"+a).css("display:", "block");
            $("#"+a).slideDown();
        }
    }
    
    return {        
        checkClaimNumberDuplicationAndSubmit : checkClaimNumberDuplicationAndSubmit,
        registerAction : registerAction,
        onExtraActionChange : handleExtraActionChange
    };

}();

$(function(){

    $.validator.addMethod("textDigitOnly",
        function(value, element) {
            return /[a-zA-Z0-9]*/.test(value);
        },
        "Invalid format, this field accept alphabet and numberic only"
        );

});
