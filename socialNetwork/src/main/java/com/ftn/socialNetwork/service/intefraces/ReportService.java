package com.ftn.socialNetwork.service.intefraces;

import com.ftn.socialNetwork.model.entity.*;
import org.springframework.data.crossstore.ChangeSetPersister;

import java.util.List;

public interface ReportService {
  Report create(Report report);

  Report findReportByPostAndUser(Post post, User user);

  Report findReportByCommentAndUser(Comment comment, User user);
  Report update(Report report);
  Report findOneById(Long id) throws ChangeSetPersister.NotFoundException;
  Report findReportByReportedUserAndUser(User reportedUser, User user);
  List<Report> findAllReportsForPosts();
  List<Report> findAllReportsForComments();
  List<Report> findAllReportsForReportedUsers();
}
