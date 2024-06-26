package com.ftn.socialNetwork.service.intefraces;

import com.ftn.socialNetwork.model.entity.Group;
import com.ftn.socialNetwork.model.entity.GroupRequest;
import com.ftn.socialNetwork.model.entity.User;
import org.springframework.data.crossstore.ChangeSetPersister;

import java.util.List;
import java.util.Set;

public interface GroupRequestService {
    GroupRequest create(GroupRequest groupRequest);
    GroupRequest update(GroupRequest groupRequest);
    GroupRequest findOneById(Long id) throws ChangeSetPersister.NotFoundException;
    List<GroupRequest> findAll();
    List<GroupRequest> findAllByGroupId(Long groupId);
    GroupRequest findByUserAndGroup(User user, Group group);
  Set<Group> getApprovedGroupsForUser(User user);
  Set<User> getGroupMembers(Group group);
}
