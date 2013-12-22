<%@ taglib prefix="saws" uri="http://www.seguim.com/taglibs/saws" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

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
<c:if test="${empty param.dirName}" var="emptyParamDirName">
    <c:set var="dirName" value="vv/"/>
    <c:set var="rootFolder" value="true/"/>
</c:if>
<c:if test="${!emptyParamDirName}">
    <c:set var="dirName" value="${param.dirName}"/>
</c:if>


<c:set var="objectListing" value="${saws:listObjects('media.seguim.com', dirName,100)}"/>

<div class="container">

    <c:set var="trackValue" value=""/>
    <c:forTokens items="${dirName}" delims="/" var="item" varStatus="vs">
        <c:set var="trackValue" value="${trackValue}${item}/"/>
        <c:if test="${vs.count==1}"><a href="${pageContext.request.contextPath}/">all files</a>
            <c:if test="${!rootFolder}"> <a>></a> </c:if>
        </c:if>
        <c:if test="${vs.count>2}"><a>></a> </c:if>
        <c:if test="${vs.count>1}"> <a href="${pageContext.request.contextPath}/?dirName=${trackValue}"><c:out value='${item}'/></a></c:if>
    </c:forTokens>
    <br>
    <c:forEach items="${objectListing.commonPrefixes}" var="folder">
        <c:set var="folderName" value="${fn:substring(folder,fn:length(dirName),fn:length(folder)-1)}"/>
        <a href="${pageContext.request.contextPath}/?dirName=${folder}">${folderName}</a><br>
    </c:forEach>

    <c:forEach items="${objectListing.objectSummaries}" var="object">
        <c:set var="file" value="${object.key}"/>
        <a href="http://media.seguim.com/${file}">${fn:substring(file,fn:length(dirName),fn:length(file))}</a><br>
    </c:forEach>

</div> <!-- /container -->
