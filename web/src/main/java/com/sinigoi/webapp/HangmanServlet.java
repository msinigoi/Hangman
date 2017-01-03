package com.sinigoi.webapp;

import com.sinigoi.apputils.Game;
import com.sinigoi.apputils.GameCenter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/Hangman")
public class HangmanServlet extends HttpServlet {

    private static final String ONGOING_GAMES_STRING = "ongoingGames";
    private static final String CONTENT_TYPE_STRING = "text/plain";
    private static final String CHARACTER_ENCODING_STRING = "UTF-8";
    private static final String MANAGEMENT_PAGE_ADDRESS_STRING = "/management.jsp";
    private static final String NEW_GAME_PARAMETER_STRING = "newGame";
    private static final String CLUE_PARAMETER_STRING = "clue";
    private static final String RESUME_GAME_PARAMETER_STRING = "resumeGame";
    private static final String GAME_NOT_FOUND_STRING = "Game not found, please start a new one";
    private static final String NEW_GAME_STRING = "New game created";
    private static final String GAME_RESUMED_STRING = "Game resumed";
    private static final String COOKIE_PARAM_STRING = "firstSessionId";
    private static final String WORD_STRING = "word";
    private static final String COMPLETE_STRING = "complete";
    private static final String ERRORS_STRING = "errors";
    private static final String LOG_STRING = "log";
    private static final String INDEX_PAGE_ADDRESS = "index.jsp";
    private GameCenter gameCenter;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            gameCenter = new GameCenter();
            gameCenter.loadGames();
            gameCenter.removeOlderGames();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.setAttribute(ONGOING_GAMES_STRING, gameCenter.getOngoingGames());
        response.setContentType(CONTENT_TYPE_STRING);
        response.setCharacterEncoding(CHARACTER_ENCODING_STRING);
        request.getRequestDispatcher(MANAGEMENT_PAGE_ADDRESS_STRING).forward(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String sessionId = request.getSession().getId();
        String newGameParameter = request.getParameter(NEW_GAME_PARAMETER_STRING);
        String clueParameter = request.getParameter(CLUE_PARAMETER_STRING);
        String resumeGameParameter = request.getParameter(RESUME_GAME_PARAMETER_STRING);
        if (clueParameter != null) {
            addClue(request, response, clueParameter);
        } else if (newGameParameter != null) {
            gameCenter.removePreviousGame(sessionId);
            createNewGame(request, response, sessionId);
        } else if (resumeGameParameter != null) {
            resumeGame(request, response, sessionId);
        }
    }

    private void addClue(HttpServletRequest request, HttpServletResponse response, String clueParameter) throws ServletException, IOException {
        Cookie cookie = request.getCookies()[0];
        Game game = gameCenter.getGame(cookie.getValue());
        if (!cookie.getValue().equals(request.getSession().getId())) {
            game.setPlayerId(request.getSession().getId());
        }
        if (game != null) {
            sendResponse(request, response, game, game.getWordWithClue(clueParameter), null);
        } else {
            sendResponse(request, response, null, null, GAME_NOT_FOUND_STRING);
        }
    }

    public void createNewGame(HttpServletRequest request, HttpServletResponse response, String sessionId) throws IOException, ServletException {
        String obscuredWord = gameCenter.startNewGame(sessionId);
        Game game = gameCenter.getGame(sessionId);
        sendResponse(request, response, game, obscuredWord, NEW_GAME_STRING);
    }

    private void resumeGame(HttpServletRequest request, HttpServletResponse response, String sessionId) throws ServletException, IOException {
        Cookie cookie = request.getCookies()[0];
        Game ongoingGame = gameCenter.getGame(cookie.getValue());
        if (ongoingGame != null) {
            sendResponse(request, response, ongoingGame, ongoingGame.getWordInProgressAsString(), GAME_RESUMED_STRING);
        } else {
            createNewGame(request, response, sessionId);
        }
    }

    private void sendResponse(HttpServletRequest request, HttpServletResponse response, Game game, String word, String log) throws ServletException, IOException {
        Cookie cookie = new Cookie(COOKIE_PARAM_STRING, request.getSession().getId());
        cookie.setMaxAge(1800);
        if (game != null) {
            request.setAttribute(WORD_STRING, word);
            request.setAttribute(COMPLETE_STRING, game.isWordGuessed());
            request.setAttribute(ERRORS_STRING, game.getErrorCount());
            gameCenter.checkGameState(cookie.getValue(), game);
        }
        request.setAttribute(LOG_STRING, log);
        gameCenter.saveGames();
        response.addCookie(cookie);
        response.setContentType(CONTENT_TYPE_STRING);
        response.setCharacterEncoding(CHARACTER_ENCODING_STRING);
        request.getRequestDispatcher(INDEX_PAGE_ADDRESS).forward(request, response);
    }

}
