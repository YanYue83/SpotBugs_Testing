package View.Controller;

import Alert.Alerts;
import DictionaryCommanLine.Dictionary;
import DictionaryCommanLine.DictionaryManagement;
import DictionaryCommanLine.Word;
import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

public class DictionaryController implements Initializable {
    private final String path = "src/main/resources/Data/dictionaries.txt";
    ObservableList<String> list = FXCollections.observableArrayList();
    Dictionary lovedic = new Dictionary();
    private final Dictionary dictionary = new Dictionary();
    private final DictionaryManagement dictionaryManagement = new DictionaryManagement();
    private final Alerts alerts = new Alerts();
    private int indexOfSelectedWord;
    private int firstIndexOfListFound = 0;
    @FXML
    private AnchorPane dicPane;
    @FXML
    private Button cancelBtn;
    @FXML
    private TextArea explanation;
    @FXML
    private Pane headerOfExplanation;
    @FXML
    private ListView<String> listResults;
    @FXML
    private Label notfoundAlert;
    @FXML
    private Button saveBtn;
    @FXML
    private TextField searchTerm;
    @FXML
    private Button volumeBtn;
    @FXML
    private Button bookmarkBtn;
    @FXML
    private Label wordSearch, headerList, proun;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        dictionaryManagement.insertFromFile(dictionary, path);
        dictionaryManagement.insertFromFile(lovedic, "src/main/resources/Data/bookmark.txt");

        dictionaryManagement.setTrie(dictionary);
        setListDefault(0);

        searchTerm.setOnKeyTyped(keyEvent -> {
            if (searchTerm.getText().isEmpty()) {
                cancelBtn.setVisible(false);
                setListDefault(0);
            } else {
                cancelBtn.setVisible(true);
                handleOnKeyTyped();
            }
        });

        cancelBtn.setOnAction(event -> {
            searchTerm.clear();
            notfoundAlert.setVisible(false);
            cancelBtn.setVisible(false);
            setListDefault(0);
        });

        explanation.setEditable(false);
        saveBtn.setVisible(false);
        cancelBtn.setVisible(false);
        notfoundAlert.setVisible(false);
    }

    @FXML
    private void handleOnKeyTyped() {
        list.clear();
        String searchKey = searchTerm.getText().trim();
        list = dictionaryManagement.searchWord(dictionary, searchKey);
        if (list.isEmpty()) {
            notfoundAlert.setVisible(true);
            setListDefault(firstIndexOfListFound);
        } else {
            notfoundAlert.setVisible(false);
            headerList.setText("Kết quả");
            listResults.setItems(list);
            firstIndexOfListFound = dictionaryManagement.dictionaryLookUp(dictionary, list.get(0));
        }
    }

    @FXML
    private void handleMouseClickAWord(MouseEvent arg0) {
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
            saveBtn.setVisible(false);
        }
    }

    @FXML
    private void handleClickEditBtn() {
        explanation.setEditable(true);
        saveBtn.setVisible(true);
        alerts.showAlertInfo("Information", "Bạn đã cho phép chỉnh sửa nghĩa từ này!");
    }

    @FXML
    private void handleClickSoundBtn() {
        System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
        Voice voice = VoiceManager.getInstance().getVoice("kevin");
        if (voice != null) {
            voice.allocate();
            voice.speak(dictionary.get(indexOfSelectedWord).getWord_target());
        } else throw new IllegalStateException("Cannot find voice: kevin");
    }

    @FXML
    private void handleClickSaveBtn() {
        Alert alertConfirmation = alerts.alertConfirmation("Update", "Bạn chắc chắn muốn cập nhật nghĩa từ này ?");
        Optional<ButtonType> option = alertConfirmation.showAndWait();
        if (option.get() == ButtonType.OK) {
            dictionaryManagement.updateWord(dictionary, indexOfSelectedWord, explanation.getText(), path);
            alerts.showAlertInfo("Information", "Cập nhập thành công!");
        } else alerts.showAlertInfo("Information", "Thay đổi không được công nhận!");
        saveBtn.setVisible(false);
        explanation.setEditable(false);
    }

    @FXML
    private void handleClickDeleteBtn() {
        Alert alertWarning = alerts.alertWarning("Delete", "Bạn chắc chắn muốn xóa từ này?");
        alertWarning.getButtonTypes().add(ButtonType.CANCEL);
        Optional<ButtonType> option = alertWarning.showAndWait();
        if (option.get() == ButtonType.OK) {
            dictionaryManagement.deleteWord(dictionary, indexOfSelectedWord, path);
            refreshAfterDeleting();
            alerts.showAlertInfo("Information", "Xóa thành công!");
        } else alerts.showAlertInfo("Information", "Thay đổi không được công nhận!");
    }

    @FXML
    private void handleClickBookmarkBtn() {
        Word word;
        if (proun.getText().equals("")) {
            word = new Word(wordSearch.getText(), explanation.getText());
        } else word = new Word(wordSearch.getText(), (proun.getText()) + "\n" + explanation.getText());
        if (lovedic.contains(word)) {
            alerts.showAlertInfo("Information", "Từ đã có trong danh sách yêu thích!");
        } else {
            alerts.showAlertInfo("Information", "Đã thêm từ vào danh sách yêu thích!");
            lovedic.add(word);
            dictionaryManagement.exportToFile(lovedic, "src/main/resources/Data/bookmark.txt");
        }
    }

    @FXML
    private void handleClickStar() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/BookmarkUI.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            dicPane.getChildren().setAll(root);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void refreshAfterDeleting() {
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

    private void setListDefault(int index) {
        if (index == 0) headerList.setText("15 từ đầu tiên");
        else headerList.setText("Kết quả liên quan");
        list.clear();
        for (int i = index; i < index + 15; i++) list.add(dictionary.get(i).getWord_target());
        listResults.setItems(list);
        wordSearch.setText(dictionary.get(index).getWord_target());
        String meaning = dictionary.get(index).getWord_explain();
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

    }

}