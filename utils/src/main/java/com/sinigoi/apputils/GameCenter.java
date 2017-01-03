package com.sinigoi.apputils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Semaphore;

public class GameCenter {

    private static final String FILE_NAME = "games.json";
    private Semaphore semaphore = new Semaphore(1);
    private List<Game> ongoingGames = new ArrayList<>();
    private Type listType = new TypeToken<ArrayList<Game>>() {
    }.getType();
    private List<String> animalVocabulary = new ArrayList<String>() {{
        add("cat");
        add("doge");
        add("tiger");
        add("vampire");
        add("emu");
    }};

    public String startNewGame(String sessionId) {
        Game game = new Game();
        game.setPlayerId(sessionId);
        game.setDate(new Date().getTime());
        game.setVocabulary(animalVocabulary);
        game.createWord();
        ongoingGames.add(game);
        saveGames();
        return game.getObscuredWord();
    }

    public void removePreviousGame(String sessionId) {
        Game previousGame = getGame(sessionId);
        if (previousGame != null) {
            ongoingGames.remove(previousGame);
        }
        saveGames();
    }

    public Game getGame(String sessionId) {
        for (Game game : ongoingGames) {
            if (sessionId.equals(game.getPlayerId())) {
                return game;
            }
        }
        return null;
    }

    boolean isGameOver(String sessionId) {
        return getGame(sessionId) != null && getGame(sessionId).getErrorCount() > 7;
    }

    public void checkGameState(String sessionId, Game game) throws IOException {
        if (game.isWordGuessed() || isGameOver(sessionId)) {
            removePreviousGame(sessionId);
        }
    }

    public void removeOlderGames() {
        ongoingGames.removeIf(game -> game.getDate() - new Date().getTime() >= 30 * 60 * 1000);
    }

    public void saveGames() {
        try {
            semaphore.acquire();
            String jsonArray = new Gson().toJson(ongoingGames, listType);
            Writer writer = new FileWriter(FILE_NAME);
            writer.write(jsonArray);
            writer.close();
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        } finally {
            semaphore.release();
        }
    }

    public void loadGames() throws IOException, ClassNotFoundException {
        File file = new File(FILE_NAME);
        if (file.exists()) {
            BufferedReader br = new BufferedReader(new FileReader(FILE_NAME));
            ongoingGames = new Gson().fromJson(br, listType);
        }
    }

    public List<Game> getOngoingGames() {
        return ongoingGames;
    }

    public int getOngoingGamesSize() {
        return ongoingGames.size();
    }
}
