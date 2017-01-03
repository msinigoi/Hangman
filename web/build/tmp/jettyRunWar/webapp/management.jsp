<jsp:useBean id="game" class="com.sinigoi.apputils.Game"/>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Hangman, the game! - Management Page</title>
        <link href="css/bootstrap.css" rel="stylesheet">
        <script src="http://code.jquery.com/jquery-latest.min.js"></script>
        <script src="js/bootstrap.js"></script>
        <script src="js/back.js"></script>
    </head>
    <body>
        <div class="container-fluid text-center">
            <h1>Management page</h1>
            <br>
            <div class="row">
                <div class="col-md-6 offset-md-3">
                    <button class="btn btn-primary btn-lg" id="backButton" onclick="goBack()">Back</button >
                </div>
                <div class="col-md-6 offset-md-9">
                    <form action="${pageContext.request.contextPath}/Hangman" method="get">
                        <input class="btn btn-info btn-lg" id="managementButton" type="submit" name="management" value="Refresh" />
                    </form>
                </div>
            </div>
            <br>
            <h3>Ongoing games</h3>
            <br>
            <div id="ongoingGames">
                <c:forEach items="${ongoingGames}" var="ongoingGames">
                    <c:forEach items="${ongoingGames.wordInProgress}" var="i" begin="0">
                        <c:out value="${i}"/>
                    </c:forEach>
                    <c:out value=" - errors: ${ongoingGames.errorCount}"/>
                    <br>
                </c:forEach>
            </div>
        </div>
    </body>
</html>