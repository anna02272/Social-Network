package com.ftn.socialNetwork.repository;

import com.ftn.socialNetwork.model.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GroupAdminRepository extends JpaRepository<GroupAdmin, Long> {

  GroupAdmin findByGroupAndUser(Group group, User user);
  GroupAdmin findByGroup(Group group);
  boolean existsByGroupAndUser(Group group, User user);
}
