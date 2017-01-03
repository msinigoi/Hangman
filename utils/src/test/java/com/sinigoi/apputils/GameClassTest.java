package com.sinigoi.apputils;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

public class GameClassTest {

    private Game game;
    private List<String> vocabulary = new ArrayList<String>() {{
        add("cat");
        add("dog");
        add("lion");
        add("vampire");
        add("emu");
    }};

    @Before
    public void init() {
        game = new Game();
    }

    @Test
    public void gameSetUpTest() {
        game.setVocabulary(vocabulary);
        game.setPlayerId("id");

        assertEquals(5, game.getVocabulary().size());
        assertEquals("id", game.getPlayerId());
    }

    @Test(expected = NullPointerException.class)
    public void gameSetUpNullVocabularyTest() {
        game.setVocabulary(null);

        assertEquals(0, game.getVocabulary().size());
    }

    @Test
    public void getWordTest() {
        game.setVocabulary(vocabulary);
        String[] word = game.createWord();
        String wordAsString = "";
        for (String letter : word) {
            wordAsString += letter;
        }

        assertTrue(game.getVocabulary().contains(wordAsString));
        assertEquals(word.length, game.getCurrentWord().length);
    }

    @Test(expected = NullPointerException.class)
    public void getWordFromNullArrayTest() {
        game.setVocabulary(null);

        assertTrue(game.getVocabulary().contains(Arrays.toString(game.createWord())));
    }

    @Test
    public void getObscuredWord() {
        game.setVocabulary(vocabulary);
        String[] word = game.createWord();

        assertEquals(word.length, game.getObscuredWord().length());
        assertEquals("_", game.getObscuredWord().substring(0, 1));
    }

    @Test
    public void isLetterContainedTest() {
        game.setCurrentWord(new String[]{"c", "a", "t"});

        assertTrue(game.isClueContained("c"));
        assertTrue(game.isClueContained("A"));
        assertFalse(game.isClueContained("f"));
    }

    @Test
    public void getFilledWordTest() {
        game.setCurrentWord(new String[]{"c", "a", "t"});
        game.getObscuredWord();

        assertEquals("_", game.getWordWithClue("a").split("(?!^)")[0]);
        assertEquals("a", game.getWordWithClue("A").split("(?!^)")[1]);
        assertEquals("_", game.getWordWithClue("a").split("(?!^)")[2]);
    }

    @Test
    public void getFilledWordWithManyLettersAtOnceTest() {
        game.setCurrentWord(new String[]{"h", "y", "p", "p", "o", "p", "o", "t", "a", "m", "u", "s"});
        game.getObscuredWord();

        assertEquals("_", game.getWordWithClue("p").split("(?!^)")[0]);
        assertEquals("_", game.getWordWithClue("p").split("(?!^)")[1]);
        assertEquals("p", game.getWordWithClue("p").split("(?!^)")[2]);
        assertEquals("p", game.getWordWithClue("p").split("(?!^)")[3]);
        assertEquals("_", game.getWordWithClue("p").split("(?!^)")[4]);
        assertEquals("p", game.getWordWithClue("p").split("(?!^)")[5]);
        assertEquals("_", game.getWordWithClue("p").split("(?!^)")[6]);
        assertEquals("_", game.getWordWithClue("p").split("(?!^)")[7]);
        assertEquals("_", game.getWordWithClue("p").split("(?!^)")[8]);
        assertEquals("_", game.getWordWithClue("p").split("(?!^)")[9]);
        assertEquals("_", game.getWordWithClue("p").split("(?!^)")[10]);
    }

    @Test
    public void getErrorCountTest() {
        game.setCurrentWord(new String[]{"h", "y", "p", "p", "o", "p", "o", "t", "a", "m", "u", "s"});
        game.getObscuredWord();
        game.getWordWithClue("f");
        game.getWordWithClue("f");
        game.getWordWithClue("f");
        game.getWordWithClue("f");

        assertEquals(4, game.getErrorCount());
    }

    @Test
    public void isWordGuessedTest() {
        game.setCurrentWord(new String[]{"c", "a", "t"});
        game.getObscuredWord();
        game.getWordWithClue("c");
        game.getWordWithClue("a");
        game.getWordWithClue("t");

        assertTrue(game.isWordGuessed());
    }

    @Test
    public void isWordGuessed2Test() {
        game.setCurrentWord(new String[]{"e", "l", "e", "p", "h", "a", "n", "t"});
        game.getObscuredWord();
        game.getWordWithClue("e");
        game.getWordWithClue("l");
        game.getWordWithClue("p");
        game.getWordWithClue("h");
        game.getWordWithClue("a");
        game.getWordWithClue("n");
        game.getWordWithClue("t");

        assertTrue(game.isWordGuessed());
    }

    @Test
    public void isWordNotGuessedTest() {
        game.setCurrentWord(new String[]{"c", "a", "t"});
        game.getObscuredWord();
        game.getWordWithClue("c");
        game.getWordWithClue("a");

        assertFalse(game.isWordGuessed());
    }

    @Test
    public void setAndGetTimeOfGameCreationTest() {
        Date date = new Date();
        game.setDate(date.getTime());

        assertEquals(date.getTime(), game.getDate());
    }

}