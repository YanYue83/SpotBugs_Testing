package Service;

import java.util.Arrays;
import java.util.List;

public class Language {
    private final String code;
    private final String name;

    public Language(String code, String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * Tạo 1 list ngôn ngữ.
     */
    public static List<Language> getDefaultLanguages() {
        return Arrays.asList(
                new Language("en", "English"),
                new Language("vi", "Vietnamese"),
                new Language("fr", "French"),
                new Language("es", "Spanish"),
                new Language("de", "German"),
                new Language("zh", "Chinese"),
                new Language("ja", "Japanese"),
                new Language("ko", "Korean"),
                new Language("ru", "Russian"),
                new Language("ar", "Arabic"),
                new Language("it", "Italian"),
                new Language("pt", "Portuguese"),
                new Language("nl", "Dutch"),
                new Language("hi", "Hindi"),
                new Language("tr", "Turkish"),
                new Language("th", "Thai")
        );
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
