package bth.common.contract;

public interface ImageService {
    String uploadImage(byte[] imageBytes);
    byte[] downloadImage(String imageId);
    void deleteImage(String imageId);
}
