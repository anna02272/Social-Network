package com.ftn.socialNetwork.controller;

import com.ftn.socialNetwork.model.entity.*;
import com.ftn.socialNetwork.service.intefraces.FriendRequestService;
import com.ftn.socialNetwork.service.intefraces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("api/friendRequests")
public class FriendRequestController {
  @Autowired
  private FriendRequestService friendRequestService;
  @Autowired
  private UserService userService;

  @PostMapping("/create/{id}")
  public ResponseEntity<FriendRequest> create(@PathVariable("id") Long userId,@RequestBody FriendRequest friendRequest, Principal principal) throws ChangeSetPersister.NotFoundException {
    String username = principal.getName();
    User fromUser = userService.findByUsername(username);
    User forUser = userService.findOneById(userId);
    if (fromUser.equals(forUser)) {
      return ResponseEntity.badRequest().body(null);
    }
    FriendRequest existingFriendRequest = friendRequestService.findByForUserAndFromUser(forUser, fromUser);
    if (existingFriendRequest != null) {
      return ResponseEntity.badRequest().body(null);
    }
    friendRequest.setFromUser(fromUser);
    friendRequest.setCreated_at(LocalDateTime.now());
    friendRequest.setApproved(false);
    friendRequest.setForUser(forUser);
    FriendRequest created = friendRequestService.create(friendRequest);
    return ResponseEntity.ok(created);
  }
  @PutMapping("/approve/{id}")
  public ResponseEntity<FriendRequest> approve(@PathVariable Long id) throws ChangeSetPersister.NotFoundException {
    FriendRequest existing = friendRequestService.findOneById(id);
    if (existing == null) {
      return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }
    existing.setAt(LocalDateTime.now());
    existing.setApproved(true);


    FriendRequest updated = friendRequestService.update(existing);
    return new ResponseEntity<>(updated, HttpStatus.OK);
  }
  @PutMapping("/decline/{id}")
  public ResponseEntity<FriendRequest> decline(@PathVariable Long id) throws ChangeSetPersister.NotFoundException {
    FriendRequest existing = friendRequestService.findOneById(id);

    if (existing == null) {
      return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }
    existing.setAt(LocalDateTime.now());
    existing.setApproved(false);
    FriendRequest updated = friendRequestService.update(existing);

    return new ResponseEntity<>(updated, HttpStatus.OK);
  }
  @GetMapping("/friends/{userId}")
  public ResponseEntity<Set<User>> getApprovedFriendsForUser(@PathVariable Long userId) {
    User user = userService.findOneById(userId);
    Set<User> friends = friendRequestService.getFriendsForUser(user);
    return ResponseEntity.ok(friends);
  }
  @GetMapping("/find/{id}")
  public ResponseEntity<FriendRequest> getFriendRequestById(@PathVariable Long id) throws ChangeSetPersister.NotFoundException {
    FriendRequest friendRequest = friendRequestService.findOneById(id);
    return ResponseEntity.ok(friendRequest);
  }
  @GetMapping("/all/{userId}")
  public ResponseEntity<List<FriendRequest>> getByUser(@PathVariable Long userId) {
    List<FriendRequest> friendRequests = friendRequestService.findAllByUserId(userId);
    return ResponseEntity.ok(friendRequests);
  }

  @GetMapping("/all")
  public ResponseEntity<List<FriendRequest>> getAll() {
    List<FriendRequest> friendRequests = friendRequestService.findAll();
    return ResponseEntity.ok(friendRequests);
  }
  @GetMapping("/search")
  public ResponseEntity<List<User>> searchUsers(@RequestParam String keyword) {
    String[] nameParts = keyword.split(" ");
    if (nameParts.length < 2) {
      List<User> foundUsers = userService.searchUsersByNameOrLastName(keyword);
      return ResponseEntity.ok(foundUsers);
    } else {
      List<User> foundUsers = userService.searchUsersByFirstNameAndLastName(nameParts[0], nameParts[1]);
      return ResponseEntity.ok(foundUsers);
    }
  }
  }
