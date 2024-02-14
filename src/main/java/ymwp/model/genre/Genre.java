package ymwp.model.genre;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ymwp.model.icon.Icon;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Genre {

    private String id;
    private int weight;
    private boolean composerTop;
    private String title;
    private Map<String, String> titles;
    private Map<String, String> images;
    private boolean showInMenu;
    private List<Integer> showInRegions;
    private String fullTitle;
    private String urlPart;
    private String color;
    private Icon radioIcon;
    private Object subGenres;
    private List<Integer> hideInRegions;

    @Override
    public String toString() {
        return "Genre{" +
                "\n\tid='" + id + '\'' +
                "\n\tweight=" + weight +
                "\n\tcomposerTop=" + composerTop +
                "\n\ttitle='" + title + '\'' +
                "\n\ttitles=" + titles +
                "\n\timages=" + images +
                "\n\tshowInMenu=" + showInMenu +
                "\n\tshowInRegions=" + showInRegions +
                "\n\tfullTitle='" + fullTitle + '\'' +
                "\n\turlPart='" + urlPart + '\'' +
                "\n\tcolor='" + color + '\'' +
                "\n\tradioIcon=" + radioIcon +
                "\n\tsubGenres=" + subGenres +
                "\n\thideInRegions=" + hideInRegions +
                '}';
    }
}
