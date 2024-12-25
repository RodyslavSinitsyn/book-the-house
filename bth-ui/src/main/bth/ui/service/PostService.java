package bth.ui.service;

import bth.ui.dto.PostDto;

import java.util.List;

public interface PostService {

    List<PostDto> getPosts(int page);

    PostDto getPost(String id);
}
