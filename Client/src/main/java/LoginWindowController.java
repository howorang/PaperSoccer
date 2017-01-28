import common.Field;
import common.Network;
import common.Player;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import sun.nio.ch.Net;

import java.io.IOException;
import java.util.List;

/**
 * Created by howor on 27.01.2017.
 */
public class LoginWindowController {

    @FXML
    private Stage primaryStage;

    @FXML
    private Label statusLabel;

    private Endpoint endpoint;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void initialize() {
        endpoint = Endpoint.getInstance();
        endpoint.setLoginWindowController(this);

    }

    public void startGame(Player player, List<Network.SimpleField>availableFields) {
        Platform.runLater(() -> {
            try {
                FXMLLoader gameScreenLoader = new FXMLLoader(getClass().getClassLoader().getResource("play_window.fxml"));
                Parent parent = gameScreenLoader.load();
                PlayWindowController controller = gameScreenLoader.getController();
                endpoint.setPlayWindowController(controller);
                Scene scene = new Scene(parent);
                Stage stage = new Stage();
                controller.setBoundPlayer(player);
                controller.enableAvailableFields(availableFields);
                controller.setPrimaryStage(stage);
                stage.setScene(scene);
                stage.show();
                primaryStage.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void connectingError() {
        Platform.runLater(() -> {
            statusLabel.setText("Nie udało się połączyć");
        });
    }
}
