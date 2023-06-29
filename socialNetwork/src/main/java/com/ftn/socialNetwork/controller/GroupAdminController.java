package com.ftn.socialNetwork.controller;

import com.ftn.socialNetwork.model.entity.GroupAdmin;
import com.ftn.socialNetwork.service.GroupAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("api/groupadmins")
public class GroupAdminController {
  private GroupAdminService groupAdminService;

  @Autowired
  public GroupAdminController(GroupAdminService groupAdminService) {
    this.groupAdminService = groupAdminService;
  }

  @GetMapping("/all")
  public ResponseEntity<List<GroupAdmin>> getAllGroupAdmins() {
    List<GroupAdmin> groupAdmins = groupAdminService.findAll();
    return ResponseEntity.ok(groupAdmins);
  }
}
