<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<table class="table table-hover table-striped">
    <thead>
    <tr>
        <th>Description</th>
        <th>Url</th>
        <th>Status code</th>
        <th>Response time</th>
        <th>Actions</th>
    </tr>
    </thead>
    <tbody>
<c:forEach items="${websites}" var="website">
    <tr id="jqSelWebsite_${website.id}">
        <td>${website.description}</td>
        <td>${website.url}</td>
        <td>
            <c:if test="${website.lastPing.responseCode==200}">
                <button class="btn btn-success disabled">${website.lastPing.responseCode}</button>
            </c:if>
            <c:if test="${website.lastPing.responseCode!=200}">
                <button class="btn btn-danger disabled">${website.lastPing.responseCode}</button>
            </c:if>
        </td>
        <td>${website.lastPing.responseTime} ms</td>
        <td>
            <div class="btn-toolbar" style="margin: 0;">
                <div class="btn-group">
                    <%--<a class="btn btn-mini jqSelEditWebsite" href="#" onclick="return false;" title="Edit" data-id="${website.id}"><i class="icon-pencil"></i></a>--%>
                    <a class="btn btn-danger btn-mini jqSelRemoveWebsite" href="#" onclick="return false;" title="Remove" data-id="${website.id}"><i class="icon-trash icon-white"></i></a>
                </div>
            </div>
        </td>
    </tr>
</c:forEach>
    </tbody>
</table>

<a href="#addWebsiteModal" role="button" class="btn btn-primary" data-toggle="modal">Add</a>

<div id="addWebsiteModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="addWebsiteModalLabel" aria-hidden="true">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">x</button>
        <h3 id="addWebsiteModalLabel">Add Website</h3>
    </div>
    <div class="modal-body">
        <form:form cssClass="form-horizontal" commandName="websiteForm" method="post" action="${pageContext.request.contextPath}/admin/website/add">
            <div class="control-group">
                <label class="control-label" for="description">Description</label>
                <div class="controls">
                    <form:input path="description" id="description" placeholder="Description" />
                    <span class="help-inline"></span>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label" for="url">Url</label>
                <div class="controls">
                    <form:input path="url" id="url" placeholder="Url" />
                        <span class="help-inline"></span>
                </div>
            </div>
        </form:form>
    </div>
    <div class="modal-footer">
        <button class="btn" data-dismiss="modal" aria-hidden="true">Close</button>
        <button class="btn btn-primary" id="jqSelAddWebsite">Add</button>
    </div>
</div>

<script type="text/javascript" src="https://www.google.com/jsapi"></script>
<script type="text/javascript">
    google.load("visualization", "1", {packages:["corechart"]});
    google.setOnLoadCallback(drawChart);

    function drawChart() {
        var jsonData = $.ajax({
            url: "${pageContext.request.contextPath}/admin/pings",
            dataType:"json",
            async: false
        }).responseText;

        // Create our data table out of JSON data loaded from server.
        var data = new google.visualization.DataTable(jsonData);

        var options = {
            title: 'Websites response time (in milliseconds)'
        };

        var chart = new google.visualization.LineChart(document.getElementById('chart_div'));
//        var chart = new google.visualization.AreaChart(document.getElementById('chart_div'));
        chart.draw(data, options);
    }
    //setInterval(drawChart, 10000);
</script>
<div id="chart_div" style="width: 900px; height: 500px;"></div>
