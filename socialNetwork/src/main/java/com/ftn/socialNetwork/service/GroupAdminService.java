package com.ftn.socialNetwork.service;
import com.ftn.socialNetwork.model.entity.*;
import org.springframework.data.crossstore.ChangeSetPersister;

import java.util.List;


public interface GroupAdminService {
    GroupAdmin save (GroupAdmin groupAdmin);
  GroupAdmin findOneById(Long id) throws ChangeSetPersister.NotFoundException;
  List<GroupAdmin> findAll();
  GroupAdmin findByGroupAndUser(Group group, User user);

  boolean existsByGroupAndUser(Group group, User user);
  GroupAdmin findByUsername(String username);
}
