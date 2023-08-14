package com.ftn.socialNetwork.service;

import com.ftn.socialNetwork.model.entity.Group;
import com.ftn.socialNetwork.model.entity.GroupRequest;
import com.ftn.socialNetwork.model.entity.User;
import org.springframework.data.crossstore.ChangeSetPersister;

import java.util.List;

public interface GroupRequestService {
    GroupRequest create(GroupRequest groupRequest);
    GroupRequest update(GroupRequest groupRequest);
    GroupRequest findOneById(Long id) throws ChangeSetPersister.NotFoundException;
    List<GroupRequest> findAll();
    List<GroupRequest> findAllByGroupId(Long groupId);
    GroupRequest findByUserAndGroup(User user, Group group);
}
