package com.ftn.socialNetwork.service.implementation;

import com.ftn.socialNetwork.model.entity.*;
import com.ftn.socialNetwork.repository.*;
import com.ftn.socialNetwork.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReportServiceImpl implements ReportService {

  @Autowired
  private ReportRepository reportRepository;
  @Autowired
  private PostRepository postRepository;
  @Autowired
  private UserRepository userRepository;

  @Autowired
  private CommentRepository commentRepository;
  @Override
  public Report create(Report report) {
    return reportRepository.save(report);
  }

  @Override
  public Report findReportByPostAndUser(Post post, User user) {
    return reportRepository.findByPostAndUser(post, user);
  }

  @Override
  public Report findReportByCommentAndUser(Comment comment, User user) {
    return reportRepository.findByCommentAndUser(comment, user);
  }

  @Override
  public Report findReportByUserAndUser(User user, User loggedUser) {
    return reportRepository.findByUserAndUser(user, loggedUser);
  }



}
