    function registeAction(val)
    {
        $("#actionName").val(val);
    }

    // NOT IN USE - TO BE REMOVED
    function isClaimNumberInvalid(){

        $("#isClaimNumberValidFlag").val("1");
        var sClaimNumber = $("#claimNumber").val();

        if(isSpecialCharacterExist(sClaimNumber)){
            $("#isClaimNumberValidFlag").val("0");
        }

    }

    function isFormClaimNumberInvalid(formName){
        $("form#"+formName+" input[name$='isClaimNumberValidFlag']").val("1");
        var sClaimNumber = $("form#"+formName+" input[name$='claimNumber']").val();

        if(isSpecialCharacterExist(sClaimNumber)){
            $("form#"+formName+" input[name$='isClaimNumberValidFlag']").val("0");
        }
    }
    
    function isSpecialCharacterExist(strClaimNumber){

        if(strClaimNumber.length>0){
            var iChars = "!£$%^&*+=-_{[}]#~'@;:/?.>,<"+'"';
            for (var i = 0; i < strClaimNumber.length; i++) {
                if (iChars.indexOf(strClaimNumber.charAt(i)) != -1) {
                    return true;
                }
            }
        }
        return false;
    }

    function isRejected(){
        var sActionName = $("#actionName").val();
        if(sActionName=="reject"){
            return true;
        }
        return false;
    }

    function checkAndConfirmClaimNumberDuplication(sClaimNumber, sClaimId, form)
    {

        if(sClaimNumber && sClaimNumber != null)
        {
            $.getJSON("checkIsClaimNumberDuplicated.action", { claimNumber: sClaimNumber, claimId: sClaimId },

            function(data){

                if(data.isValid)
                {
                    if(data.result && data.result == "yes"){
                        if(confirm("The claim number you have supplied is already associated with another claim(s). Do you wish to continue?"))
                        {
                           form.submit();
                        }
                    }
                    else{
                        form.submit();
                    }
                }
                else
                {
                    propmtErrors(data.errors);
                }
            });

        }
        else
        {
            form.submit();
        }
    }

    function isClaimNumberMandatory(){
        var sActionName = $("#actionName").val();

        if(sActionName=="reject" || sActionName=="referFNOL" || sActionName=="pending"){
            return false;
        }
        
        return true;
    }

    /* EXTRA ACTION PANEL */
    function doShowHideExtraAction(a, b){
        if(b){
            $("#"+a).css("display:", "block");
            $("#"+a).slideDown();
        }else{
            $("#"+a).slideUp();
            $("#"+a).css("display:", "none");
            $("#extraAction").val("");

        }
    }

    function extraActionChange(){

        var selectedAction = $("#extraAction").val();
        $(".extraActionClass").slideUp();
        $(".extraActionClass").css("display:", "none");

        if(selectedAction!=null && selectedAction!=""){
            doShowHideExtraAction(selectedAction, 1);
        }
    }