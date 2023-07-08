package com.ftn.socialNetwork.service;

import com.ftn.socialNetwork.model.entity.*;

public interface ReportService {
  Report create(Report report);

  Report findReportByPostAndUser(Post post, User user);

  Report findReportByCommentAndUser(Comment comment, User user);

  Report findReportByUserAndUser(User user, User loggedUser);
}
