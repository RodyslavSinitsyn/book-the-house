package bth.models.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@NoArgsConstructor
public class PostDto {
    private String id;
    private String title;
    private String imageUrl;
    private BookingDetailsDto details;
    private LocationDto location;
    private BookingStatus status;

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (PostDto) obj;
        return Objects.equals(this.id, that.id) &&
                Objects.equals(this.title, that.title) &&
                Objects.equals(this.imageUrl, that.imageUrl) &&
                Objects.equals(this.details, that.details) &&
                Objects.equals(this.location, that.location) &&
                Objects.equals(this.status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, imageUrl, details, location, status);
    }

    @Override
    public String toString() {
        return "PostDto[" +
                "id=" + id + ", " +
                "title=" + title + ", " +
                "imageUrl=" + imageUrl + ", " +
                "details=" + details + ", " +
                "location=" + location + ", " +
                "status=" + status + ']';
    }

}
