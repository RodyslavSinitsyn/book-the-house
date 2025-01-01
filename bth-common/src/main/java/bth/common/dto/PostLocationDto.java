package bth.common.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PostLocationDto {
    private String country;
    private String state;
    private String city;
    private String street;
    private String houseNumber;
}
