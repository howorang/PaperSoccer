import common.Field;
import common.Network;
import common.Player;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Created by Piotr Borczyk on 12.01.2017.
 */
public class PlayWindowController {

    @FXML
    private Canvas backgroundCanvas;

    @FXML
    private GridPane buttonPane;

    @FXML
    private Label playerLabel;

    @FXML
    private Label scoreLabel;

    private Button[][] buttons = new Button[9][13];

    private Button lastPressed = null;

    private Player boundPlayer = null;

    private Endpoint endpoint = Endpoint.getInstance();

    private Stage primaryStage;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void setBoundPlayer(Player boundPlayer) {
        this.boundPlayer = boundPlayer;
        playerLabel.setText(boundPlayer.name());
    }

    public void initialize() {

        drawBackground();
        updateScoreString();

        for(int y = 0; y < 13; y++) {

            if(y == 0 || y == 12) {
                for (int x = 3; x < 6; x++) {
                    createButton(x,y);
                }
                continue;
            }

            for (int x = 0; x < 9; x++) {
                createButton(x,y);
            }
        }

        //game init

        gameInit();
        updatePlayingField();
    }

    public void move(int x, int y, Player player, List<Network.SimpleField> availableFields) {
        Platform.runLater(() -> {
            clearEnabledFields();
            drawLine(lastPressed,buttons[x][y]);
            lastPressed = buttons[x][y];
            enableAvailableFields(availableFields);
            scoreLabel.setText("Teraz gra: " + (player == Player.ONE ? Player.TWO.name() : Player.ONE.name()));
        });
    }

    public void win(Player player) {
        Platform.runLater(() -> {
            scoreLabel.setText(player.name() + " wygrał");
            displayDialog();
        });

    }

    public void disconnect(){
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Przewciwnik rozłączył się");
            alert.setContentText("Wrócisz do lobby");
            alert.showAndWait();
            goBackToLobby();
        });
    }

    public void connectionLost(){
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Stracono połączenie z serwerem");
            alert.setContentText("Gra wyłączy się");
            alert.showAndWait();
            System.exit(1);
        });
    }

    private void displayDialog() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Gra zakończona");
        alert.setContentText("Wybierz:");

        ButtonType backToLobby = new ButtonType("Wróć do poczekalni");
        ButtonType disconnect = new ButtonType("Rozłącz");

        alert.getButtonTypes().setAll(backToLobby,disconnect);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == backToLobby) {
            goBackToLobby();
        } else {
            System.exit(0);
        }
    }

    private void goBackToLobby() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("login_window.fxml"));
            Parent root = fxmlLoader.load();
            LoginWindowController loginWindowController = fxmlLoader.getController();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            loginWindowController.setPrimaryStage(stage);
            stage.setScene(scene);
            stage.show();
            primaryStage.close();
            endpoint.registerInLobby();
            endpoint.setPlayWindowController(null);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void enableAvailableFields(List<Network.SimpleField> availableFields) {
        for(Network.SimpleField field : availableFields) {
            buttons[field.x][field.y].setDisable(false);
        }
    }

    private void gameInit() {
        backgroundCanvas.getGraphicsContext2D().clearRect(0, 0, backgroundCanvas.getWidth(), backgroundCanvas.getHeight());
        lastPressed = buttons[4][6];
    }

    private void drawBackground() {
        AnchorPane background = (AnchorPane) backgroundCanvas.getParent();
        background.setStyle("-fx-background-color: green");
    }

    private void updatePlayingField() {
        clearEnabledFields();
    }

    private void updateScoreString() {
        scoreLabel.setText("Teraz gra: " + Player.ONE.name());
    }

    private void clearEnabledFields() {
        for(Button[] buttonArray : buttons) {
            for(Button button : buttonArray) {
                if(button != null) {
                    button.setDisable(true);
                }
            }
        }
    }

    private void createButton(int x, int y) {
        Button button = new Button("");
        button.setDisable(true);
        button.getStylesheets().add(getClass().getClassLoader().getResource("play_button.css").toExternalForm());
        button.setOnAction(event -> {
            endpoint.requestMove(x,y,boundPlayer);
        });
        buttonPane.add(button,x,y);
        buttons[x][y] = button;
    }

    private void drawLine(Button button1, Button button2) {
        final GraphicsContext gc = backgroundCanvas.getGraphicsContext2D();
        Point2D button1XY = new Point2D(button1.getTranslateX() + button1.getWidth()/2,
                                        button1.getTranslateY() + button1.getHeight()/2);
        Point2D button2XY = new Point2D(button2.getTranslateX() + button2.getWidth()/2,
                                        button2.getTranslateY() + button2.getHeight()/2);
        button1XY = button1.localToScene(button1XY);
        button2XY = button2.localToScene(button2XY);
        button1XY = backgroundCanvas.sceneToLocal(button1XY);
        button2XY = backgroundCanvas.sceneToLocal(button2XY);
        gc.strokeLine(button1XY.getX(),button1XY.getY(),button2XY.getX(),button2XY.getY());
    }
}
