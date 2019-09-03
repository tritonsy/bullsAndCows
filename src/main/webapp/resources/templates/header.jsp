<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<script>
    function submit() {
        $('#logout').submit();
    }
</script>

<nav class="navbar navbar-expand-lg navbar-light bg-light">
    <a class="navbar-brand" href="#">Bulls and Cows</a>
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent"
            aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>

    <div class="collapse navbar-collapse" id="navbarSupportedContent">
        <ul class="navbar-nav mr-auto">
            <li class="nav-item active">
                <sec:authorize access="isAuthenticated()">
                    <sec:authentication var="principal" property="principal"/>
                    <div class="nav-item active">
                        <a href="<c:url value="/person/${principal.username}"/>" class="btn btn-default">
                            User page
                            <span class="sr-only">(current)</span> </a>
                    </div>
                </sec:authorize>
            </li>
            <li class="nav-item">
                <sec:authorize access="isAuthenticated()">
                    <div class="btn-group">
                        <a href="<c:url value="/game"/>" class="btn btn-default">Game</a>
                    </div>
                </sec:authorize>
            </li>
            <li class="nav-item">
                <div class="btn-group">
                    <a href="<c:url value="/rating"/>" class="btn btn-default">Players rating</a>
                </div>
            </li>
        </ul>
        <sec:authorize access="isAuthenticated()">
            <form class="form-inline my-2 my-lg-0" method="post" action="<c:url value="/logout"/>" id="logout">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                <button type="button" onclick="submit()" class="btn btn-outline-success my-2 my-sm-0">Log out
                </button>
            </form>
        </sec:authorize>
        <sec:authorize access="!isAuthenticated()">
            <a href="<c:url value="/login"/>" class="btn btn-outline-success my-2 my-sm-0">Log in</a>
        </sec:authorize>
    </div>
</nav>