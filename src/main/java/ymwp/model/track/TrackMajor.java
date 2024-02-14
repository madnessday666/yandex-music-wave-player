package ymwp.model.track;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TrackMajor {

    private int id;
    private String name;

    @Override
    public String toString() {
        return "TrackMajor{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
