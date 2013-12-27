<%@ taglib prefix="saws" uri="http://www.seguim.com/taglibs/saws" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>

<c:set var="testNew1" value="${saws:new()}"/>
<c:set target="${testNew1}" property="name" value="adria2"/>
<c:set target="${testNew1}" property="surname" value="febrer2"/>

${saws:saveOrUpdate("test1", "user2", testNew1)}

<%--<c:set var="user4" value="${saws:get('test1', 'user4')}"/>--%>
<%--<c:set target="${user4}" property="name" value="adria4"/>--%>
<%--<c:set target="${user4}" property="surname" value="febrer4"/>--%>
<%--${saws:saveOrUpdate("test1", "user4", user4)}--%>


<%--${saws:search("test1", "name like 'adria%'", "", -1)}--%>
<%----%>
<%--<hr>--%>
<%--${saws:search("test1", "itemName() = 'user4'", "", -1)}--%>
<%----%>
<%--<hr>--%>
<%--<c:set var="items" value="${saws:search('test1', 'itemName() is not null', 'itemName() desc', -1)}"/>--%>
<%--<c:forEach items="${items}" var="item">--%>
    <%--${saws:del("test1", item.key)}--%>
<%--</c:forEach>--%>

<hr>
${saws:search('test1', 'itemName() is not null', 'itemName() desc', -1)}