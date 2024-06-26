package com.ftn.socialNetwork.service.implementation;

import com.ftn.socialNetwork.model.entity.FriendRequest;
import com.ftn.socialNetwork.model.entity.User;
import com.ftn.socialNetwork.repository.FriendRequestRepository;
import com.ftn.socialNetwork.service.intefraces.FriendRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class FriendRequestServiceImpl implements FriendRequestService {
  @Autowired
  private FriendRequestRepository friendRequestRepository;

  @Override
  public FriendRequest create(FriendRequest friendRequest) {
    try {
      return friendRequestRepository.save(friendRequest);
    } catch (IllegalArgumentException e) {
      return null;
    }
  }

  @Override
  public FriendRequest update(FriendRequest friendRequest) {
    return friendRequestRepository.save(friendRequest);
  }

  @Override
  public FriendRequest findOneById(Long id) throws ChangeSetPersister.NotFoundException {
    return friendRequestRepository.findById(id)
      .orElseThrow(() -> new ChangeSetPersister.NotFoundException());
  }

  @Override
  public List<FriendRequest> findAll() {
    return friendRequestRepository.findAll();
  }
  @Override
  public List<FriendRequest> findAllByUserId(Long userId) {
    return friendRequestRepository.findAllByForUserId(userId);
  }

  @Override
  public FriendRequest findByForUserAndFromUser(User forUser, User fromUser){
    return friendRequestRepository.findByForUserAndFromUser(forUser, fromUser);
  }
//  @Override
//  public Set<User> getFriendsForUser(User forUser) {
//    List<FriendRequest> approvedFriendRequests = friendRequestRepository.findByForUserAndApproved(forUser, true);
//    Set<User> friends = approvedFriendRequests.stream()
//      .map(FriendRequest::getFromUser)
//      .collect(Collectors.toSet());
//    return friends;
//  }
@Override
public Set<User> getFriendsForUser(User forUser) {
  List<FriendRequest> approvedFriendRequests = friendRequestRepository.findByForUserAndApproved(forUser, true);
  Set<User> friends = new HashSet<>();

  for (FriendRequest friendRequest : approvedFriendRequests) {
    User fromUser = friendRequest.getFromUser();
    User requestUser = friendRequest.getForUser();

    if (fromUser.equals(forUser)) {
      friends.add(requestUser);
    } else {
      friends.add(fromUser);
    }
  }
  List<FriendRequest> initiatedFriendRequests = friendRequestRepository.findByFromUserAndApproved(forUser, true);
  for (FriendRequest friendRequest : initiatedFriendRequests) {
    User targetUser = friendRequest.getForUser();
    friends.add(targetUser);
  }

  return friends;
}


}
