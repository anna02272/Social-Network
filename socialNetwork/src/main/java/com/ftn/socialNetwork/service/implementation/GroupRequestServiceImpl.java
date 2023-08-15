package com.ftn.socialNetwork.service.implementation;


import com.ftn.socialNetwork.model.entity.Group;
import com.ftn.socialNetwork.model.entity.GroupRequest;
import com.ftn.socialNetwork.model.entity.User;
import com.ftn.socialNetwork.repository.GroupRequestRepository;
import com.ftn.socialNetwork.service.GroupRequestService;
import com.ftn.socialNetwork.service.GroupService;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class GroupRequestServiceImpl implements GroupRequestService {
  @Autowired
  private GroupRequestRepository groupRequestRepository;

  @Override
  public GroupRequest create(GroupRequest groupRequest) {
    try {
      return groupRequestRepository.save(groupRequest);
    } catch (IllegalArgumentException e) {
      return null;
    }
  }
  @Override
  public GroupRequest update(GroupRequest groupRequest) {
    return groupRequestRepository.save(groupRequest);
  }
  @Override
  public GroupRequest findOneById(Long id) throws ChangeSetPersister.NotFoundException {
    return groupRequestRepository.findById(id)
      .orElseThrow(() -> new ChangeSetPersister.NotFoundException());
  }
  @Override
  public List<GroupRequest> findAll() {
    return groupRequestRepository.findAll();
  }

  @Override
  public List<GroupRequest> findAllByGroupId(Long groupId) {
    return groupRequestRepository.findAllByGroupId(groupId);
  }

  @Override
  public GroupRequest findByUserAndGroup(User user, Group group){
    return groupRequestRepository.findByUserAndGroup(user, group);
  }
  @Override
  public Set<Group> getApprovedGroupsForUser(User user) {
    List<GroupRequest> approvedGroupRequests = groupRequestRepository.findByUserAndApproved(user, true);
    Set<Group> approvedGroups = approvedGroupRequests.stream()
      .map(GroupRequest::getGroup)
      .collect(Collectors.toSet());
    return approvedGroups;
  }

}
