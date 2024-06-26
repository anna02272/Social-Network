package com.ftn.socialNetwork.controller;

import com.ftn.socialNetwork.model.entity.Group;
import com.ftn.socialNetwork.model.entity.GroupAdmin;
import com.ftn.socialNetwork.model.entity.User;
import com.ftn.socialNetwork.service.intefraces.GroupAdminService;
import com.ftn.socialNetwork.service.intefraces.GroupService;
import com.ftn.socialNetwork.service.intefraces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("api/groupadmins")
public class GroupAdminController {
  @Autowired
  private GroupAdminService groupAdminService;

  @Autowired
  private GroupService groupService;

  @Autowired
  private UserService userService;

  @GetMapping("/all")
  public ResponseEntity<List<GroupAdmin>> getAllGroupAdmins() {
    List<GroupAdmin> groupAdmins = groupAdminService.findAll();
    return ResponseEntity.ok(groupAdmins);
  }
  @GetMapping("/findBy/{groupId}/{userId}")
  public ResponseEntity<GroupAdmin> findByGroupAndUser(@PathVariable Long groupId, @PathVariable Long userId) throws ChangeSetPersister.NotFoundException {
    Group group = groupService.findOneById(groupId);
    if (group == null) {
      return ResponseEntity.notFound().build();
    }
    User user = userService.findOneById(userId);
    if (user == null) {
      return ResponseEntity.notFound().build();
    }
    GroupAdmin groupAdmin = groupAdminService.findByGroupAndUser(group, user);
    if (groupAdmin == null) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(groupAdmin);
  }
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @DeleteMapping("/removeGroupAdmin/{groupId}")
  public ResponseEntity<GroupAdmin> removeGroupAdmin(@PathVariable Long groupId) throws ChangeSetPersister.NotFoundException {
    Group group = groupService.findOneById(groupId);
    if (group == null) {
      return ResponseEntity.notFound().build();
    }

    GroupAdmin groupAdmin = groupAdminService.findByGroup(group);
    if (groupAdmin == null) {
      return ResponseEntity.notFound().build();
    }
    groupAdminService.delete(groupAdmin);
    return ResponseEntity.ok(groupAdmin);
  }
  @PostMapping("/createGroupAdmin/{groupId}/{userId}")
  public ResponseEntity<GroupAdmin> createGroupAdmin(@PathVariable Long groupId,@PathVariable Long userId) throws ChangeSetPersister.NotFoundException {
    User user = userService.findOneById(userId);
    Group group = groupService.findOneById(groupId);

    if (groupAdminService.existsByGroupAndUser(group, user)) {
      return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    GroupAdmin groupAdmin = new GroupAdmin();
    groupAdmin.setGroup(group);
    groupAdmin.setUser(user);

    List<GroupAdmin> groupAdmins = new ArrayList<>();
    groupAdmins.add(groupAdmin);

    GroupAdmin createdGroupAdmin = groupAdminService.save(groupAdmin);

    return ResponseEntity.ok(createdGroupAdmin);
  }


}
