package com.ftn.socialNetwork.service;

import com.ftn.socialNetwork.model.entity.Banned;
import com.ftn.socialNetwork.model.entity.Group;
import com.ftn.socialNetwork.model.entity.User;
import org.springframework.data.crossstore.ChangeSetPersister;

import java.util.List;

public interface BannedService {
  Banned create(Banned banned);
  Banned update(Banned banned);
  Banned findOneById(Long id) throws ChangeSetPersister.NotFoundException;
  List<Banned> getAllBlockedUsers(boolean isBlocked);
  List<Banned> getAllBlockedUsersByGroupId(boolean isBlocked, Long groupId);
  Banned findExistingBanned(User bannedUser);;
}
