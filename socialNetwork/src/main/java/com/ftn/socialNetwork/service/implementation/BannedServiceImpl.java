package com.ftn.socialNetwork.service.implementation;

import com.ftn.socialNetwork.model.entity.Banned;
import com.ftn.socialNetwork.model.entity.User;
import com.ftn.socialNetwork.repository.BannedRepository;
import com.ftn.socialNetwork.service.BannedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class BannedServiceImpl implements BannedService {

  @Autowired
  private BannedRepository bannedRepository;

  @Override
  public Banned create(Banned banned) {
    try {
      return bannedRepository.save(banned);
    } catch (IllegalArgumentException e) {
      return null;
    }
  }
  @Override
  public Banned update(Banned banned) {
    return bannedRepository.save(banned);
  }

  @Override
  public Banned findOneById(Long id) throws ChangeSetPersister.NotFoundException {
    return bannedRepository.findById(id)
      .orElseThrow(() -> new ChangeSetPersister.NotFoundException());
  }
  @Override
  public List<Banned> getAllBlockedUsers(boolean isBlocked) {
    return bannedRepository.findAllByIsBlocked(isBlocked);
  }

  @Override
  public Banned findExistingBanned(User bannedUser) {
    return bannedRepository.findByBannedUserAndIsBlocked(bannedUser, true);
  }
  @Override
  public List<Banned> getAllBlockedUsersByGroupId(boolean isBlocked, Long groupId){
    return bannedRepository.findAllByIsBlockedAndGroupId(isBlocked, groupId);
  }
}
