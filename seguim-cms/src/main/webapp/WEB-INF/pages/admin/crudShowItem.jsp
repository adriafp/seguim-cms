<%@ taglib prefix="saws" uri="http://www.seguim.com/taglibs/saws" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<div class="container">
    Showing item "${itemName}" from table "${domainName}":<br>

    <c:set var="result" value="${saws:get(domainName, itemName)}"/>
    <c:if test="${!empty result}">
        <table class="table table-bordered table-hover">
            <thead>
            <tr>
                <td>Item Name</td>
                <c:forEach items="${result}" var="attr">
                    <td>${attr.key}</td>
                </c:forEach>
                <td>Actions</td>
            </tr>
            </thead>
            <tr>
                <td>${itemName}</td>
                <c:forEach items="${result}" var="attr">
                    <td>${attr.value}</td>
                </c:forEach>
                <td><a href="${pageContext.request.contextPath}/crud/${domainName}/${result}">del</a></td>
            </tr>
        </table>
    </c:if>
</div>