package View.Controller;

import DictionaryCommanLine.Dictionary;
import Game.Scramble;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.shape.SVGPath;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ScrambleController implements Initializable {
    private final double questionDuration = 10;
    private Scramble game;
    private final Dictionary wordList = new Dictionary();
    private ArrayList<Character> userGuess;
    private int answerCount;
    private float score;
    private int live;
    private int wordCount;
    private PauseTransition timer;
    //Hbox lưu đáp án người chơi chọn.
    @FXML
    private HBox answerBox;
    //Ô chữ cho người chơi chọn.
    @FXML
    private HBox guessBox;
    @FXML
    private HBox liveLeft;

    @FXML
    private Pane cluePane, answerPane, resultPane;
    @FXML
    private Label labelScore, countLB, scoreLB;

    @FXML
    private Button btnGameRestart, exitBtn;
    //Gợi ý - Nghĩa từ.
    @FXML
    private Label clue = new Label();
    //Kết quả
    @FXML
    private Label result = new Label();
    @FXML
    private Label currentScore = new Label();
    //Thời gian
    @FXML
    private ProgressBar showTimer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        exitBtn.setOnAction(event -> {
            closeCurrentWindow();
        });
        game = new Scramble(wordList);
        //Label.
        clue.setAlignment(Pos.CENTER);
        result.setVisible(false);
        resultPane.setVisible(false);
        result.setAlignment(Pos.CENTER);
        //HBox.
        hboxStyle(answerBox, 3);
        hboxStyle(guessBox, 3);
        hboxStyle(liveLeft, 30);
        btnGameRestart.setVisible(false);
        btnGameRestart.setOnAction(event -> start());
        //Misc.
        start();
    }


    private Button createStyledButton(String buttonText, String type) {
        Button button = new Button(buttonText);
        //Style.
        if (type.equals("one"))
            button.getStyleClass().add("custom-button1");
        else {
            button.getStyleClass().add("custom-button2");
        }
        return button;
    }

    private void hboxStyle(HBox demo, int spacing) {
        demo.setVisible(false);
        demo.setAlignment(Pos.CENTER);
        demo.setSpacing(spacing);
    }

    private SVGPath createHeart() {
        SVGPath heartShape = new SVGPath();
        heartShape.setContent("M23.6,0c-3.4,0-6.3,2.7-7.6,5.6C14.7,2.7,11.8,0,8.4,0C3.8,0,0,3.8,0,8.4c0,9.4,9.5,11.9,16,21.2\n" +
                "c6.1-9.3,16-12.1,16-21.2C32,3.8,28.2,0,23.6,0z");
        heartShape.setStyle("-fx-border-color: yellow");
        heartShape.setScaleX(1);
        heartShape.setScaleY(1);
        return heartShape;
    }

    private void startTimeCounter(PauseTransition timeCountdown) {
        PauseTransition time = timeCountdown;
        showTimer.progressProperty().bind(
                Bindings.createDoubleBinding(
                        () -> {
                            double currentTime = time.getCurrentTime().toSeconds();
                            double duration = time.getDuration().toSeconds();
                            return 1.0 - (currentTime / duration);
                        },
                        time.currentTimeProperty(), time.durationProperty()
                )
        );

        timer.playFromStart();
    }

    /**
     * Khởi động game.
     */
    @FXML
    private void start() {
        score = 0;
        wordCount = 0;
        live = 3;
        reset();
        liveLeft.getChildren().clear();
        btnGameRestart.setVisible(false);
        for (int i = 0; i < 3; i++) {
            SVGPath node = createHeart();
            liveLeft.getChildren().add(node);
            liveLeft.getChildren().get(i).setStyle("-fx-fill: yellow");
        }
        liveLeft.setVisible(true);
        currentScore.setVisible(true);
        String formatted = String.format("%.0f", score);
        currentScore.setText(formatted);
    }

    /**
     * Tái khởi động.
     */
    @FXML
    private void reset() {
        resultPane.setVisible(false);
        answerPane.setVisible(true);
        cluePane.setVisible(true);
        labelScore.setVisible(true);
        answerBox.setVisible(true);
        guessBox.setVisible(true);
        clue.setVisible(true);
        result.setVisible(false);
        answerCount = 0;
        timer = new PauseTransition(Duration.seconds(questionDuration));
        startTimeCounter(timer);
        game.resetGame();
        result.setVisible(false);
        showTimer.setVisible(true);
        int length = game.getAnswers().size();
        userGuess = new ArrayList<>();
        for (Character i : game.getAnswers()) {
            userGuess.add(null);
        }
        guessBox.getChildren().clear();
        answerBox.getChildren().clear();
        clue.setText(game.getCode().getWord_explain());
        for (int i = 0; i < game.getAnswers().size(); i++) {
            /**
             * Ô chữ của người chơi
             */
            String btnName = game.getAnswers().get(i).toString().toUpperCase();
            Button button = createStyledButton(btnName, "one");
            button.setId(Integer.toString(i));
            button.setUserData(game.getAnswers().get(i));
            button.setOnAction(this::handleGuess);
            guessBox.getChildren().add(button);
            /**
             * Ô trả lời.
             */
            btnName = "";
            Button savedbutton = createStyledButton(btnName, "two");
            savedbutton.setId(Integer.toString(i));
            savedbutton.setVisible(false);
            savedbutton.setOnAction(this::undoGuess);
            answerBox.getChildren().add(savedbutton);
        }
        timer.setOnFinished(event -> resolveGuess());
    }

    /**
     * Loại chữ ra khỏi đáp án.
     */
    @FXML
    private void undoGuess(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();
        guessBox.getChildren().forEach(node -> {
            if (node instanceof Button checkbutton) {
                if (checkbutton.getId() == clickedButton.getUserData()) {
                    checkbutton.setVisible(true);
                    for (int i = 0; i < userGuess.size(); i++) {
                        if (userGuess.get(i) == checkbutton.getUserData()) {
                            userGuess.set(i, null);
                            break;
                        }
                    }
                }
            }
        });
        clickedButton.setText("");
        clickedButton.setUserData("");
        clickedButton.setVisible(false);
        answerCount--;
        score -= (float) 50 / userGuess.size();
    }

    /**
     * Xử lý đáp án.
     */
    @FXML
    private void handleGuess(ActionEvent event) {
        /**
         * Nhận chữ được chọn.
         */
        Button clickedButton = (Button) event.getSource();
        Character check = (Character) clickedButton.getUserData();
        int pos = answerCount;
        for (int i = 0; i < userGuess.size(); i++) {
            if (userGuess.get(i) == null) {
                userGuess.set(i, check);
                pos = i;
                Button savedButton = (Button) answerBox.getChildren().get(pos);
                savedButton.setText(check.toString().toUpperCase());
                savedButton.setUserData(clickedButton.getId());
                savedButton.setVisible(true);
                break;
            }
        }
        clickedButton.setVisible(false);
        /**
         * Kiểm tra đáp án.
         */
        answerCount++;
        if ((answerCount == userGuess.size())) {
            resolveGuess();
        }
    }

    @FXML
    private void resolveGuess() {
        timer.stop();
        if (game.isCorrectGuess(userGuess)) {
            score += 100;
            wordCount++;
            String formattedScore = String.format("%.0f", score);
            currentScore.setText(formattedScore);
            result.setText("Đáp án chính xác!");

            // Hiển thị ô chữ màu xanh khi trả lời đúng
            for (Node node : answerBox.getChildren()) {
                if (node instanceof Button button) {
                    button.setStyle("-fx-background-color: lightgreen;");
                }
            }
        } else {
            String correct = game.getCode().getWord_target();
            result.setText("Đáp án đúng là: " + game.getCode().getWord_target().toUpperCase());
            live--;
            liveLeft.getChildren().get(live).setStyle("-fx-fill: black");

            // Hiển thị ô chữ màu đỏ khi trả lời sai
            for (Node node : answerBox.getChildren()) {
                if (node instanceof Button button) {
                    button.setStyle("-fx-background-color: lightcoral;");
                }
            }
        }
        result.setVisible(true);
        showTimer.setVisible(false);

        if (live == 0) {
            gameResult();
        } else if (live > 0) {
            // Tạo Timeline để giữ màu sắc trước khi reset
            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(2), event -> reset()));
            timeline.play();
        }
    }


    /**
     * Kết quả.
     */
    @FXML
    private void gameResult() {
        // Hiển thị đáp án trước khi ẩn các thành phần khác
        String correctAnswer = game.getCode().getWord_target().toUpperCase();
        result.setText("Đáp án đúng là: " + correctAnswer);
        result.setVisible(true);

        // Đợi 2 giây trước khi ẩn các thành phần khác và hiển thị kết quả cuối cùng
        PauseTransition pauseBeforeFinalResult = new PauseTransition(Duration.seconds(2));
        pauseBeforeFinalResult.setOnFinished(event -> {
            // Ẩn các thành phần khác
            answerPane.setVisible(false);
            cluePane.setVisible(false);
            liveLeft.setVisible(false);
            labelScore.setVisible(false);
            currentScore.setVisible(false);
            showTimer.setVisible(false);
            currentScore.setVisible(false);
            guessBox.setVisible(false);

            // Hiển thị kết quả cuối cùng
            showFinalResult();
        });
        pauseBeforeFinalResult.play();
    }

    private void showFinalResult() {
        String formattedScore = String.format("%.0f", score);
        countLB.setText(String.valueOf(wordCount));
        countLB.setAlignment(Pos.CENTER);

        scoreLB.setText(formattedScore);
        scoreLB.setAlignment(Pos.CENTER);

        resultPane.setVisible(true);
        result.setVisible(false);
        btnGameRestart.setVisible(true);

    }


    private void closeCurrentWindow() {
        Window window = exitBtn.getScene().getWindow();
        if (window instanceof Stage) {
            ((Stage) window).close();
        }
    }
}