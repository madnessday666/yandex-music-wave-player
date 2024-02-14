package ymwp.model.icon;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Icon {

    private String backgroundColor;
    private String imageUrl;

    @Override
    public String toString() {
        return "Icon{" +
                "backgroundColor='" + backgroundColor + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
