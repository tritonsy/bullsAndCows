<%--@elvariable id="game" type="com.bullsncows.entity.Game"--%>
<%--@elvariable id="error" type="java.lang.String"--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@include file="/resources/templates/includes.jsp" %>

    <title>Game#${game.id}</title>

    <meta id="_csrf_token" value="${_csrf.token}"/>
    <script>
        <%@include file="/resources/js/game.page.js" %>
    </script>
    <style>
        .btn-group {
            width: 100%;
        }
    </style>
</head>
<body>
<%@include file="/resources/templates/header.jsp" %>
<div class="top-content">
    <div class="container">
        <div class="row">
            <c:if test="${error != null}">
                <div class="alert alert-danger">
                    <p>${error}</p>
                </div>
            </c:if>
        </div>
        <div class="row">
            <div class="col-6">
                <br>
                <div role="form" class="btn-group d-flex">
                    <div class="btn-group">
                        <div id="1" class="form-control text-center">0</div>
                        <div class="btn-group d-flex" role="group" aria-label="plus-minus">
                            <div class="btn-group">
                                <button type="button" class="btn btn-danger minus" disabled="disabled" number="1">-
                                </button>
                            </div>
                            <div class="btn-group">
                                <button type="button" class="btn btn-success plus" number="1">+</button>
                            </div>
                        </div>
                    </div>

                    <div class="btn-group">
                        <div id="2" class="form-control text-center">0</div>
                        <div class="btn-group d-flex" role="group" aria-label="plus-minus">
                            <div class="btn-group">
                                <button type="button" class="btn btn-danger minus" disabled="disabled" number="2">-
                                </button>
                            </div>
                            <div class="btn-group">
                                <button type="button" class="btn btn-success plus" number="2">+</button>
                            </div>
                        </div>
                    </div>

                    <div class="btn-group">
                        <div id="3" class="form-control text-center">0</div>
                        <div class="btn-group d-flex" role="group" aria-label="plus-minus">
                            <div class="btn-group">
                                <button type="button" class="btn btn-danger minus" disabled="disabled" number="3">-
                                </button>
                            </div>
                            <div class="btn-group">
                                <button type="button" class="btn btn-success plus" number="3">+</button>
                            </div>
                        </div>
                    </div>

                    <div class="btn-group">
                        <div id="4" class="form-control text-center">0</div>
                        <div class="btn-group d-flex" role="group" aria-label="plus-minus">
                            <div class="btn-group">
                                <button type="button" class="btn btn-danger minus" disabled="disabled" number="4">-
                                </button>
                            </div>
                            <div class="btn-group">
                                <button type="button" class="btn btn-success plus" number="4">+</button>
                            </div>
                        </div>
                    </div>
                </div>

                <br>
                <div role="form" class="btn-group d-flex">
                    <button id="send" class="btn btn-outline-info" onclick="sendAttempt()">Check</button>
                </div>
                <div class="col">
                    <div id="answerDiv" class="row alert alert-info">
                        <textarea id="answer" class="col"
                                  style="height: 50px; text-align:center; resize: vertical; min-height: 50px;max-height: 150px"
                                  readonly> </textarea>
                    </div>
                </div>
                <br>
                <div role="form" class="btn-group">
                    <a class="btn btn-primary btn-group" href="<c:url value="/newGame"/>">New game</a>
                    <div class="btn-group">
                        <button type="button" class="btn btn-info dropdown-toggle" data-toggle="dropdown">
                            Load...
                        </button>
                        <ul class="dropdown-menu">
                            <li class="divider"></li>
                            <c:forEach items="${list}" var="unfinished">
                                <li class="list-group-item text-center">
                                    <a href="<c:url value="/game/${unfinished.id}"/>">Game#${unfinished.id}</a>
                                </li>
                            </c:forEach>
                        </ul>
                    </div>
                </div>
            </div>

            <div class="col-6">
                <br>
                <table class="table table-hover table-bordered">
                    <thead>
                    <tr>
                        <th scope="col">Number</th>
                        <th scope="col">Attempt</th>
                        <th scope="col">Answer</th>
                    </tr>
                    </thead>
                    <tbody id="attempts">
                    <c:forEach items="${game.attempts}" var="Attempt" varStatus="status">
                        <tr>
                            <th scope="row">${status.index+1}</th>
                            <td>${Attempt.number}</td>
                            <td id="answer${status.index}"></td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
</body>
</html>
