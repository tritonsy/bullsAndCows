<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@include file="/resources/templates/includes.jsp" %>
    <title>HTTP Status 404 – Not Found</title>
</head>
<body>
<%@include file="/resources/templates/header.jsp" %>
<h1>404 – Not Found</h1>
<hr class="line"/>
<c:choose>
    <c:when test="${error == null}">
        <p><b>Type</b> Status Report</p>
        <p><b>Description</b> Something went wrong.</p>
    </c:when>
    <c:otherwise>
        <p><b>Type</b> Illegal player's nickname</p>
        <p><b>Description</b>${error}</p>
        <p>Illegal player's nickname</p>
    </c:otherwise>
</c:choose>
<hr class="line"/>
</body>
</html>
