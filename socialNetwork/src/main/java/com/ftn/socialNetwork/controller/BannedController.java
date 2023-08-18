package com.ftn.socialNetwork.controller;

import com.ftn.socialNetwork.model.entity.*;
import com.ftn.socialNetwork.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("api/banned")
public class BannedController {

  @Autowired
  private BannedService bannedService;

  @Autowired
  private UserService userService;
  @Autowired
  private GroupService groupService;

  @Autowired
  private GroupAdminService groupAdminService;

  @PostMapping("/blockUser/{id}")
  public ResponseEntity<Banned> blockUser(@PathVariable("id") Long userId,@RequestBody Banned banned, Principal principal) throws ChangeSetPersister.NotFoundException {
    String username = principal.getName();
    User user = userService.findByUsername(username);
    banned.setUser(user);
    banned.setTimeStamp(LocalDate.now());
    banned.setBlocked(true);
    User bannedUser = userService.findOneById(userId);
    if (bannedUser == null) {
      return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }
    Banned existingBanned = bannedService.findExistingBanned(bannedUser);
    if (existingBanned != null) {
      return new ResponseEntity<>(HttpStatus.CONFLICT);
    }
    banned.setBannedUser(bannedUser);
    Banned created = bannedService.create(banned);

    return ResponseEntity.ok(created);
  }
  @PutMapping("/unblockUser/{id}")
  public ResponseEntity<Banned> unblockUser(@PathVariable Long id) throws ChangeSetPersister.NotFoundException {
    Banned existing = bannedService.findOneById(id);

    if (existing == null) {
      return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }
    existing.setBlocked(false);
    Banned updated = bannedService.update(existing);

    return new ResponseEntity<>(updated, HttpStatus.OK);
  }
  @GetMapping("/allBlockedUsers")
  public ResponseEntity<List<Banned>> getAllBlockedUsers() {
    List<Banned> banns = bannedService.getAllBlockedUsers(true);
    return ResponseEntity.ok(banns);
  }
  @PostMapping("/blockGroupUser/{userId}/{groupId}")
  public ResponseEntity<Banned> blockGroupUser(@PathVariable("userId") Long userId, @PathVariable("groupId") Long groupId,
                                               @RequestBody Banned banned, Principal principal) throws ChangeSetPersister.NotFoundException {
    String username = principal.getName();
    GroupAdmin user = groupAdminService.findByUsername(username);
    banned.setGroupAdmin(user);
    banned.setTimeStamp(LocalDate.now());
    banned.setBlocked(true);
    User bannedUser = userService.findOneById(userId);
    if (bannedUser == null) {
      return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }
    Group group = groupService.findOneById(groupId);
    if (group == null) {
      return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }
    Banned existingBanned = bannedService.findExistingBanned(bannedUser);
    if (existingBanned != null) {
      return new ResponseEntity<>(HttpStatus.CONFLICT);
    }
    banned.setBannedUser(bannedUser);
    banned.setGroup(group);
    Banned created = bannedService.create(banned);

    return ResponseEntity.ok(created);
  }
  @GetMapping("/allBlockedGroupUsers/{groupId}")
  public ResponseEntity<List<Banned>> getAllBlockedGroupUsers( @PathVariable("groupId") Long groupId) {
    List<Banned> banns = bannedService.getAllBlockedUsersByGroupId(true, groupId);
    return ResponseEntity.ok(banns);
  }
}

