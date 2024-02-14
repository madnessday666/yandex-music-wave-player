package ymwp.model.track;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TrackShort {

    private String id;
    private String albumId;
    private String timestamp;

    @Override
    public String toString() {
        return "TrackShort{" +
                "id='" + id + '\'' +
                ", albumId='" + albumId + '\'' +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }
}
