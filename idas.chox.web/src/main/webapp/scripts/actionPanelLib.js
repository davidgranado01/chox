var actionPanel = function(){
    
    function registerAction(name)
    {
        $("#name").val(name);
    }
    
    function handleExtraActionChange(){
        var selectedAction = $("#extraAction").val();
        $(".extra-action-class").slideUp();
        $(".extra-action-class").css("display:", "none");
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

Ext.onReady(function() {
    $.validator.addMethod("textDigitOnly",
        function(value, element) {
            return /^[a-zA-Z0-9]*$/.test(value);
        },
        "Invalid Claim Number Format, accept alphabet and numberic only"
        );
});
