package com.ftn.socialNetwork.service.implementation;

import com.ftn.socialNetwork.model.entity.Comment;
import com.ftn.socialNetwork.repository.CommentRepository;
import com.ftn.socialNetwork.repository.PostRepository;
import com.ftn.socialNetwork.service.CommentService;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

  @Autowired
  private CommentRepository commentRepository;

  @Autowired
  private PostRepository postRepository;

  @Override
  public Comment create(Comment comment) {
    return commentRepository.save(comment);
  }

  @Override
  public Comment update(Comment comment) {
    return commentRepository.save(comment);
  }

  @Override
  public Comment delete(Long id) {
    commentRepository.deleteById(id);
    return null;
  }

  @Override
  public Comment findOneById(Long id) throws ChangeSetPersister.NotFoundException {
    return commentRepository.findById(id)
      .orElseThrow(() -> new ChangeSetPersister.NotFoundException());
  }
  @Override
  public List<Comment> findCommentsByPostId(Long postId) {
    return commentRepository.findByPostId(postId);
  }


  @Override
  public List<Comment> findAll() {
    return commentRepository.findAll();
  }

  @Autowired
  private EntityManager entityManager;

  public List<Comment> findAllByIsDeleted(boolean isDeleted) {
    Session session = entityManager.unwrap(Session.class);
    Filter filter = session.enableFilter("deletedCommentFilter");
    filter.setParameter("isDeleted", isDeleted);
    List<Comment> comments = commentRepository.findAll();
    session.disableFilter("deletedCommentFilter");
    return comments;
  }
}
