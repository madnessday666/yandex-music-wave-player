package ymwp.gui.manager;

import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import ymwp.config.ClientConfiguration;
import ymwp.gui.authentication.AuthenticationController;
import ymwp.gui.player.WavePlayerController;

import java.util.Objects;

public class WindowManager {

    public static void showAuthenticationWindow() {
        VBox mainContainer = new AuthenticationController().getMainContainer();
        AnchorPane root = new AnchorPane(mainContainer);
        Stage stage = new Stage(StageStyle.TRANSPARENT);
        Scene scene = new Scene(root);
        double CENTER_ON_SCREEN_X_FRACTION = 1.0f / 2;
        double CENTER_ON_SCREEN_Y_FRACTION = 1.0f / 3;
        Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
        double centerX = bounds.getMinX() + (bounds.getWidth() - stage.getWidth())
                * CENTER_ON_SCREEN_X_FRACTION;
        double centerY = bounds.getMinY() + (bounds.getHeight() - stage.getHeight())
                * CENTER_ON_SCREEN_Y_FRACTION;
        stage.setX(centerX);
        stage.setY(centerY);
        stage.setTitle("Enter Your API token");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
        stage.setOnHidden(event -> showPlayerWindow());
    }

    public static void showPlayerWindow() {
        HBox mainContainer = new WavePlayerController().getMainContainer();
        Pane root = new Pane(mainContainer);
        Scene scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(WindowManager.class.getResource("/css/styles.css")).toExternalForm());
        scene.setFill(Color.TRANSPARENT);
        Stage stage = new Stage(StageStyle.TRANSPARENT);
        stage.setX(ClientConfiguration.config.getPosition().getX());
        stage.setY(ClientConfiguration.config.getPosition().getY());
        stage.setTitle("Yandex Music Wave Player");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

}
