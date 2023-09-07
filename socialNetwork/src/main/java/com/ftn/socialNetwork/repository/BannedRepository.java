package com.ftn.socialNetwork.repository;

import com.ftn.socialNetwork.model.entity.Banned;
import com.ftn.socialNetwork.model.entity.Group;
import com.ftn.socialNetwork.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BannedRepository extends JpaRepository<Banned, Long> {
  List<Banned> findAllByIsBlocked(boolean isBlocked);
  Banned findByBannedUserAndIsBlockedAndGroup(User bannedUser, boolean isBlocked, Group group);
  List<Banned> findAllByIsBlockedAndGroupId(boolean isBlocked, Long groupId);
  List<Banned> findAllByIsBlockedAndGroup(boolean isBlocked, Group group);

}
