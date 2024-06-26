package com.ftn.socialNetwork.service.intefraces;

import com.ftn.socialNetwork.model.entity.Group;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface GroupService {
    Group createGroup(Group group, MultipartFile pdfFile);
    Group updateGroup(Group group);
    Group deleteGroup(Long id);
    Group findOneById(Long id) throws ChangeSetPersister.NotFoundException;
    List<Group> findAll();

  List<Group> findAllByIsSuspended(boolean isDeleted);
  boolean existsByName(String name);
  List<Group> findAllByIsDeletedWithGroupAdmins(boolean isDeleted);
}
