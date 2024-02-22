package ymwp.model.config;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ConfigFile {

    private ConfigBackground background;
    private ConfigBar bar;
    private ConfigDuration duration;
    private ConfigPosition position;
    private ConfigTitle title;
    private ConfigUser user;
    private Double volume;

}
