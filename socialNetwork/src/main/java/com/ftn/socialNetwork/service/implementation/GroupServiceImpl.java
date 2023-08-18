package com.ftn.socialNetwork.service.implementation;

import com.ftn.socialNetwork.model.entity.Comment;
import com.ftn.socialNetwork.model.entity.Group;
import com.ftn.socialNetwork.model.entity.GroupAdmin;
import com.ftn.socialNetwork.model.entity.Post;
import com.ftn.socialNetwork.repository.GroupRepository;
import com.ftn.socialNetwork.service.GroupService;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.List;

@Service
public class GroupServiceImpl implements GroupService {

    @Autowired
    private GroupRepository groupRepository;

    @Override
    public Group createGroup(Group group) {
        try {
            return groupRepository.save(group);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }


    @Override
    public Group updateGroup(Group group) {
        return groupRepository.save(group);
    }

    @Override
    public Group deleteGroup(Long id) {
      groupRepository.deleteById(id);
        return null;
    }

  @Override
  public Group findOneById(Long id) throws ChangeSetPersister.NotFoundException {
    return groupRepository.findById(id)
      .orElseThrow(() -> new ChangeSetPersister.NotFoundException());
  }

    @Override
    public List<Group> findAll() {
        return groupRepository.findAll();
    }

  @Autowired
  private EntityManager entityManager;

  public List<Group> findAllByIsSuspended(boolean isSuspended) {
    Session session = entityManager.unwrap(Session.class);
    Filter filter = session.enableFilter("suspendedGroupFilter");
    filter.setParameter("isSuspended", isSuspended);
    List<Group> groups = groupRepository.findAll();
    session.disableFilter("suspendedGroupFilter");
    return groups;
  }
  public boolean existsByName(String name) {
    return groupRepository.existsByName(name);
  }
  @Override
  public List<Group> findAllByIsDeletedWithGroupAdmins(boolean isDeleted) {
    return groupRepository.findAllByIsSuspendedWithGroupAdmins(isDeleted);
  }

}
