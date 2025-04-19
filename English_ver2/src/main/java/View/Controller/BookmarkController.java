package View.Controller;

import Alert.Alerts;
import DictionaryCommanLine.Dictionary;
import DictionaryCommanLine.DictionaryManagement;
import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BookmarkController implements Initializable {
    private final String path = "src/main/resources/Data/bookmark.txt";
    private final Dictionary dictionary = new Dictionary();
    private final DictionaryManagement dictionaryManagement = new DictionaryManagement();
    private final Alerts alerts = new Alerts();
    ObservableList<String> list = FXCollections.observableArrayList();
    private int indexOfSelectedWord;
    @FXML
    private AnchorPane BPane;
    @FXML
    private TextArea explanation;
    @FXML
    private Pane headerOfExplanation;
    @FXML
    private ListView<String> listResults;
    @FXML
    private Label proun;
    @FXML
    private Button volumeBtn;
    @FXML
    private Label wordSearch;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dictionaryManagement.insertFromFile(dictionary, path);
        dictionaryManagement.setTrie(dictionary);
        setList();
    }

    @FXML
    protected void handleClickDeleteBtn(ActionEvent event) {
        Alert alertWarning = alerts.alertWarning("Delete", "Bạn chắc chắn muốn xóa từ này khỏi danh sách yêu thích?");
        alertWarning.getButtonTypes().add(ButtonType.CANCEL);
        Optional<ButtonType> option = alertWarning.showAndWait();
        if (option.get() == ButtonType.OK) {
            dictionaryManagement.deleteWord(dictionary, indexOfSelectedWord, path);
            refreshAfterDeleting();
            alerts.showAlertInfo("Information", "Xóa thành công!");
        } else alerts.showAlertInfo("Information", "Thay đổi không được công nhận!");
    }

    private void refreshAfterDeleting() {
        for (int i = 0; i < list.size(); i++)
            if (list.get(i).equals(wordSearch.getText())) {
                list.remove(i);
                break;
            }
        listResults.setItems(list);
        headerOfExplanation.setVisible(false);
        proun.setVisible(false);
        explanation.setVisible(false);
    }

    @FXML
    void handleClickSoundBtn(ActionEvent event) {
        System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
        Voice voice = VoiceManager.getInstance().getVoice("kevin");
        if (voice != null) {
            voice.allocate();
            voice.speak(dictionary.get(indexOfSelectedWord).getWord_target());
        } else throw new IllegalStateException("Cannot find voice: kevin");
    }

    @FXML
    void handleClickReturnBtn(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/DictionaryUI.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            BPane.getChildren().setAll(root);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    void handleMouseClickAWord(MouseEvent event) {
        String selectedWord = listResults.getSelectionModel().getSelectedItem();
        if (selectedWord != null) {
            indexOfSelectedWord = dictionaryManagement.dictionaryLookUp(dictionary, selectedWord);
            if (indexOfSelectedWord == -1) return;
            wordSearch.setText(dictionary.get(indexOfSelectedWord).getWord_target());
            //tách lấy phát âm
            String meaning = dictionary.get(indexOfSelectedWord).getWord_explain();
            String relax = meaning;
            proun.setText("");
            Pattern pattern = Pattern.compile("/([^/]+)");
            Matcher matcher = pattern.matcher(meaning);
            if (matcher.find()) {
                String contentBetweenSlashes = matcher.group(1);
                proun.setText("/" + contentBetweenSlashes + "/");
                relax = meaning.substring(meaning.indexOf("\n") + 1);
            }
            explanation.setText(relax);

            headerOfExplanation.setVisible(true);
            explanation.setVisible(true);
            explanation.setEditable(false);
        }
    }

    private void setList() {
        if (dictionary.isEmpty()) {
            // Nếu từ điển trống, xóa danh sách và không hiển thị gì cả trên listResults
            list.clear();
            listResults.setItems(list);

            wordSearch.setText("");
            proun.setText("");
            explanation.setText("");
            explanation.setEditable(false);
            headerOfExplanation.setVisible(false);
        } else {
            list.clear();
            for (int i = 0; i < dictionary.size(); i++) list.add(dictionary.get(i).getWord_target());
            listResults.setItems(list);
            wordSearch.setText(dictionary.get(0).getWord_target());
            String meaning = dictionary.get(0).getWord_explain();
            String relax = meaning;
            Pattern pattern = Pattern.compile("/([^/]+)");
            proun.setText("");
            Matcher matcher = pattern.matcher(meaning);
            if (matcher.find()) {
                String contentBetweenSlashes = matcher.group(1);
                proun.setText("/" + contentBetweenSlashes + "/");
                relax = meaning.substring(meaning.indexOf("\n") + 1);
            }
            explanation.setText(relax);
            explanation.setEditable(false);
        }
    }
}
