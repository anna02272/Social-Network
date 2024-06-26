package com.ftn.socialNetwork.service.intefraces;
import com.ftn.socialNetwork.model.entity.*;
import org.springframework.data.crossstore.ChangeSetPersister;

import java.util.List;


public interface GroupAdminService {
    GroupAdmin save (GroupAdmin groupAdmin);
  GroupAdmin findOneById(Long id) throws ChangeSetPersister.NotFoundException;
  List<GroupAdmin> findAll();
  GroupAdmin findByGroupAndUser(Group group, User user);
  GroupAdmin findByGroup(Group group);

  boolean existsByGroupAndUser(Group group, User user);
  GroupAdmin delete(GroupAdmin groupAdmin);
}
