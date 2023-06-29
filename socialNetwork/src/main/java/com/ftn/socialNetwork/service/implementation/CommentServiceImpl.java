package com.ftn.socialNetwork.service.implementation;

import com.ftn.socialNetwork.model.entity.Comment;
import com.ftn.socialNetwork.repository.CommentRepository;
import com.ftn.socialNetwork.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

  @Autowired
  private CommentRepository commentRepository;

  @Autowired
  public CommentServiceImpl(CommentRepository commentRepository) {
    this.commentRepository = commentRepository;
  }

  @Override
  public Comment createComment(Comment comment) {
    return commentRepository.save(comment);
  }

  @Override
  public Comment updateComment(Comment comment) {
    return commentRepository.save(comment);
  }

  @Override
  public Comment deleteComment(Long id) {
    commentRepository.deleteById(id);
    return null;
  }

  @Override
  public Comment findOneById(Long id) throws ChangeSetPersister.NotFoundException {
    return commentRepository.findById(id)
      .orElseThrow(() -> new ChangeSetPersister.NotFoundException());
  }

  @Override
  public List<Comment> findAll() {
    return commentRepository.findAll();
  }
}
