package ymwp.gui.authentication;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import ymwp.config.ClientConfiguration;
import ymwp.httpclient.station.StationHttpClient;

public class AuthenticationWindow {

    public VBox getMainContainer() {
        Label enterApiTokenLabel = new Label("Enter Your API token");
        enterApiTokenLabel.setStyle("-fx-padding: 0 0 5 0");
        TextField textField = new TextField();
        Button send = new Button("Send");
        Button cancel = new Button("Cancel");
        send.setOnMouseClicked(mouseEvent -> {
            ClientConfiguration.updateToken(textField.getText());
            ClientConfiguration.loadConfigurationFromFile();
            try {
                new StationHttpClient().getAccountId();
                enterApiTokenLabel.setText("Token accepted!");
                ((Node) mouseEvent.getSource()).getParent().getScene().getWindow().hide();
            } catch (RuntimeException e) {
                ClientConfiguration.updateToken(null);
                enterApiTokenLabel.setText("Incorrect token!");
            }
        });
        cancel.setOnMouseClicked(mouseEvent -> {
            System.exit(0);
        });
        HBox buttons = new HBox(send, cancel);
        buttons.setStyle("-fx-alignment: center; -fx-padding: 5 0 0 0; -fx-spacing: 10");
        VBox mainContainer = new VBox(enterApiTokenLabel, textField, buttons);
        mainContainer.setStyle("-fx-alignment: center; -fx-padding: 10 10 10 10;");
        return mainContainer;
    }

}
