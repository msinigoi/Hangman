package com.sinigoi.apputils;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

public class GameCenterTest {

    private GameCenter gameCenter;

    @Before
    public void init() {
        gameCenter = new GameCenter();
    }

    @Test
    public void getGameFromListTest() {
        gameCenter.startNewGame("newGame");
        gameCenter.startNewGame("newGame2");

        assertEquals("newGame", gameCenter.getGame("newGame").getPlayerId());
        assertEquals("newGame2", gameCenter.getGame("newGame2").getPlayerId());
        assertNull(gameCenter.getGame("nullGame"));
        assertEquals(2, gameCenter.getOngoingGamesSize());
    }

    @Test
    public void startNewGameTest() throws IOException {
        String obscuredWord = gameCenter.startNewGame("id");
        String[] obscuredWordAsArray = obscuredWord.split("(?!^)");

        for (String letterToCheck : obscuredWordAsArray) {
            assertEquals("_", letterToCheck);
        }
    }

    @Test
    public void removeOngoingGameTest() throws IOException {
        gameCenter.startNewGame("id");
        gameCenter.removePreviousGame("id");

        assertEquals(0, gameCenter.getOngoingGamesSize());
    }

    @Test
    public void isGameOverTest() {
        GameCenter gameCenter = new GameCenter();
        gameCenter.startNewGame("id1");
        Game game = gameCenter.getGame("id1");
        game.getWordWithClue("f");
        game.getWordWithClue("f");
        game.getWordWithClue("f");
        game.getWordWithClue("f");
        game.getWordWithClue("f");
        game.getWordWithClue("f");
        game.getWordWithClue("f");
        game.getWordWithClue("f");

        assertEquals(8, game.getErrorCount());
        assertTrue(gameCenter.isGameOver("id1"));
        assertEquals(1, gameCenter.getOngoingGamesSize());
    }

    @Test
    public void checkGameStateGameOverTest() throws IOException {
        GameCenter gameCenter = new GameCenter();
        gameCenter.startNewGame("id");
        Game game = gameCenter.getGame("id");
        game.getWordWithClue("f");
        game.getWordWithClue("f");
        game.getWordWithClue("f");
        game.getWordWithClue("f");
        game.getWordWithClue("f");
        game.getWordWithClue("f");
        game.getWordWithClue("f");
        game.getWordWithClue("f");
        gameCenter.checkGameState("id", game);

        assertEquals(0, gameCenter.getOngoingGamesSize());
    }

    @Test
    public void checkGameStatePlayingTest() throws IOException {
        GameCenter gameCenter = new GameCenter();
        gameCenter.startNewGame("id");
        Game game = gameCenter.getGame("id");
        game.getWordWithClue("f");
        game.getWordWithClue("f");
        game.getWordWithClue("f");
        game.getWordWithClue("f");
        game.getWordWithClue("f");
        game.getWordWithClue("f");
        game.getWordWithClue("f");
        gameCenter.checkGameState("id", game);

        assertEquals(1, gameCenter.getOngoingGamesSize());
        assertFalse(gameCenter.isGameOver("id"));
    }

    @Test
    public void removeOlderGamesTest() {
        GameCenterStub gameCenterStub = new GameCenterStub();
        gameCenterStub.startNewGamePast("past");
        gameCenterStub.startNewGameFuture("future");

        gameCenterStub.removeOlderGames();

        assertEquals(1, gameCenterStub.getOngoingGamesSize());
    }


    private class GameCenterStub extends GameCenter {

        private List<Game> ongoingGames = new ArrayList<>();
        private List<String> animalVocabulary = new ArrayList<String>() {{
            add("cat");
        }};

        @Override
        public int getOngoingGamesSize() {
            return ongoingGames.size();
        }

        @Override
        public void removeOlderGames() {
            ongoingGames.removeIf(game -> game.getDate() - new Date().getTime() >= 30 * 60 * 1000);
        }

        String startNewGamePast(String sessionId) {
            Game game = new Game();
            game.setPlayerId(sessionId);
            game.setDate(new Date(0).getTime());
            game.setVocabulary(animalVocabulary);
            game.createWord();
            ongoingGames.add(game);
            return game.getObscuredWord();
        }

        String startNewGameFuture(String sessionId) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.add(Calendar.YEAR, 1000);

            Game game = new Game();
            game.setPlayerId(sessionId);
            game.setDate(calendar.getTime().getTime());
            game.setVocabulary(animalVocabulary);
            game.createWord();
            ongoingGames.add(game);
            return game.getObscuredWord();
        }
    }
}