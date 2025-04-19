package View.Controller;

import Alert.Alerts;
import DictionaryCommanLine.Dictionary;
import DictionaryCommanLine.DictionaryManagement;
import DictionaryCommanLine.Word;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class AddWordController implements Initializable {

    private final String path = "src/main/resources/Data/dictionaries.txt";
    private final Dictionary dictionary = new Dictionary();
    private final DictionaryManagement dictionaryManagement = new DictionaryManagement();
    private final Alerts alerts = new Alerts();
    @FXML
    private Button addBtn;
    @FXML
    private TextField wordTargetInput;
    @FXML
    private TextArea explanationInput;
    @FXML
    private Pane successAlert;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dictionaryManagement.insertFromFile(dictionary, path);
        if (explanationInput.getText().isEmpty() || wordTargetInput.getText().isEmpty()) addBtn.setDisable(true);

        wordTargetInput.setOnKeyTyped(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                addBtn.setDisable(explanationInput.getText().isEmpty() || wordTargetInput.getText().isEmpty());
            }
        });

        explanationInput.setOnKeyTyped(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                addBtn.setDisable(explanationInput.getText().isEmpty() || wordTargetInput.getText().isEmpty());
            }
        });

        successAlert.setVisible(false);
    }

    @FXML
    private void handleClickAddBtn() {
        Alert alertConfirmation = alerts.alertConfirmation("Add word", "Bạn chắc chắn muốn thêm từ này?");
        Optional<ButtonType> option = alertConfirmation.showAndWait();
        String englishWord = wordTargetInput.getText().trim();
        String meaning = explanationInput.getText().trim();

        if (option.get() == ButtonType.OK) {
            Word word = new Word(englishWord, meaning);
            if (dictionary.contains(word)) {
                int indexOfWord = dictionaryManagement.dictionaryLookUp(dictionary, englishWord);
                Alert selectionAlert = alerts.alertConfirmation("This word already exists", "Từ này đã tồn tại.\nThay thế hoặc bổ sung nghĩa vừa nhập cho nghĩa cũ.");
                selectionAlert.getButtonTypes().clear();
                ButtonType replaceBtn = new ButtonType("Thay thế");
                ButtonType insertBtn = new ButtonType("Bổ sung");
                selectionAlert.getButtonTypes().addAll(replaceBtn, insertBtn, ButtonType.CANCEL);
                Optional<ButtonType> selection = selectionAlert.showAndWait();

                if (selection.get() == replaceBtn) {
                    dictionary.get(indexOfWord).setWord_explain(meaning);
                    dictionaryManagement.exportToFile(dictionary, path);
                    showSuccessAlert();
                }
                if (selection.get() == insertBtn) {
                    String oldMeaning = dictionary.get(indexOfWord).getWord_explain();
                    dictionary.get(indexOfWord).setWord_explain(oldMeaning + "\n- " + meaning);
                    dictionaryManagement.exportToFile(dictionary, path);
                    showSuccessAlert();
                }
                if (selection.get() == ButtonType.CANCEL)
                    alerts.showAlertInfo("Information", "Thay đổi không được công nhận.");
            } else {
                dictionary.add(word);
                dictionaryManagement.addWord(word, path);
                dictionaryManagement.exportToFile(dictionary, path);
                showSuccessAlert();
            }
            addBtn.setDisable(true);
            resetInput();
        } else if (option.get() == ButtonType.CANCEL) {
            alerts.showAlertInfo("Information", "Thay đổi không được công nhận.");
            resetInput();
        }
    }

    private void resetInput() {
        wordTargetInput.setText("");
        explanationInput.setText("");
    }

    private void showSuccessAlert() {
        successAlert.setVisible(true);
        dictionaryManagement.setTimeout(() -> successAlert.setVisible(false), 2000);
    }
}


