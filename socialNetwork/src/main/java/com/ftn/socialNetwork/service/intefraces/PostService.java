package com.ftn.socialNetwork.service.intefraces;

import com.ftn.socialNetwork.model.entity.Comment;
import com.ftn.socialNetwork.model.entity.Post;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PostService {
    Post createPost(Post post, MultipartFile pdfFile);
    Post updatePost(Post post, MultipartFile pdfFile);

    Post deletePost(Post post);

    Post findOneById(Long id) throws ChangeSetPersister.NotFoundException;
    List<Post> findAll();
  List<Post> findAllByGroupId(Long groupId);
  List<Post> findAllByIsDeleted(boolean isDeleted);
}
