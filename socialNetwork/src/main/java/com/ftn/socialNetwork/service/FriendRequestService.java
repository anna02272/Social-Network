package com.ftn.socialNetwork.service;

import com.ftn.socialNetwork.model.entity.FriendRequest;
import com.ftn.socialNetwork.model.entity.Group;
import com.ftn.socialNetwork.model.entity.User;
import org.springframework.data.crossstore.ChangeSetPersister;

import java.util.List;
import java.util.Set;

public interface FriendRequestService {
  FriendRequest create(FriendRequest friendRequest);
  FriendRequest update(FriendRequest friendRequest);
  FriendRequest findOneById(Long id) throws ChangeSetPersister.NotFoundException;
  List<FriendRequest> findAll();
  List<FriendRequest> findAllByUserId(Long userId);
  FriendRequest findByForUserAndFromUser(User forUser, User fromUser);
  Set<User> getFriendsForUser(User user);

  }

