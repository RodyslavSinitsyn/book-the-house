package bth.models.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@NoArgsConstructor
public class PostLocationDto {
    private String country;
    private String city;
    private String street;
    private String houseNumber;

    public PostLocationDto(
            String country,
            String city,
            String street,
            String houseNumber
    ) {
        this.country = country;
        this.city = city;
        this.street = street;
        this.houseNumber = houseNumber;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (PostLocationDto) obj;
        return Objects.equals(this.country, that.country) &&
                Objects.equals(this.city, that.city) &&
                Objects.equals(this.street, that.street) &&
                Objects.equals(this.houseNumber, that.houseNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(country, city, street, houseNumber);
    }

    @Override
    public String toString() {
        return "LocationDto[" +
                "country=" + country + ", " +
                "city=" + city + ", " +
                "street=" + street + ", " +
                "houseNumber=" + houseNumber + ']';
    }

}
