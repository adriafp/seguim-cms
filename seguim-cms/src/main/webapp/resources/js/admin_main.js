$("#jqSelAddWebsite").bind("click",function() {
    $("#websiteForm").find(".control-group").removeClass("error");
    $.post(CONTEXT+"/admin/website/add",$("#websiteForm").serialize(),function(obj){
        if(obj.success) {
            document.location.reload();
        } else {
            for(var i = 0; i < obj.data.length; i++) {
                console.debug(obj.data[i].field + "::" + obj.data[i].defaultMessage);
                $("#"+obj.data[i].field).closest(".control-group").addClass("error")
                    .find(".help-inline").html(obj.data[i].defaultMessage);
            }
        }
        console.debug(obj);
    });
});

$(".jqSelRemoveWebsite").bind("click",function(){
    if(confirm("Are you sure?")) {
        var obj = $(this);
        $.post(CONTEXT+"/admin/website/del/"+obj.attr("data-id"), function(result) {
            if(result.success) {
                $("#jqSelWebsite_"+obj.attr("data-id")).fadeOut("slow").remove();
                drawChart();
            }
        });
    }
});