<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login</title>
    <%@include file="/resources/templates/includes.jsp" %>
    <link rel="stylesheet" type="text/css"
          href="<c:url value="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css"/>"/>
    <link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/form-elements.css"/>"/>
    <link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/style.css"/>"/>
    <link rel="stylesheet" href="http://fonts.googleapis.com/css?family=Roboto:400,100,300,500">
</head>

<body>
<%@include file="/resources/templates/header.jsp" %>
<div class="container">
    <div class="row">
        <c:if test="${registrationSuccess != null}">
            <div class="alert alert-success">
                <p>${registrationSuccess}</p>
            </div>
        </c:if>
        <c:if test="${param.error != null}">
            <div class="alert alert-danger">
                <p>Invalid username and password.</p>
            </div>
        </c:if>
        <c:if test="${param.logout != null}">
            <div class="alert alert-success">
                <p>You have been log out successfully</p>
            </div>
        </c:if>
    </div>
    <div class="top-content">
        <div class="container">
            <div class="row">
                <div class="col-sm-5">
                    <div class="form-box">
                        <div class="form-top">
                            <div class="form-top-left">
                                <h3>Login to our site</h3>
                                <p>Enter username and password to log on:</p>
                            </div>
                            <div class="form-top-right">
                                <i class="fa fa-lock"></i>
                            </div>
                        </div>
                        <div class="form-bottom">
                            <c:url var="loginUrl" value="/login"/>
                            <form id="signIn" action="${loginUrl}" method="post" role="form">
                                <div class="form-group">
                                    <input type="text" class="form-control" id="username" name="login"
                                           placeholder="Enter Username"
                                           required>
                                </div>
                                <div class="form-group">
                                    <input type="password" class="form-control" id="password" name="pass"
                                           placeholder="Enter Password" required>
                                </div>
                                <div class="form-check">
                                    <input type="checkbox" name="remember-me" class="form-check-input"
                                           id="dropdownCheck2">
                                    <label class="form-check-label" for="dropdownCheck2">Remember me</label>
                                </div>
                                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                <button type="submit" form="signIn" class="btn">Sign in</button>
                            </form>
                        </div>
                    </div>
                </div>
                <div class="col-sm-1 middle-border"></div>
                <div class="col-sm-1"></div>
                <div class="col-sm-5">
                    <div class="form-box">
                        <div class="form-top">
                            <div class="form-top-left">
                                <h3>Sign up now</h3>
                                <p>Sign up form:</p>
                            </div>
                            <div class="form-top-right">
                                <i class="fa fa-pencil"></i>
                            </div>
                        </div>
                        <div class="form-bottom">
                            <c:url var="registrationUrl" value="/registration"/>
                            <form:form id="signUp" method="POST" action="${registrationUrl}"
                                       modelAttribute="registrationForm"
                                       class="form-signin" role="form">
                                <spring:bind path="login">
                                    <div class="form-group ${status.error ? 'has-error' : ''}">
                                        <form:input id="regUsername" type="text" path="login" class="form-control"
                                                    placeholder="Enter Username"/>
                                        <form:errors path="login"/>
                                    </div>
                                </spring:bind>
                                <spring:bind path="password">
                                    <div class="form-group ${status.error ? 'has-error' : ''}">
                                        <form:input id="regPassword" type="password" path="password"
                                                    class="form-control"
                                                    placeholder="Enter Password"/>
                                        <form:errors path="password"/>
                                    </div>
                                </spring:bind>
                                <spring:bind path="passwordConfirm">
                                    <div class="form-group ${status.error ? 'has-error' : ''}">
                                        <form:input id="regConfirm" type="password" path="passwordConfirm"
                                                    class="form-control"
                                                    placeholder="Confirm your password"/>
                                        <form:errors path="passwordConfirm"/>
                                    </div>
                                </spring:bind>
                                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                <button type="submit" form="signUp" class="btn">Sign up</button>
                            </form:form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>

</html>
