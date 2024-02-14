package ymwp.model.track;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TrackDownloadInfo {

    private TrackCodec codec;
    private boolean gain;
    private boolean preview;
    private String downloadInfoUrl;
    private boolean direct;
    private int bitrateInKbps;

    @Override
    public String toString() {
        return "TrackDownloadInfo{" +
                "codec=" + codec +
                ", gain=" + gain +
                ", preview=" + preview +
                ", downloadInfoUrl='" + downloadInfoUrl + '\'' +
                ", direct=" + direct +
                ", bitrateInKbps=" + bitrateInKbps +
                '}';
    }
}
