package com.ftn.socialNetwork.service;

import com.ftn.socialNetwork.model.entity.Group;
import org.springframework.data.crossstore.ChangeSetPersister;

import java.util.List;

public interface GroupService {
    Group createGroup(Group group);
    Group updateGroup(Group group);
    Group deleteGroup(Long id);
    Group findOneById(Long id) throws ChangeSetPersister.NotFoundException;
    List<Group> findAll();

  List<Group> findAllByIsDeleted(boolean isDeleted);
}
