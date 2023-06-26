package com.ftn.socialNetwork.controller;

import com.ftn.socialNetwork.model.entity.Group;
import com.ftn.socialNetwork.model.entity.GroupAdmin;
import com.ftn.socialNetwork.model.entity.Post;
import com.ftn.socialNetwork.model.entity.User;
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
    private GroupService groupService;


    @Autowired
    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @Autowired
    UserService userService;


    @Autowired
    GroupAdminService groupAdminService;

    @Autowired
    TokenUtils tokenUtils;

    @GetMapping("/whoami")
    public User user(Principal user) {

        return this.userService.findByUsername(user.getName());
    }

@PostMapping("/create")
public ResponseEntity<Group> createGroup(@RequestBody Group group, Principal principal) {
    String username = principal.getName();
    User user = userService.findByUsername(username);

    group.setCreationDate(LocalDateTime.now());

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

        Group updatedGroup = groupService.updateGroup(existingGroup);

        return new ResponseEntity<>(updatedGroup, HttpStatus.OK);
    }





    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteGroup(@PathVariable Long id) {
        groupService.deleteGroup(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<Group> getGroupById(@PathVariable Long id) throws ChangeSetPersister.NotFoundException {
        Group group = groupService.findOneById(id);
        return ResponseEntity.ok(group);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Group>> getAllGroups() {
        List<Group> groups = groupService.findAll();
        return ResponseEntity.ok(groups);
    }
}
