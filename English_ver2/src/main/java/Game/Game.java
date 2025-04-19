package Game;

import DictionaryCommanLine.Dictionary;
import DictionaryCommanLine.DictionaryManagement;
import DictionaryCommanLine.Word;

import java.util.ArrayList;
import java.util.Random;

public abstract class Game {
    private final String path = "src/main/resources/Data/wordBank.txt";
    private final Dictionary wordList;
    private Word code = new Word();
    private final DictionaryManagement management = new DictionaryManagement();

    public Game(Dictionary wordList) {
        management.insertFromFile(wordList, path);
        this.wordList = wordList;
        resetGame();
    }

    public abstract void resetGame();

    public Word getRandom() {
        Random random = new Random();
        code = wordList.get(random.nextInt(wordList.size()));
        return code;
    }

    public abstract boolean isCorrectGuess(ArrayList<Character> guess);

    public Word getCode() {
        return this.code;
    }

    public void setCode(Word word) {
        this.code = word;
    }

    public Dictionary getWordList() {
        return wordList;
    }
}
