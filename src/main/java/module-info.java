module yandex.music.wave.player.main {

    requires javafx.graphics;
    requires javafx.fxml;
    requires javafx.controls;
    requires javafx.media;
    requires static lombok;
    requires com.google.gson;
    requires java.net.http;
    requires org.yaml.snakeyaml;

    opens ymwp to javafx.graphics, javafx.base, javafx.controls, javafx.media, javafx.fxml;

    opens ymwp.model.album to com.google.gson;
    opens ymwp.model.artist to com.google.gson;
    opens ymwp.model.config to com.google.gson;
    opens ymwp.model.cover to com.google.gson;
    opens ymwp.model.genre to com.google.gson;
    opens ymwp.model.icon to com.google.gson;
    opens ymwp.model.label to com.google.gson;
    opens ymwp.model.sequence to com.google.gson;
    opens ymwp.model.station to com.google.gson;
    opens ymwp.model.track to com.google.gson;

    opens ymwp.gui.player to javafx.base, javafx.controls, javafx.fxml, javafx.graphics, javafx.media;
    opens ymwp.gui.authentication to javafx.base, javafx.controls, javafx.fxml, javafx.graphics, javafx.media;

    exports ymwp.model.config;
    exports ymwp;
}