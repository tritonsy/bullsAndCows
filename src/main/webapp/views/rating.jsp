<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@include file="/resources/templates/includes.jsp" %>

    <title>Players rating</title>
</head>
<body>
<%@include file="/resources/templates/header.jsp" %>
<div class="container center-block">
    <h1>Rating players</h1>
    <table class="table table-hover table-bordered">
        <thead class="thead-dark">
        <tr>
            <th scope="col">#</th>
            <th scope="col">Player</th>
            <th scope="col">Score</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${rating}" var="item" varStatus="status">
            <tr>
                <th scope="row">${status.index+1}</th>
                <td><a href="<c:url value="/person/${item.login}"/>">${item.login}</a></td>
                <td>${item.average}</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
</body>
</html>
