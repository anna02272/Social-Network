package com.ftn.socialNetwork.repository;

import com.ftn.socialNetwork.model.entity.Group;
import com.ftn.socialNetwork.model.entity.GroupRequest;
import com.ftn.socialNetwork.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupRequestRepository extends JpaRepository<GroupRequest, Long> {
  List<GroupRequest> findAllByGroupId(Long groupId);
  GroupRequest findByUserAndGroup(User user, Group group);
  List<GroupRequest> findByUserAndApproved(User user, boolean approved);
  List<GroupRequest> findByGroupAndApproved(Group group, boolean approved);
}
