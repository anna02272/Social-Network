package com.ftn.socialNetwork.service;

import com.ftn.socialNetwork.model.entity.Comment;
import com.ftn.socialNetwork.model.entity.Post;
import org.springframework.data.crossstore.ChangeSetPersister;

import java.util.List;

public interface PostService {
    Post createPost(Post post);
    Post updatePost(Post post);
    Post deletePost(Long id);
    Post findOneById(Long id) throws ChangeSetPersister.NotFoundException;
    List<Post> findAll();
  List<Post> findAllByIsDeleted(boolean isDeleted);
}
