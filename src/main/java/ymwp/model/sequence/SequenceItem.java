package ymwp.model.sequence;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ymwp.model.track.Track;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SequenceItem {

    private SequenceItemType type;
    private Track track;
    private boolean liked;
    private SequenceItemTrackParameters trackParameters;

    @Override
    public String toString() {
        return "SequenceItem{" +
                "type=" + type +
                ", track=" + track +
                ", liked=" + liked +
                ", trackParameters=" + trackParameters +
                '}';
    }
}
