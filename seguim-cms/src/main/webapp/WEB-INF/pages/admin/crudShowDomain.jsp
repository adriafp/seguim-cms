<%@ taglib prefix="saws" uri="http://www.seguim.com/taglibs/saws" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<div class="container">
Showing items of table: ${domainName}:<br>

<c:set var="result" value="${saws:search(domainName, 'itemName() is not null', 'itemName()', -1)}"/>
<c:if test="${!empty result}">
    <table class="table table-bordered table-hover">
        <c:forEach items="${result}" var="item" varStatus="vs">
            <c:if test="${vs.first}">
                <thead>
                <tr>
                    <td>Name</td>
                    <c:forEach items="${item.value}" var="attr">
                        <td>${attr.key}</td>
                    </c:forEach>
                    <td>Actions</td>
                </tr>
                </thead>
            </c:if>
            <tr>
                <td>${item.key}</td>
                <c:forEach items="${item.value}" var="attr">
                    <td>${attr.value}</td>
                </c:forEach>
                <td><a href="${pageContext.request.contextPath}/crud/${domainName}/${item.key}">edit</a></td>
            </tr>
        </c:forEach>
    </table>
</c:if>

    <hr>
    La millor manera de no retornar valor falsos es fer una cache i treballar amb ella, tant amb resultats com la resta del CRUD.
    <br>
    Seria una bona idea tenir un xml/json amb la definició de les taules, de manera que tant l'admin del crud com la resta esta bassat amb la conf del xml/json.
    <br>
    Seria bona idea, tenir una taula de sequencies i fer que tots els itemsNames fossin: {itemName}_{seq.nextval} user_1, user_2, etc...

</div>
