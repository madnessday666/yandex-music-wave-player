package ymwp.model.track;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TrackDownloadLinkDetails {

    private String s;
    private String ts;
    private String path;
    private String host;

    @Override
    public String toString() {
        return "TrackDownloadLinkDetails{" +
                "s='" + s + '\'' +
                ", ts='" + ts + '\'' +
                ", path='" + path + '\'' +
                ", host='" + host + '\'' +
                '}';
    }
}
