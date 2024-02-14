package ymwp.util;

import javafx.animation.FadeTransition;
import javafx.scene.Node;
import javafx.util.Duration;

public class FadeTransitionFiller {

    private FadeTransitionFiller() {
    }

    public static void fillWithFade(double from, double to, Duration duration, Node node) {
        FadeTransition fadeTransition = new FadeTransition(duration, node);
        fadeTransition.setFromValue(from);
        fadeTransition.setToValue(to);
        fadeTransition.play();
    }

}
