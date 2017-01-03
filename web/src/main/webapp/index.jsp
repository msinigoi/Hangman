<jsp:useBean id="game" class="com.sinigoi.apputils.Game"/>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Hangman, the game!</title>
        <link href="css/bootstrap.css" rel="stylesheet">
        <link href="css/hangman.css" rel="stylesheet">
        <script src="js/jquery-3.1.1.min.js"></script>
        <script src="js/bootstrap.js"></script>
        <script src="js/showModals.js"></script>
    </head>
    <body>
        <div class="container-fluid text-center">
            <h1>Hangman, the game!</h1>
            <br>
            <div class="row">
                <div class="col-md-6 offset-md-3">
                  <div class="btn-group btn-group-lg" role="group" aria-label="...">
                    <form action="${pageContext.request.contextPath}/Hangman" method="post">
                        <input class="btn btn-primary btn-lg" id="newGameButton" type="submit" name="newGame" value="New Game" />
                        <input class="btn btn-primary btn-lg" id="resumeGameButton" type="submit" name="resumeGame" value="Resume Game" />
                    </form>
                    </div>
                </div>
                <div class="col-md-3 offset-md-9">
                    <form action="${pageContext.request.contextPath}/Hangman" method="get">
                        <input class="btn btn-info btn-lg" id="managementButton" type="submit" name="management" value="Management Page" />
                    </form>
                </div>
            </div>
            <br>
            <div class="row">
                <div class="col-md-6 offset-md-3">
                    <form class="form-inline" action="${pageContext.request.contextPath}/Hangman" method="post">
                        <div class="form-group">
                            <input class="form-control form-control-warning" type="text" name="clue" maxlength="1" pattern="[A-Za-z]{1}" title="One letter only!"/>
                            <input class="btn btn-primary" id="sendClueButton" type="submit" name="sendClue" value="Go" />
                        </div>
                    </form>
                </div>
            </div>
            <div class="row">
                <div class="col-md-6 offset-md-3">
                    <div id="hangmanWord">
                        ${word}
                        <c:if test="${complete=='true'}">
                            <script>
                                showYouWinModal();
                            </script>
                        </c:if>
                    </div>
                </div>
                <div class="col-md-3 offset-md-9">
                    <div id="hangmanPicture">
                        <c:choose>
                            <c:when test="${errors == '1'}">
                                <img src="img/1error.jpg" alt="${errors}" height="200" width="300">
                            </c:when>
                            <c:when test="${errors == '2'}">
                                <img src="img/2error.jpg" alt="${errors}" height="200" width="300">
                            </c:when>
                            <c:when test="${errors == '3'}">
                                <img src="img/3error.jpg" alt="${errors}" height="200" width="300">
                            </c:when>
                            <c:when test="${errors == '4'}">
                                <img src="img/4error.jpg" alt="${errors}" height="200" width="300">
                            </c:when>
                            <c:when test="${errors == '5'}">
                                <img src="img/5error.jpg" alt="${errors}" height="200" width="300">
                            </c:when>
                            <c:when test="${errors == '6'}">
                                <img src="img/6error.jpg" alt="${errors}" height="200" width="300">
                            </c:when>
                            <c:when test="${errors == '7'}">
                                <img src="img/7error.jpg" alt="${errors}" height="200" width="300">
                            </c:when>
                            <c:when test="${errors >= '8'}">
                                <img src="img/8error.jpg" alt="${errors}" height="200" width="300">
                                <script>
                                    showGameOverModal();
                                </script>
                            </c:when>
                        </c:choose>
                    </div>
                </div>
            </div>
            <br>
            <div class="row">
                <div class="col-md-6 offset-md-3">
                    <div id="hangmanLog">
                        ${log}
                    </div>
                </div>
            </div>
        </div>
        <div class="modal fade" id="youWinModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title" id="myModalLabel">Success</h4>
                    </div>
                    <div class="modal-body">
                        <p>Congratulations, you win!</p>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                    </div>
                </div>
            </div>
        </div>
        <div class="modal fade" id="gameOverModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title" id="myModalLabel">Game Over</h4>
                    </div>
                    <div class="modal-body">
                        <p>Try again!</p>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
