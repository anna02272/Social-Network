package com.ftn.socialNetwork.controller;

import com.ftn.socialNetwork.model.entity.*;
import com.ftn.socialNetwork.service.CommentService;
import com.ftn.socialNetwork.service.PostService;
import com.ftn.socialNetwork.service.ReactionService;
import com.ftn.socialNetwork.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/comments")
public class CommentController {
  @Autowired
  private CommentService commentService;

  @Autowired
  private UserService userService;

  @Autowired
  private PostService postService;

  @Autowired
  private ReactionService reactionService;

  @PostMapping("/create/{id}")
  public ResponseEntity<Comment> create(@PathVariable("id") Long postId, @RequestBody Comment comment, Principal principal) throws ChangeSetPersister.NotFoundException {
    String username = principal.getName();
    User user = userService.findByUsername(username);
    Post post = postService.findOneById(postId);
    if (comment.getParentComment() != null) {
    Long parentCommentId = comment.getParentComment().getId();
    if (parentCommentId != null) {
      Comment parentComment = commentService.findOneById(parentCommentId);
      comment.setParentComment(parentComment);
    }
    }

    comment.setPost(post);
    comment.setUser(user);
    comment.setIsDeleted(false);
    comment.setTimeStamp(LocalDate.now());
    Comment created = commentService.create(comment);

    return ResponseEntity.ok(created);
  }

  @PutMapping("/update/{id}")
  public ResponseEntity<Comment> update(@PathVariable("id") Long id, @RequestBody Comment comment) throws ChangeSetPersister.NotFoundException {
    Comment existing = commentService.findOneById(id);

    if (existing == null) {
      return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    if (comment.getText() != null) {
      existing.setText(comment.getText());
    }
    if (comment.getTimeStamp() != null){
      existing.setTimeStamp(comment.getTimeStamp());
    }
    if (comment.getIsDeleted()!= null){
      existing.setIsDeleted(comment.getIsDeleted());
    }
    if (comment.getUser() != null) {
      existing.setUser(comment.getUser());
    }
    if (comment.getPost() != null) {
      existing.setPost(comment.getPost());
    }


    Comment updated = commentService.update(existing);

    return new ResponseEntity<>(updated, HttpStatus.OK);
  }

//  @PutMapping("/delete/{id}")
//  public ResponseEntity<Comment> delete(@PathVariable Long id) throws ChangeSetPersister.NotFoundException {
//    Comment existing = commentService.findOneById(id);
//
//    if (existing == null) {
//      return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
//    }
//
//    existing.setIsDeleted(true);
//    Comment updated = commentService.update(existing);
//
//    return new ResponseEntity<>(updated, HttpStatus.OK);
//  }
@PutMapping("/delete/{id}")
public ResponseEntity<Comment> delete(@PathVariable Long id) throws ChangeSetPersister.NotFoundException {
  Comment existing = commentService.findOneById(id);

  if (existing == null) {
    return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
  }
  deleteChildComments(existing);

  existing.setIsDeleted(true);
  Comment updated = commentService.update(existing);

  return new ResponseEntity<>(updated, HttpStatus.OK);
}
  private void deleteChildComments(Comment parentComment) {
    List<Comment> childComments = commentService.findAllByParentCommentId(parentComment.getId());
    for (Comment childComment : childComments) {
      deleteChildComments(childComment);
      childComment.setIsDeleted(true);
      commentService.update(childComment);
    }
  }

  @GetMapping("/find/{id}")
  public ResponseEntity<Comment> getById(@PathVariable Long id) throws ChangeSetPersister.NotFoundException {
    Comment comment = commentService.findOneById(id);
    return ResponseEntity.ok(comment);
  }
  @GetMapping("/findByPost/{postId}")
  public ResponseEntity<List<Comment>> getCommentsByPostId(@PathVariable Long postId) {
    List<Comment> comments = commentService.findByPostIdAndIsDeleted(false, postId);
    return ResponseEntity.ok(comments);
  }


  @GetMapping("/all")
  public ResponseEntity<List<Comment>> getAll() {
    List<Comment> comments = commentService.findAllByIsDeleted(false);
    Collections.sort(comments, (comment1, comment2) -> comment2.getTimeStamp().compareTo(comment1.getTimeStamp()));

    return ResponseEntity.ok(comments);
  }
  @GetMapping("/ascendingAll/{postId}")
  public ResponseEntity<List<Comment>> getAllAscending(@PathVariable Long postId){
    List<Comment> comments = commentService.findByPostIdAndIsDeleted(false, postId);
    Collections.sort(comments, (comment1, comment2) -> comment1.getTimeStamp().compareTo(comment2.getTimeStamp()));

    return ResponseEntity.ok(comments);
  }
  @GetMapping("/descendingAll/{postId}")
  public ResponseEntity<List<Comment>> getAllDescending(@PathVariable Long postId){
    List<Comment> comments = commentService.findByPostIdAndIsDeleted(false, postId);
    Collections.sort(comments, (comment1, comment2) -> comment2.getTimeStamp().compareTo(comment1.getTimeStamp()));

    return ResponseEntity.ok(comments);
  }
  @GetMapping("/likesAscendingAll/{postId}")
  public ResponseEntity<List<Comment>> getAllAscendingLikes(@PathVariable Long postId) {
    List<Comment> comments = commentService.findByPostIdAndIsDeleted(false, postId);
    comments.sort(Comparator.comparingInt(comment -> {
      Integer likeCount = reactionService.countReactionsByComment(comment).get(EReactionType.LIKE);
      return likeCount != null ? likeCount : 0;
    }));
     return ResponseEntity.ok(comments);
  }

  @GetMapping("/likesDescendingAll/{postId}")
  public ResponseEntity<List<Comment>> getAllDescendingLikes(@PathVariable Long postId) {
    List<Comment> comments = commentService.findByPostIdAndIsDeleted(false, postId);
    comments.sort(Comparator.comparingInt(comment -> {
      Integer likeCount = reactionService.countReactionsByComment(comment).get(EReactionType.LIKE);
      return likeCount != null ? likeCount : 0;
    }));
    Collections.reverse(comments);
    return ResponseEntity.ok(comments);
  }


  @GetMapping("/dislikesAscendingAll/{postId}")
  public ResponseEntity<List<Comment>> getAllByAscendingDislikes(@PathVariable Long postId) {
    List<Comment> comments = commentService.findByPostIdAndIsDeleted(false, postId);
    comments.sort(Comparator.comparingInt(comment -> {
      Integer dislikeCount = reactionService.countReactionsByComment(comment).get(EReactionType.DISLIKE);
      return dislikeCount != null ? dislikeCount : 0;
    }));
    return ResponseEntity.ok(comments);
  }
  @GetMapping("/dislikesDescendingAll/{postId}")
  public ResponseEntity<List<Comment>> getAllByDescendingDislikes(@PathVariable Long postId) {
    List<Comment> comments = commentService.findByPostIdAndIsDeleted(false, postId);
    comments.sort(Comparator.comparingInt(comment -> {
      Integer dislikeCount = reactionService.countReactionsByComment(comment).get(EReactionType.DISLIKE);
      return dislikeCount != null ? dislikeCount : 0;
    }));
    Collections.reverse(comments);
    return ResponseEntity.ok(comments);
  }

  @GetMapping("/heartsAscendingAll/{postId}")
  public ResponseEntity<List<Comment>> getAllByAscendingHearts(@PathVariable Long postId) {
    List<Comment> comments = commentService.findByPostIdAndIsDeleted(false, postId);
    comments.sort(Comparator.comparingInt(comment -> {
      Integer heartCount = reactionService.countReactionsByComment(comment).get(EReactionType.HEART);
      return heartCount != null ? heartCount : 0;
    }));
    return ResponseEntity.ok(comments);
  }
  @GetMapping("/heartsDescendingAll/{postId}")
  public ResponseEntity<List<Comment>> getAllByDescendingHearts(@PathVariable Long postId) {
    List<Comment> comments = commentService.findByPostIdAndIsDeleted(false, postId);
    comments.sort(Comparator.comparingInt(comment -> {
      Integer heartCount = reactionService.countReactionsByComment(comment).get(EReactionType.HEART);
      return heartCount != null ? heartCount : 0;
    }));
    Collections.reverse(comments);
    return ResponseEntity.ok(comments);
  }

}
