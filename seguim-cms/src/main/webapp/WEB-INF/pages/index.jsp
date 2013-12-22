<%@ taglib prefix="saws" uri="http://www.seguim.com/taglibs/saws" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!-- Main hero unit for a primary marketing message or call to action -->
<div class="hero-unit">
    <h1>Website Monitor</h1>
    <p>
        <strong>The easiest way to monitor your website.</strong>
    </p>
    <ul>
        <li>It's totally free.</li>
        <li>Easy to register.</li>
        <li>E-mail notifications.</li>
    </ul>
</div>

<div class="container">
    <c:forEach items="${saws:getListFolders('media.seguim.com', 'vv')}" var="folder">
        ${folder}<br>
    </c:forEach>
    <c:forEach items="${saws:getListFiles('media.seguim.com', 'vv')}" var="file">
        ${file}<br>
    </c:forEach>
</div> <!-- /container -->
