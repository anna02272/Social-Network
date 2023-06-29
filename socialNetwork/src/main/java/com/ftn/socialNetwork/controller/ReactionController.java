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

@RestController
@RequestMapping("api/reactions")
public class ReactionController {

  @Autowired
  private  ReactionService reactionService;

  @Autowired
  private UserService userService;
  @Autowired
  private PostService postService;

  @Autowired
  private CommentService commentService;

  @Autowired
  public ReactionController(ReactionService reactionService) {
    this.reactionService = reactionService;
  }


  @PostMapping("/reactToPost/{id}")
  public ResponseEntity<Reaction> reactToPost(@PathVariable("id") Long postId,@RequestBody Reaction reaction, Principal principal) throws ChangeSetPersister.NotFoundException {
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
      reactionService.create(existingReaction);
      return ResponseEntity.ok(existingReaction);
    }

    reaction.setPost(post);
    Reaction createdReaction = reactionService.create(reaction);

    return ResponseEntity.ok(createdReaction);
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
      reactionService.create(existingReaction);
      return ResponseEntity.ok(existingReaction);
    }
    reaction.setComment(comment);
    Reaction createdReaction = reactionService.create(reaction);

    return ResponseEntity.ok(createdReaction);
  }


}

