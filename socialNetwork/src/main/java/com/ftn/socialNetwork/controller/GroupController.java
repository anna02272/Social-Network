package com.ftn.socialNetwork.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ftn.socialNetwork.dto.DocumentFileResponseDTO;
import com.ftn.socialNetwork.indexservice.interfaces.GroupIndexingService;
import com.ftn.socialNetwork.model.entity.*;
import com.ftn.socialNetwork.service.intefraces.GroupAdminService;
import com.ftn.socialNetwork.service.intefraces.GroupService;
import com.ftn.socialNetwork.service.intefraces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
public ResponseEntity<Group> createGroup (@RequestParam("group") String groupJson,
                                          @RequestParam(value = "pdfFile", required = false) MultipartFile pdfFile,
                                          Principal principal) throws JsonProcessingException {
    Group group = new ObjectMapper().readValue(groupJson, Group.class);

    if (groupService.existsByName(group.getName())) {
    return new ResponseEntity<>(null, HttpStatus.CONFLICT);
  }
    String username = principal.getName();
    User user = userService.findByUsername(username);

    group.setCreationDate(LocalDateTime.now());
    group.setSuspended(false);
    Group createdGroup = groupService.createGroup(group, pdfFile);

    GroupAdmin groupAdmin = new GroupAdmin();
    groupAdmin.setGroup(createdGroup);
    groupAdmin.setUser(user);

    List<GroupAdmin> groupAdmins = new ArrayList<>();
    groupAdmins.add(groupAdmin);
    createdGroup.setGroupAdmin(groupAdmins);

    GroupAdmin createdGroupAdmin = groupAdminService.save(groupAdmin);

  return ResponseEntity.ok(createdGroup);
}
    @PutMapping("/update/{id}")
    public ResponseEntity<Group> updateGroup(@PathVariable("id") Long groupId, @RequestBody Group group) throws ChangeSetPersister.NotFoundException {
        Group existingGroup = groupService.findOneById(groupId);

        if (existingGroup == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
      if (!existingGroup.getName().equals(group.getName()) && groupService.existsByName(group.getName())) {
        return new ResponseEntity<>(null, HttpStatus.CONFLICT);
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
      if (group.isSuspended()){
        existingGroup.setSuspended(group.isSuspended());
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
    public ResponseEntity<Group> delete(@PathVariable Long id, @RequestBody String suspendedReason) throws ChangeSetPersister.NotFoundException {
      Group existing = groupService.findOneById(id);

      if (existing == null) {
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
      }
      existing.setSuspended(true);
      existing.setSuspendedReason(suspendedReason);
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
        List<Group> groups = groupService.findAllByIsSuspended(false);
        return ResponseEntity.ok(groups);
    }

}
