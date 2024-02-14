package ymwp.model.artist;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ymwp.model.cover.Cover;
import ymwp.model.genre.Genre;
import ymwp.model.track.Track;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Artist {

    private boolean composer;
    private Cover cover;
    private List<Genre> genres;
    private String id;
    private String name;
    private List<Track> popularTracks;
    private boolean various;

    @Override
    public String toString() {
        return "Artist{" +
                "composer=" + composer +
                ", cover=" + cover +
                ", genres=" + genres +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", popularTracks=" + popularTracks +
                ", various=" + various +
                '}';
    }
}
