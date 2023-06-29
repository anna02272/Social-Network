package com.ftn.socialNetwork.service;
import com.ftn.socialNetwork.model.entity.GroupAdmin;
import org.springframework.data.crossstore.ChangeSetPersister;

import java.util.List;


public interface GroupAdminService {
    GroupAdmin save (GroupAdmin groupAdmin);
  GroupAdmin findOneById(Long id) throws ChangeSetPersister.NotFoundException;
  List<GroupAdmin> findAll();
}
