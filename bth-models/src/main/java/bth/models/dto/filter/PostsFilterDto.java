package bth.models.dto.filter;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.beans.Transient;

@Data
@NoArgsConstructor
public class PostsFilterDto {
    private String country;
    private String city;
    private Integer priceMin;
    private Integer priceMax;

    @Transient
    public boolean isNotEmpty() {
        return StringUtils.isNotEmpty(country) ||
                StringUtils.isNotEmpty(city) ||
                priceMin != null ||
                priceMax != null;
    }
}