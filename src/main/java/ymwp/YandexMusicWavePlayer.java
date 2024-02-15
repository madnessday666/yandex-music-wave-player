package ymwp;

import javafx.application.Application;
import javafx.stage.Stage;
import ymwp.config.ClientConfiguration;
import ymwp.gui.manager.WindowManager;
import ymwp.httpclient.station.StationHttpClient;

public class YandexMusicWavePlayer extends Application {

    @Override
    public void start(Stage stage) {
        this.checkConfiguration();
    }

    private void checkConfiguration() {
        ClientConfiguration.loadConfigurationFromFile();
        if (ClientConfiguration.config.getUser().getToken() != null) {
            try {
                new StationHttpClient().getAccountId();
                WindowManager.showPlayerWindow();
            } catch (RuntimeException e) {
                WindowManager.showAuthenticationWindow();
            }
        } else {
            WindowManager.showAuthenticationWindow();
        }
    }

}
