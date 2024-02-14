package ymwp.util;

import javafx.util.Duration;

public class DurationAdjuster {

    private DurationAdjuster() {
    }

    public static String adjust(Duration duration) {
        int totalDuration = (int) duration.toSeconds();
        int hours = 0;
        int minutes = 0;
        int seconds = 0;
        if (totalDuration >= 3600) {
            hours = totalDuration / 3600;
            totalDuration -= hours * 3600;
        }
        if (totalDuration >= 60) {
            minutes = totalDuration / 60;
            totalDuration -= minutes * 60;
        }
        seconds = totalDuration;
        return hours == 0 ?
                String.format("%d:%02d", minutes, seconds) :
                String.format("%d:%02d:%02d", hours, minutes, seconds);
    }

}
