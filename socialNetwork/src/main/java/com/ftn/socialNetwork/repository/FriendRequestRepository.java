package com.ftn.socialNetwork.repository;

import com.ftn.socialNetwork.model.entity.FriendRequest;
import com.ftn.socialNetwork.model.entity.GroupRequest;
import com.ftn.socialNetwork.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {
  FriendRequest findByForUserAndFromUser(User forUser, User fromUser);
  List<FriendRequest> findByForUserAndApproved(User forUser, boolean approved);
  List<FriendRequest> findByFromUserAndApproved(User fromUser, boolean approved);
  List<FriendRequest> findAllByForUserId(Long forUserId);
}
