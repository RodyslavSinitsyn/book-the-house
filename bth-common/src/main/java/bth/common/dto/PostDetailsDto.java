package bth.common.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Objects;

@Data
@NoArgsConstructor
public class PostDetailsDto {
    private String description;
    private LocalDate availableFrom;
    private LocalDate availableTo;
    private int price;

    public PostDetailsDto(
            String description,
            LocalDate availableFrom,
            LocalDate availableTo,
            int price
    ) {
        this.description = description;
        this.availableFrom = availableFrom;
        this.availableTo = availableTo;
        this.price = price;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (PostDetailsDto) obj;
        return Objects.equals(this.description, that.description) &&
                Objects.equals(this.availableFrom, that.availableFrom) &&
                Objects.equals(this.availableTo, that.availableTo) &&
                Objects.equals(this.price, that.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, availableFrom, availableTo, price);
    }

    @Override
    public String toString() {
        return "BookingDetailsDto[" +
                "description=" + description + ", " +
                "availableFrom=" + availableFrom + ", " +
                "availableTo=" + availableTo + ", " +
                "price=" + price + ']';
    }

}
