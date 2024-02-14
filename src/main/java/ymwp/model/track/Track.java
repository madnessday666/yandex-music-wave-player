package ymwp.model.track;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ymwp.model.album.Album;
import ymwp.model.artist.Artist;

import java.util.Arrays;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Track {

    private List<Album> albums;
    private List<Artist> artists;
    private int[] arr = {1, 2, 3};
    private boolean available;
    private boolean availableForPremiumUsers;
    private boolean availableFullWithoutPermission;
    private String coverUri;
    private long durationMs;
    private long fileSize;
    private String id;
    private boolean lyricsAvailable;
    private TrackMajor major;
    private String ogImage;
    private long previewDurationMs;
    private String realId;
    private boolean rememberPosition;
    private String storageDir;
    private String title;
    private TrackType type;

    @Override
    public String toString() {
        return "Track{" +
                "albums=" + albums +
                ", artists=" + artists +
                ", arr=" + Arrays.toString(arr) +
                ", available=" + available +
                ", availableForPremiumUsers=" + availableForPremiumUsers +
                ", availableFullWithoutPermission=" + availableFullWithoutPermission +
                ", coverUri='" + coverUri + '\'' +
                ", durationMs=" + durationMs +
                ", fileSize=" + fileSize +
                ", id='" + id + '\'' +
                ", lyricsAvailable=" + lyricsAvailable +
                ", major=" + major +
                ", ogImage='" + ogImage + '\'' +
                ", previewDurationMs=" + previewDurationMs +
                ", realId='" + realId + '\'' +
                ", rememberPosition=" + rememberPosition +
                ", storageDir='" + storageDir + '\'' +
                ", title='" + title + '\'' +
                ", type=" + type +
                '}';
    }
}
