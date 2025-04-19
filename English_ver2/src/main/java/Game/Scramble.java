package Game;

import DictionaryCommanLine.Dictionary;

import java.util.ArrayList;
import java.util.Collections;

public class Scramble extends Game {
    private ArrayList<Character> correctAnswer;
    private ArrayList<Character> answers;

    public Scramble(Dictionary wordList) {
        super(wordList);
    }

    public void resetGame() {
        this.setCode(getRandom());
        correctAnswer = new ArrayList<>();
        answers = new ArrayList<>();
        String cleanCode = this.getCode().getWord_target().toLowerCase();
        // Scramble the word
        for (Character c : cleanCode.toCharArray()) {
            this.correctAnswer.add(c);
        }
        this.answers.addAll(correctAnswer);
        Collections.shuffle(this.answers);
    }

    @Override
    public boolean isCorrectGuess(ArrayList<Character> guess) {
        for (int i = 0; i < this.correctAnswer.size(); i++) {
            if (this.correctAnswer.get(i) != guess.get(i)) {
                return false;
            }
        }
        return true;
    }

    public ArrayList<Character> getAnswers() {
        return this.answers;
    }
}