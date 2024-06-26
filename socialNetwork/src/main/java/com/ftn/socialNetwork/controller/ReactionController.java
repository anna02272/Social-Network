package com.ftn.socialNetwork.controller;

import com.ftn.socialNetwork.model.entity.*;
import com.ftn.socialNetwork.service.intefraces.CommentService;
import com.ftn.socialNetwork.service.intefraces.PostService;
import com.ftn.socialNetwork.service.intefraces.ReactionService;
import com.ftn.socialNetwork.service.intefraces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("api/reactions")
public class ReactionController {

  @Autowired
  private ReactionService reactionService;

  @Autowired
  private UserService userService;
  @Autowired
  private PostService postService;

  @Autowired
  private CommentService commentService;

  @PostMapping("/reactToPost/{id}")
  public ResponseEntity<Reaction> reactToPost(@PathVariable("id") Long postId, @RequestBody Reaction reaction, Principal principal) throws ChangeSetPersister.NotFoundException {
    String username = principal.getName();
    User user = userService.findByUsername(username);
    reaction.setUser(user);
    reaction.setTimeStamp(LocalDate.now());
    Post post = postService.findOneById(postId);
    if (post == null) {
      return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }
    Reaction existingReaction = reactionService.findReactionByPostAndUser(post, user);
    if (existingReaction != null) {
      existingReaction.setType(reaction.getType());
      existingReaction.setTimeStamp(LocalDate.now());
      reactionService.create(existingReaction);
      return ResponseEntity.ok(existingReaction);
    }

    reaction.setPost(post);
    Reaction createdReaction = reactionService.create(reaction);

    return ResponseEntity.ok(createdReaction);
  }
  @GetMapping("/count/post/{postId}")
  public ResponseEntity<Map<EReactionType, Integer>> countReactionsByPost(
    @PathVariable("postId") Long postId
  ) throws ChangeSetPersister.NotFoundException {
    Post post = postService.findOneById(postId);
    if (post == null) {
      return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }
    Map<EReactionType, Integer> reactionCounts = reactionService.countReactionsByPost(post);
    return ResponseEntity.ok(reactionCounts);
  }


  @GetMapping("/find/post/{postId}/user/{userId}")
  public ResponseEntity<Reaction> findReactionByPostAndUser(@PathVariable("postId") Long postId, @PathVariable("userId") Long userId) throws ChangeSetPersister.NotFoundException {
    Post post = postService.findOneById(postId);
    User user = userService.findOneById(userId);

    if (post == null || user == null) {
      return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    Reaction reaction = reactionService.findReactionByPostAndUser(post, user);
    return ResponseEntity.ok(reaction);
  }


  @PostMapping("/reactToComment/{id}")
  public ResponseEntity<Reaction> reactToComment(@PathVariable("id") Long commentId,@RequestBody Reaction reaction, Principal principal) throws ChangeSetPersister.NotFoundException {
    String username = principal.getName();
    User user = userService.findByUsername(username);
    reaction.setUser(user);
    reaction.setTimeStamp(LocalDate.now());
    Comment comment = commentService.findOneById(commentId);
    if (comment == null) {
      return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }
    Reaction existingReaction = reactionService.findReactionByCommentAndUser(comment, user);
    if (existingReaction != null) {
      existingReaction.setType(reaction.getType());
      existingReaction.setTimeStamp(LocalDate.now());
      reactionService.create(existingReaction);
      return ResponseEntity.ok(existingReaction);
    }
    reaction.setComment(comment);
    Reaction createdReaction = reactionService.create(reaction);

    return ResponseEntity.ok(createdReaction);
  }
  @GetMapping("/count/comment/{commentId}")
  public ResponseEntity<Map<EReactionType, Integer>> countReactionsByComment(
    @PathVariable("commentId") Long commentId
  ) throws ChangeSetPersister.NotFoundException {
    Comment comment = commentService.findOneById(commentId);
    if (comment == null) {
      return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }
    Map<EReactionType, Integer> reactionCounts = reactionService.countReactionsByComment(comment);
    return ResponseEntity.ok(reactionCounts);
  }


  @GetMapping("/find/comment/{commentId}/user/{userId}")
  public ResponseEntity<Reaction> findReactionByCommentAndUser(@PathVariable("commentId") Long commentId, @PathVariable("userId") Long userId) throws ChangeSetPersister.NotFoundException {
    Comment comment = commentService.findOneById(commentId);
    User user = userService.findOneById(userId);

    if (comment == null || user == null) {
      return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    Reaction reaction = reactionService.findReactionByCommentAndUser(comment, user);
    return ResponseEntity.ok(reaction);
  }


}

