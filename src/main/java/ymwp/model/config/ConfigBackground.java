package ymwp.model.config;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ConfigBackground {

    private String color;
    private Double opacity;
    private Integer radius;

}
