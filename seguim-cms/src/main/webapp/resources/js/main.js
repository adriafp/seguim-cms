$("#jqSelRegister").bind("click",function() {
    $("#registerForm").find(".control-group").removeClass("error");
    $.post(CONTEXT+"/register",$("#registerForm").serialize(),function(obj){
        if(obj.success) {
            document.location = CONTEXT + '/admin';
        } else {
            if(obj.data) {
                for(var i = 0; i < obj.data.length; i++) {
                    console.debug(obj.data[i].field + "::" + obj.data[i].defaultMessage);
                    $("#"+obj.data[i].field).closest(".control-group").addClass("error")
                        .find(".help-inline").html(obj.data[i].defaultMessage);
                }
            }
        }
        console.debug(obj);
    });
});
