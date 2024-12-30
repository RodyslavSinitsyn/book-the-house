package bth.common.contract;

import bth.common.dto.PostDto;
import bth.common.dto.filter.PostsFilterDto;

import java.util.List;

public interface PostService {

    List<PostDto> posts(int page, PostsFilterDto filterDto);

    PostDto post(String id);

    PostDto createPost(String imageUrl, String userId);
}
