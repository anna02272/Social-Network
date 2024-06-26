package com.ftn.socialNetwork.service.implementation;

import com.ftn.socialNetwork.model.entity.*;
import com.ftn.socialNetwork.repository.GroupAdminRepository;
import com.ftn.socialNetwork.service.intefraces.GroupAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class GroupAdminServiceImpl implements GroupAdminService {

    @Autowired
    GroupAdminRepository groupAdminRepository;
    @Override
    public GroupAdmin save(GroupAdmin groupAdmin) {
        try{
            return groupAdminRepository.save(groupAdmin);
        }catch (IllegalArgumentException e){
            return null;
        }
    }
  @Override
  public GroupAdmin findOneById(Long id) throws ChangeSetPersister.NotFoundException {
    return groupAdminRepository.findById(id)
      .orElseThrow(() -> new ChangeSetPersister.NotFoundException());
  }

  @Override
  public List<GroupAdmin> findAll() {
    return groupAdminRepository.findAll();
  }

  @Override
  public GroupAdmin findByGroupAndUser(Group group, User user) {
    return groupAdminRepository.findByGroupAndUser(group, user);
  }
  @Override
  public GroupAdmin findByGroup(Group group) {
    return groupAdminRepository.findByGroup(group);
  }
  @Override
  public boolean existsByGroupAndUser(Group group, User user) {
    return groupAdminRepository.existsByGroupAndUser(group, user);
  }
  @Override
  public GroupAdmin delete(GroupAdmin groupAdmin) {
    groupAdminRepository.delete(groupAdmin);
    return null;
  }

}
