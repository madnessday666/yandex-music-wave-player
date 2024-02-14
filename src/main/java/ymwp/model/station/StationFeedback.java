package ymwp.model.station;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StationFeedback {

    private StationFeedbackType type;
    private String timestamp = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).toString();
    private String from = "mobile-radio-user-onyourwave";
    private String trackId;
    private Integer totalPlayedSeconds;

    public StationFeedback(StationFeedbackType type) {
        this.type = type;
    }

    public StationFeedback(StationFeedbackType type, String trackId) {
        this.type = type;
        this.trackId = trackId;
    }

    public StationFeedback(StationFeedbackType type, String trackId, int totalPlayedSeconds) {
        this.type = type;
        this.trackId = trackId;
        this.totalPlayedSeconds = totalPlayedSeconds;
    }

    @Override
    public String toString() {
        return "StationFeedback{" +
                "type=" + type +
                ", timestamp=" + timestamp +
                ", from='" + from + '\'' +
                ", trackId='" + trackId + '\'' +
                ", totalPlayedSeconds=" + totalPlayedSeconds +
                '}';
    }
}
