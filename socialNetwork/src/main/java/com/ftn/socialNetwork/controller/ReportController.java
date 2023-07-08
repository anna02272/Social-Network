package com.ftn.socialNetwork.controller;

import com.ftn.socialNetwork.model.entity.*;
import com.ftn.socialNetwork.service.CommentService;
import com.ftn.socialNetwork.service.PostService;
import com.ftn.socialNetwork.service.ReportService;
import com.ftn.socialNetwork.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;

@RestController
@RequestMapping("api/reports")
public class ReportController {

  @Autowired
  private ReportService reportService;

  @Autowired
  private UserService userService;
  @Autowired
  private PostService postService;

  @Autowired
  private CommentService commentService;

  @PostMapping("/reportPost/{id}")
  public ResponseEntity<Report> reportPost(@PathVariable("id") Long postId,@RequestBody Report report, Principal principal) throws ChangeSetPersister.NotFoundException {
    String username = principal.getName();
    User user = userService.findByUsername(username);
    report.setUser(user);
    report.setTimestamp(LocalDate.now());
    report.setAccepted(false);
    Post post = postService.findOneById(postId);
    if (post == null) {
      return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }
    Report existing = reportService.findReportByPostAndUser(post, user);
    if (existing != null) {
      existing.setReason(report.getReason());
      reportService.create(existing);
      return ResponseEntity.ok(existing);
    }

    report.setPost(post);
    Report created = reportService.create(report);

    return ResponseEntity.ok(created);
  }

  @PostMapping("/reportComment/{id}")
  public ResponseEntity<Report> reportComment(@PathVariable("id") Long commentId,@RequestBody Report report, Principal principal) throws ChangeSetPersister.NotFoundException {
    String username = principal.getName();
    User user = userService.findByUsername(username);
    report.setUser(user);
    report.setTimestamp(LocalDate.now());
    report.setAccepted(false);
    Comment comment = commentService.findOneById(commentId);
    if (comment == null) {
      return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }
    Report existing = reportService.findReportByCommentAndUser(comment, user);
    if (existing != null) {
      existing.setReason(report.getReason());
      reportService.create(existing);
      return ResponseEntity.ok(existing);
    }
    report.setComment(comment);
    Report created = reportService.create(report);

    return ResponseEntity.ok(created);
  }

  //dodaj usera koji je prijavljen a koji je prijavio!
  @PostMapping("/reportUser/{id}")
  public ResponseEntity<Report> reportUser(@PathVariable("id") Long userId,@RequestBody Report report, Principal principal) throws ChangeSetPersister.NotFoundException {
    String username = principal.getName();
    User loggedUser = userService.findByUsername(username);
    report.setUser(loggedUser);
    report.setTimestamp(LocalDate.now());
    report.setAccepted(false);
    User user = userService.findOneById(userId);
    if (user == null) {
      return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }
    Report existing = reportService.findReportByUserAndUser(user, user);
    if (existing != null) {
      existing.setReason(report.getReason());
      reportService.create(existing);
      return ResponseEntity.ok(existing);
    }
    report.setUser(user);
    Report created = reportService.create(report);

    return ResponseEntity.ok(created);
  }


}

