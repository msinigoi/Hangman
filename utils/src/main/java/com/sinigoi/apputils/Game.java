package com.sinigoi.apputils;

import java.io.Serializable;
import java.util.*;

public class Game implements Serializable {

    public static final long serialVersionUID = -3961145327358284276L;
    private static final String SPLIT_REGEX = "(?!^)";
    private static final String UNDERSCORE_STRING = "_";
    private List<String> vocabulary;
    private String playerId;
    private String[] currentWord;
    private String[] wordInProgress;
    private int errorCount = 0;
    private long date;

    void setVocabulary(List<String> vocabulary) {
        this.vocabulary = vocabulary;
    }

    List<String> getVocabulary() {
        return vocabulary;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    String getPlayerId() {
        return playerId;
    }

    void setCurrentWord(String[] currentWord) {
        this.currentWord = currentWord;
    }

    String[] getCurrentWord() {
        return currentWord;
    }

    public int getErrorCount() {
        return errorCount;
    }

    long getDate() {
        return date;
    }

    void setDate(long date) {
        this.date = date;
    }

    String[] createWord() {
        Collections.shuffle(vocabulary, new Random(System.nanoTime()));
        currentWord = vocabulary.get(0).split(SPLIT_REGEX);
        return currentWord;
    }

    String getObscuredWord() {
        String[] obscuredWord = new String[currentWord.length];
        for (int i = 0; i < obscuredWord.length; i++) {
            obscuredWord[i] = UNDERSCORE_STRING;
        }
        wordInProgress = obscuredWord;
        return getWordAsString(obscuredWord);
    }

    boolean isClueContained(String clue) {
        for (String letter : currentWord) {
            if (letter.equalsIgnoreCase(clue)) {
                return true;
            }
        }
        return false;
    }

    public String getWordWithClue(String clue) {
        if (isClueContained(clue)) {
            return addClueToWord(clue);
        } else {
            errorCount++;
            return getWordAsString(wordInProgress);
        }
    }

    private String addClueToWord(String clue) {
        for (int i = 0; i < currentWord.length; i++) {
            if (currentWord[i].equals(clue)) {
                wordInProgress[i] = clue;
            }
        }
        return getWordAsString(wordInProgress);
    }

    private String getWordAsString(String[] wordAsArray) {
        String wordAsString = "";
        for (String letter : wordAsArray) {
            wordAsString += letter;
        }
        return wordAsString;
    }

    public boolean isWordGuessed() {
        return Arrays.equals(wordInProgress, currentWord);
    }

    public String getWordInProgressAsString() {
        return getWordAsString(wordInProgress);
    }

}