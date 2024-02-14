package ymwp.model.station;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StationId {

    StationIdTag tag;
    StationIdType type;

    @Override
    public String toString() {
        return "StationId{" +
                "tag=" + tag +
                ", type=" + type +
                '}';
    }
}
