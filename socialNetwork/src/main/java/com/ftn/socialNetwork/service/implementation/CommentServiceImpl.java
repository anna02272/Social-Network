package com.ftn.socialNetwork.service.implementation;

import com.ftn.socialNetwork.indexservice.interfaces.PostIndexingService;
import com.ftn.socialNetwork.model.entity.Comment;
import com.ftn.socialNetwork.repository.CommentRepository;
import com.ftn.socialNetwork.repository.PostRepository;
import com.ftn.socialNetwork.service.intefraces.CommentService;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

  @Autowired
  private CommentRepository commentRepository;

  @Qualifier("postIndexingServiceImpl")
  @Autowired
  private PostIndexingService postIndexService;

  @Override
  public Comment create(Comment comment) {
    Comment createdComment = commentRepository.save(comment);
    var postId = comment.getPost().getId().toString();
    postIndexService.updateCommentCount(postId);
    return createdComment;
  }

  @Override
  public Comment update(Comment comment) {
    return commentRepository.save(comment);
  }

  @Override
  public Comment deleteComment(Comment comment) {
    Comment deletedComment = commentRepository.save(comment);
    var postId = comment.getPost().getId().toString();
    postIndexService.deleteCommentCount(postId);
    return deletedComment;
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
  public List<Comment> findByPostIdAndIsDeleted (boolean isDeleted, Long postId) {
    Session session = entityManager.unwrap(Session.class);
    Filter filter = session.enableFilter("deletedCommentFilter");
    filter.setParameter("isDeleted", isDeleted);
    List<Comment> comments = commentRepository.findByPostId(postId);
    session.disableFilter("deletedCommentFilter");
    return comments;
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
  public List<Comment> findAllByParentCommentId(Long parentCommentId) {
    return commentRepository.findAllByParentCommentId(parentCommentId);
  }

}
