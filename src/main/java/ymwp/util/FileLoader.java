package ymwp.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileLoader {

    private static final Path filesDir;

    static {
        try {
            filesDir = Paths.get(System.getProperty("user.home") + "/.config/ymwp/");
            if (!Files.exists(filesDir)) {
                Files.createDirectory(filesDir);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private FileLoader() {
    }

    public static File loadFileFromUrl(String url, String filename) {
        String fileInDir = String.format("%s/%s", filesDir, filename);
        try (FileOutputStream fileOutputStream = new FileOutputStream(fileInDir)) {
            ReadableByteChannel readableByteChannel = Channels.newChannel(new URI(url).toURL().openStream());
            FileChannel fileChannel = fileOutputStream.getChannel();
            fileChannel.transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
            return new File(fileInDir);
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public static void cleanUp() {
        File directory = new File(filesDir.toString());
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (!file.getName().equals("config.yaml")) {
                    file.delete();
                }
            }
        }
    }

}
