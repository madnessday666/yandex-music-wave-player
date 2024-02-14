package ymwp.model.config;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ConfigFile {

    private ConfigUser user;
    private ConfigPosition position;
    private ConfigBackground background;
    private ConfigTrackTitle trackTitle;
    private ConfigBar bar;
    private Double volume;

}
