package ymwp.config;

import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.representer.Representer;
import ymwp.model.config.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

@Getter
@Setter
@AllArgsConstructor
public class ClientConfiguration {

    private Yaml yaml;
    private String configDir;
    private String configFile;
    private String pathToConfig;
    public static ConfigFile config;

    public ClientConfiguration() {
        this.yaml = getPrettyYaml();
        this.configDir = System.getProperty("user.home") + "/.config/ymwp/";
        this.configFile = "config.yaml";
        this.pathToConfig = configDir + configFile;
    }

    private void createDefaultConfig() {
        File configDir = new File(this.configDir);
        if (configDir.exists() || configDir.mkdir()) {
            try (PrintWriter writer = new PrintWriter(pathToConfig)) {
                Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
                int centerX = (int) (bounds.getWidth() / 2 * 0.8);
                int centerY = (int) (bounds.getHeight() / 3);
                config = new ConfigFile(
                        new ConfigUser(null, 0L),
                        new ConfigPosition(centerX, centerY),
                        new ConfigBackground("#181818", 1.0, 20),
                        new ConfigTrackTitle("#f4f4f4", 1.0),
                        new ConfigBar("#6d6d6d", "#ffdb4d", 1.0),
                        0.6
                );
                yaml.dump(config, writer);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            throw new RuntimeException("Couldn't create dir!");
        }
    }

    private Yaml getPrettyYaml() {
        DumperOptions dumperOptions = new DumperOptions();
        dumperOptions.setIndent(2);
        dumperOptions.setPrettyFlow(true);
        dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        LoaderOptions loaderoptions = new LoaderOptions();
        loaderoptions.setTagInspector(tag -> tag.getClassName().equals(ConfigFile.class.getName()));
        return new Yaml(new Constructor(loaderoptions), new Representer(dumperOptions));
    }

    public void loadConfigurationFromFile() {
        if (Files.exists(Paths.get(pathToConfig))) {
            try (InputStream inputStream = new FileInputStream(pathToConfig)) {
                config = yaml.loadAs(inputStream, ConfigFile.class);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            createDefaultConfig();
        }
    }

    public void resetUserData() {
        this.updateToken(null);
        this.updateUserId(0L);
    }

    public void updateToken(String token) {
        config.getUser().setToken(token);
        try (PrintWriter writer = new PrintWriter(pathToConfig)) {
            yaml.dump(config, writer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateUserId(long userId) {
        config.getUser().setId(userId);
        try (PrintWriter writer = new PrintWriter(pathToConfig)) {
            yaml.dump(config, writer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
