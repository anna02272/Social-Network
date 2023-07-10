package com.ftn.socialNetwork.controller;

import com.ftn.socialNetwork.model.entity.Comment;
import com.ftn.socialNetwork.model.entity.Post;
import com.ftn.socialNetwork.model.entity.User;
import com.ftn.socialNetwork.service.CommentService;
import com.ftn.socialNetwork.service.PostService;
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
import java.util.List;

@RestController
@RequestMapping("api/comments")
public class CommentController {
  @Autowired
  private CommentService commentService;

  @Autowired
  private UserService userService;

  @Autowired
  private PostService postService;

  @PostMapping("/create/{id}")
  public ResponseEntity<Comment> create(@PathVariable("id") Long postId, @RequestBody Comment comment, Principal principal) throws ChangeSetPersister.NotFoundException {
    String username = principal.getName();
    User user = userService.findByUsername(username);
    Post post = postService.findOneById(postId);
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

  @PutMapping("/delete/{id}")
  public ResponseEntity<Comment> delete(@PathVariable Long id) throws ChangeSetPersister.NotFoundException {
    Comment existing = commentService.findOneById(id);

    if (existing == null) {
      return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    existing.setIsDeleted(true);
    Comment updated = commentService.update(existing);

    return new ResponseEntity<>(updated, HttpStatus.OK);
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

}
