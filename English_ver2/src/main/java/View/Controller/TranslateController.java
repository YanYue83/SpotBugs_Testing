package View.Controller;

import Service.GoogleTranslate;
import Service.Language;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

import java.io.IOException;
import java.util.List;

public class TranslateController {
    @FXML
    private Button switchToggle;
    @FXML
    private TextArea sourceTextArea;

    @FXML
    private ComboBox<Language> sourceLanguageComboBox;

    @FXML
    private ComboBox<Language> targetLanguageComboBox;

    @FXML
    private TextArea resultArea;


    @FXML
    private void initialize() {
        List<Language> languages = Language.getDefaultLanguages();

        sourceLanguageComboBox.getItems().addAll(languages);
        targetLanguageComboBox.getItems().addAll(languages);

        // Set default selections
        sourceLanguageComboBox.setValue(languages.get(0));
        targetLanguageComboBox.setValue(languages.get(1));
    }

    @FXML
    private void translateText() {
        String sourceText = sourceTextArea.getText();
        Language sourceLanguage = sourceLanguageComboBox.getValue();
        Language targetLanguage = targetLanguageComboBox.getValue();

        try {
            String translatedText = GoogleTranslate.translateMultipleSentences(sourceLanguage.getCode(), targetLanguage.getCode(), sourceText);
            if (translatedText != null) {
                resultArea.setText(translatedText);
            } else {
                resultArea.setText("Translation failed.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            resultArea.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    private void handleOnClickSwitchToggle() {
        sourceTextArea.clear();
        resultArea.clear();
        Language temp = sourceLanguageComboBox.getValue();
        sourceLanguageComboBox.setValue(targetLanguageComboBox.getValue());
        targetLanguageComboBox.setValue(temp);
    }

}
