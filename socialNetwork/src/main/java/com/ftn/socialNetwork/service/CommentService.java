package com.ftn.socialNetwork.service;

import com.ftn.socialNetwork.model.entity.Comment;
import org.springframework.data.crossstore.ChangeSetPersister;

import java.util.List;

public interface CommentService {
  Comment create(Comment comment);
  Comment update(Comment comment);
  Comment delete(Long id);
  Comment findOneById(Long id) throws ChangeSetPersister.NotFoundException;
  List<Comment> findAll();
  List<Comment> findAllByIsDeleted(boolean isDeleted);

  List<Comment> findCommentsByPostId(Long postId);

  List<Comment> findByPostIdAndIsDeleted(boolean isDeleted, Long postId);

  List<Comment> findAllByParentCommentId(Long parentCommentId);
}
