package bth.models.contract;

import bth.models.dto.PostDto;
import bth.models.dto.filter.PostsFilterDto;

import java.util.List;

public interface PostService {

    List<PostDto> posts(int page, PostsFilterDto filterDto);

    PostDto post(String id);

    PostDto createPost(String imageUrl, String userId);
}
