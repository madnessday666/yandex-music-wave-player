package ymwp.util;

import javafx.scene.Node;

public class StyleHelper {

    public static final String HEART;
    public static final String HEART_FULL;
    public static final String NEXT;
    public static final String PAUSE;
    public static final String PLAY;
    public static final String VOLUME_HIGH;
    public static final String VOLUME_MEDIUM;
    public static final String VOLUME_MUTED;

    static {
        HEART = "btn-heart";
        HEART_FULL = "btn-heart-full";
        NEXT = "btn-next";
        PLAY = "btn-play";
        PAUSE = "btn-pause";
        VOLUME_HIGH = "btn-volume-high";
        VOLUME_MEDIUM = "btn-volume-medium";
        VOLUME_MUTED = "btn-volume-muted";
    }

    private StyleHelper() {
    }

    public static void changeNodeStyle(Node node, String styleToAdd) {
        if (!node.getStyleClass().contains(styleToAdd)) {
            String nodeClassName = node.getClass().getName();
            String nodeStyleClassName = nodeClassName.substring(nodeClassName.lastIndexOf('.') + 1);
            node.getStyleClass().removeIf(s -> !s.equalsIgnoreCase(nodeStyleClassName));
            node.getStyleClass().add(styleToAdd);
        }
    }

}
