package DictionaryCommanLine;


import java.util.Objects;

public class Word {
    /**
     * Class tu.
     */
    private String word_target;
    private String word_explain;

    /**
     * Constructor cua word.
     */
    public Word(String word_target, String word_explain) {
        this.word_target = word_target;
        this.word_explain = word_explain;
    }

    /**
     * Ham tao mac dinh.
     */
    public Word() {
        this.word_explain = "";
        this.word_target = "";
    }

    public String getWord_target() {
        return this.word_target;
    }

    /**
     * Getter, setter.
     */
    public void setWord_target(String word_target) {
        this.word_target = word_target;
    }

    public String getWord_explain() {
        return this.word_explain;
    }

    public void setWord_explain(String word_explain) {
        this.word_explain = word_explain;
    }

    /**
     * Ham kiem tra doi tuong co phai word.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Word newWord)) return false;

        return Objects.equals(word_target, newWord.word_target);
    }

    @Override
    public int hashCode() {
        return Objects.hash(word_target, word_explain);
    }

    @Override
    public String toString() {
        return "Word: " + word_target + " | Word meaning: " + word_explain;
    }
}
