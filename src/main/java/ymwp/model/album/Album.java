package ymwp.model.album;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ymwp.model.artist.Artist;
import ymwp.model.label.Label;
import ymwp.model.track.Track;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Album {

    private long id;
    private String error;
    private String title;
    private AlbumType type;
    private AlbumMetaType metaType;
    private int year;
    private String releaseDate;
    private String coverUri;
    private String ogImage;
    private String genre;
    //    private Object buy;
    private int trackCount;
    private boolean recent;
    private boolean veryImportant;
    private List<Artist> artists;
    private List<Label> labels;
    private boolean available;
    private boolean availableForPremiumUsers;
    private boolean availableForMobile;
    private boolean availablePartially;
    private List<Integer> bests;
    //   private Object prerolls;
    private List<Track> volumes;

    @Override
    public String toString() {
        return "Album{" +
                "id=" + id +
                ", error='" + error + '\'' +
                ", title='" + title + '\'' +
                ", type=" + type +
                ", metaType=" + metaType +
                ", year=" + year +
                ", releaseDate='" + releaseDate + '\'' +
                ", coverUri='" + coverUri + '\'' +
                ", ogImage='" + ogImage + '\'' +
                ", genre='" + genre + '\'' +
                ", trackCount=" + trackCount +
                ", recent=" + recent +
                ", veryImportant=" + veryImportant +
                ", artists=" + artists +
                ", labels=" + labels +
                ", available=" + available +
                ", availableForPremiumUsers=" + availableForPremiumUsers +
                ", availableForMobile=" + availableForMobile +
                ", availablePartially=" + availablePartially +
                ", bests=" + bests +
                ", volumes=" + volumes +
                '}';
    }
}
