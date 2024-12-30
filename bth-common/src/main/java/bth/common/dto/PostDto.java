package bth.common.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PostDto {
    private String id;
    private String title;
    private String imageUrl;
    private PostDetailsDto details;
    private PostLocationDto location;
    private BookingStatus status;
    private String userId;
}
