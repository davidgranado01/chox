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
    
    function registerAction(name)
    {
        $("#name").val(name);
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
