package ymwp.model.sequence;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SequenceItemTrackParameters {

    private int bpm;
    private int hue;
    private double energy;

    @Override
    public String toString() {
        return "SequenceItemTrackParameters{" +
                "bpm=" + bpm +
                ", hue=" + hue +
                ", energy=" + energy +
                '}';
    }
}
