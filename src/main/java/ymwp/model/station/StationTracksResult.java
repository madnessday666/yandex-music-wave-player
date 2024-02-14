package ymwp.model.station;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ymwp.model.sequence.SequenceItem;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StationTracksResult {

    private StationId id;
    private List<SequenceItem> sequence;
    private String batchId;
    private boolean pumpkin;
    private String radioSessionId;

    @Override
    public String toString() {
        return "StationTracksResult{" +
                "id=" + id +
                ", sequence=" + sequence +
                ", batchId='" + batchId + '\'' +
                ", pumpkin=" + pumpkin +
                ", radioSessionId='" + radioSessionId + '\'' +
                '}';
    }
}
