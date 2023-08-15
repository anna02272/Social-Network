package com.ftn.socialNetwork.controller;

import com.ftn.socialNetwork.model.entity.Group;
import com.ftn.socialNetwork.model.entity.GroupRequest;
import com.ftn.socialNetwork.model.entity.User;
import com.ftn.socialNetwork.service.GroupRequestService;
import com.ftn.socialNetwork.service.GroupService;
import com.ftn.socialNetwork.service.UserService;
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
@RequestMapping("api/groupRequests")
public class GroupRequestController {
  @Autowired
  private GroupRequestService groupRequestService;

  @Autowired
  private UserService userService;

  @Autowired
  private GroupService groupService;

  @PostMapping("/create/{id}")
  public ResponseEntity<GroupRequest> create(@PathVariable("id") Long groupId,@RequestBody GroupRequest groupRequest, Principal principal) throws ChangeSetPersister.NotFoundException {
    String username = principal.getName();
    User user = userService.findByUsername(username);
    Group group = groupService.findOneById(groupId);
    GroupRequest existingGroupRequest = groupRequestService.findByUserAndGroup(user, group);
    if (existingGroupRequest != null) {
      return ResponseEntity.badRequest().body(null);
    }
    groupRequest.setUser(user);
    groupRequest.setCreated_at(LocalDateTime.now());
    groupRequest.setApproved(false);
    groupRequest.setGroup(group);
    GroupRequest created = groupRequestService.create(groupRequest);

    return ResponseEntity.ok(created);
  }

  @PutMapping("/approve/{id}")
  public ResponseEntity<GroupRequest> approve(@PathVariable Long id) throws ChangeSetPersister.NotFoundException {
    GroupRequest existing = groupRequestService.findOneById(id);

    if (existing == null) {
      return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }
    existing.setAt(LocalDateTime.now());
    existing.setApproved(true);
    GroupRequest updated = groupRequestService.update(existing);

    return new ResponseEntity<>(updated, HttpStatus.OK);
  }
  @PutMapping("/decline/{id}")
  public ResponseEntity<GroupRequest> decline(@PathVariable Long id) throws ChangeSetPersister.NotFoundException {
    GroupRequest existing = groupRequestService.findOneById(id);

    if (existing == null) {
      return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }
    existing.setAt(LocalDateTime.now());
    existing.setApproved(false);
    GroupRequest updated = groupRequestService.update(existing);

    return new ResponseEntity<>(updated, HttpStatus.OK);
  }

  @GetMapping("/find/{id}")
  public ResponseEntity<GroupRequest> getGroupRequestById(@PathVariable Long id) throws ChangeSetPersister.NotFoundException {
    GroupRequest groupRequest = groupRequestService.findOneById(id);
    return ResponseEntity.ok(groupRequest);
  }

  @GetMapping("/all")
  public ResponseEntity<List<GroupRequest>> getAllGroups() {
    List<GroupRequest> groupRequests = groupRequestService.findAll();
    return ResponseEntity.ok(groupRequests);
  }
  @GetMapping("/all/{groupId}")
  public ResponseEntity<List<GroupRequest>> getByGroup(@PathVariable Long groupId) {
    List<GroupRequest> groupRequests = groupRequestService.findAllByGroupId(groupId);
    return ResponseEntity.ok(groupRequests);
  }
  @GetMapping("/find/{userId}/{groupId}")
  public ResponseEntity<GroupRequest> getByUserAndGroup(@PathVariable Long userId, @PathVariable Long groupId) throws ChangeSetPersister.NotFoundException {
    User user = userService.findOneById(userId);
    Group group = groupService.findOneById(groupId);

    GroupRequest groupRequest = groupRequestService.findByUserAndGroup(user, group);
    return ResponseEntity.ok(groupRequest);
  }
  @GetMapping("/approvedGroups/{userId}")
  public ResponseEntity<Set<Group>> getApprovedGroupsForUser(@PathVariable Long userId) {
    User user = userService.findOneById(userId);
    Set<Group> approvedGroups = groupRequestService.getApprovedGroupsForUser(user);
    return ResponseEntity.ok(approvedGroups);
  }

}
