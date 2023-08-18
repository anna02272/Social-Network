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
import java.time.LocalDateTime;
import java.util.List;

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
    report.setIsDeleted(false);
    Post post = postService.findOneById(postId);
    if (post == null) {
      return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }
    Report existing = reportService.findReportByPostAndUser(post, user);
    if (existing != null) {
      if (!existing.getIsDeleted()) {
        existing.setReason(report.getReason());
        reportService.create(existing);
        return ResponseEntity.ok(existing);
      }
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
    report.setIsDeleted(false);
    Comment comment = commentService.findOneById(commentId);
    if (comment == null) {
      return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }
    Report existing = reportService.findReportByCommentAndUser(comment, user);
    if (existing != null) {
      if (!existing.getIsDeleted()) {
        existing.setReason(report.getReason());
        reportService.create(existing);
        return ResponseEntity.ok(existing);
      }
    }
    report.setComment(comment);
    Report created = reportService.create(report);

    return ResponseEntity.ok(created);
  }

  @PostMapping("/reportUser/{id}")
  public ResponseEntity<Report> reportUser(@PathVariable("id") Long reportedUserId,@RequestBody Report report, Principal principal) throws ChangeSetPersister.NotFoundException {
    String username = principal.getName();
    User user = userService.findByUsername(username);
    report.setUser(user);
    report.setTimestamp(LocalDate.now());
    report.setAccepted(false);
    report.setIsDeleted(false);
    User reportedUser = userService.findOneById(reportedUserId);
    if (reportedUser == null) {
      return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }
//    Report existing = reportService.findReportByReportedUserAndUser(reportedUser, user);
//    if (existing != null) {
//      if (!existing.getIsDeleted()) {
//        existing.setReason(report.getReason());
//        reportService.create(existing);
//        return ResponseEntity.ok(existing);
//      }
//    }
    report.setReportedUser(reportedUser);
    Report created = reportService.create(report);

    return ResponseEntity.ok(created);
  }
  @PutMapping("/approve/{id}")
  public ResponseEntity<Report> approve(@PathVariable Long id) throws ChangeSetPersister.NotFoundException {
    Report existing = reportService.findOneById(id);

    if (existing == null) {
      return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }
    existing.setAccepted(true);
    existing.setIsDeleted(true);
    if (existing.getPost() != null) {
      existing.getPost().setIsDeleted(true);
    } else if (existing.getComment() != null) {
      existing.getComment().setIsDeleted(true);
    }
    Report updated = reportService.update(existing);

    return new ResponseEntity<>(updated, HttpStatus.OK);
  }
  @PutMapping("/decline/{id}")
  public ResponseEntity<Report> decline(@PathVariable Long id) throws ChangeSetPersister.NotFoundException {
    Report existing = reportService.findOneById(id);

    if (existing == null) {
      return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }
    existing.setIsDeleted(true);
    Report updated = reportService.update(existing);

    return new ResponseEntity<>(updated, HttpStatus.OK);
  }

  @GetMapping("/allPosts")
  public ResponseEntity<List<Report>> getAllReportsForPosts() {
    List<Report> reports = reportService.findAllReportsForPosts();
    return ResponseEntity.ok(reports);
  }
  @GetMapping("/allComments")
  public ResponseEntity<List<Report>> getAllReportsForComments() {
    List<Report> reports = reportService.findAllReportsForComments();
    return ResponseEntity.ok(reports);
  }

  @GetMapping("/allUsers")
  public ResponseEntity<List<Report>> getAllReportsForUsers() {
    List<Report> reports = reportService.findAllReportsForReportedUsers();
    return ResponseEntity.ok(reports);
  }

}

