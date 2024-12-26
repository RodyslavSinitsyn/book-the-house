package bth.models.contract;

import bth.models.dto.PostDto;

import java.util.List;

public interface PostService {

    List<PostDto> posts(int page);

    PostDto post(String id);
}
