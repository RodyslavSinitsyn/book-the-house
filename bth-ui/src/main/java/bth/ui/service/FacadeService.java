package bth.ui.service;

import bth.models.contract.PostService;
import bth.models.dto.PostDto;
import bth.ui.exception.PostServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
public class FacadeService {

    private final ImageServiceClient imageService;
    private final PostService postService;

    public PostDto createPost(MultipartFile file) {
        String imageId = "/img/house.jpeg";
        try {
            if (!file.isEmpty()) {
                imageId = "/images/" + imageService.uploadImage(file.getBytes());
            }
            return postService.createPost(imageId);
        } catch (PostServiceException e) {
            if (!file.isEmpty()) {
                log.error("Deleting saved image due to 'post-service' error: {}", e.getMessage());
                imageService.deleteImage(imageId);
            }
            throw new PostServiceException(e.getMessage(), e);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new PostServiceException(e.getMessage(), e);
        }
    }
}
