package com.ftn.socialNetwork.controller;

import com.ftn.socialNetwork.model.entity.*;
import com.ftn.socialNetwork.security.TokenUtils;
import com.ftn.socialNetwork.service.GroupAdminService;
import com.ftn.socialNetwork.service.GroupService;
import com.ftn.socialNetwork.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@RestController
@RequestMapping("api/groups")
public class GroupController {
  @Autowired
    private GroupService groupService;

    @Autowired
    UserService userService;

    @Autowired
    GroupAdminService groupAdminService;


@PostMapping("/create")
public ResponseEntity<Group> createGroup(@RequestBody Group group, Principal principal) {
  if (groupService.existsByName(group.getName())) {
    return new ResponseEntity<>(null, HttpStatus.CONFLICT);
  }
    String username = principal.getName();
    User user = userService.findByUsername(username);

    group.setCreationDate(LocalDateTime.now());
   group.setIsDeleted(false);
    Group createdGroup = groupService.createGroup(group);

    GroupAdmin groupAdmin = new GroupAdmin();
    groupAdmin.setGroup(createdGroup);
    groupAdmin.setUser(user);

    GroupAdmin createdGroupAdmin = groupAdminService.save(groupAdmin);

  return ResponseEntity.ok(createdGroup);
}
    @PutMapping("/update/{id}")
    public ResponseEntity<Group> updateGroup(@PathVariable("id") Long groupId, @RequestBody Group group) throws ChangeSetPersister.NotFoundException {
        Group existingGroup = groupService.findOneById(groupId);

      if (groupService.existsByName(group.getName())) {
        return new ResponseEntity<>(null, HttpStatus.CONFLICT);
      }
        if (existingGroup == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        if (group.getName() != null) {
            existingGroup.setName(group.getName());
        }
        if (group.getDescription() != null) {
            existingGroup.setDescription(group.getDescription());
        }
        if (group.isSuspended() != existingGroup.isSuspended()) {
            existingGroup.setSuspended(group.isSuspended());
        }
        if (group.getSuspendedReason() != null) {
            existingGroup.setSuspendedReason(group.getSuspendedReason());
        }
      if (group.getIsDeleted()!= null){
        existingGroup.setIsDeleted(group.getIsDeleted());
      }

        Group updatedGroup = groupService.updateGroup(existingGroup);

        return new ResponseEntity<>(updatedGroup, HttpStatus.OK);
    }





    @PreAuthorize("hasRole('ROLE_ADMIN')")
//    @DeleteMapping("/delete/{id}")
//    public ResponseEntity<Void> deleteGroup(@PathVariable Long id) {
//        groupService.deleteGroup(id);
//        return ResponseEntity.noContent().build();
//    }
    @PutMapping("/delete/{id}")
    public ResponseEntity<Group> delete(@PathVariable Long id) throws ChangeSetPersister.NotFoundException {
      Group existing = groupService.findOneById(id);

      if (existing == null) {
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
      }

      existing.setIsDeleted(true);
      Group updated = groupService.updateGroup(existing);

      return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<Group> getGroupById(@PathVariable Long id) throws ChangeSetPersister.NotFoundException {
        Group group = groupService.findOneById(id);
        return ResponseEntity.ok(group);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Group>> getAllGroups() {
        List<Group> groups = groupService.findAllByIsDeleted(false);
        return ResponseEntity.ok(groups);
    }
}
