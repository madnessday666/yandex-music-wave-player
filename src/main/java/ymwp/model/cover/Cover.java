package ymwp.model.cover;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Cover {

    private boolean custom;
    private String dir;
    private CoverType type;
    private String itemsUri;
    private String uri;
    private String version;
    private String error;

    @Override
    public String toString() {
        return "Cover{" +
                "custom=" + custom +
                ", dir='" + dir + '\'' +
                ", type=" + type +
                ", itemsUri='" + itemsUri + '\'' +
                ", uri='" + uri + '\'' +
                ", version='" + version + '\'' +
                ", error='" + error + '\'' +
                '}';
    }
}
