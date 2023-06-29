package com.ftn.socialNetwork.service;

import com.ftn.socialNetwork.model.entity.Comment;
import org.springframework.data.crossstore.ChangeSetPersister;

import java.util.List;

public interface CommentService {
  Comment createComment(Comment comment);
  Comment updateComment(Comment comment);
  Comment deleteComment(Long id);
  Comment findOneById(Long id) throws ChangeSetPersister.NotFoundException;
  List<Comment> findAll();
}
