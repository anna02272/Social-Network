package com.ftn.socialNetwork.service.implementation;

import com.ftn.socialNetwork.indexmodel.GroupIndex;
import com.ftn.socialNetwork.indexservice.interfaces.GroupIndexingService;
import com.ftn.socialNetwork.model.entity.Group;
import com.ftn.socialNetwork.repository.GroupRepository;
import com.ftn.socialNetwork.service.intefraces.GroupService;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.List;

@Service
public class GroupServiceImpl implements GroupService {
    private final GroupRepository groupRepository;
    private final GroupIndexingService groupIndexService;
    private final EntityManager entityManager;

    @Autowired
    public GroupServiceImpl(GroupRepository groupRepository, GroupIndexingService groupIndexService, EntityManager entityManager) {
        this.groupRepository = groupRepository;
        this.groupIndexService = groupIndexService;
        this.entityManager = entityManager;
    }

    @Override
    public Group createGroup(Group group) {
        try {
            Group createdGroup = groupRepository.save(group);
            indexGroup(createdGroup);
            return createdGroup;
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    @Override
    public Group updateGroup(Group group) {
        Group updatedGroup = groupRepository.save(group);
        indexGroup(updatedGroup);
        return updatedGroup;
    }

    @Override
    public Group deleteGroup(Long id) {
      groupRepository.deleteById(id);
      groupIndexService.deleteById(id.toString());
      return null;
    }

   @Override
   public Group findOneById(Long id) throws ChangeSetPersister.NotFoundException {
     return groupRepository.findById(id)
      .orElseThrow(ChangeSetPersister.NotFoundException::new);
   }

   @Override
   public List<Group> findAll() {
        return groupRepository.findAll();
    }

  public List<Group> findAllByIsSuspended(boolean isSuspended) {
        try (Session session = entityManager.unwrap(Session.class)) {
            Filter filter = session.enableFilter("suspendedGroupFilter");
            filter.setParameter("isSuspended", isSuspended);
            List<Group> groups = groupRepository.findAll();
            session.disableFilter("suspendedGroupFilter");
            return groups;
        }
  }
  public boolean existsByName(String name) {
    return groupRepository.existsByName(name);
  }
  @Override
  public List<Group> findAllByIsDeletedWithGroupAdmins(boolean isDeleted) {
    return groupRepository.findAllByIsSuspendedWithGroupAdmins(isDeleted);
  }
  private void indexGroup(Group group) {
      GroupIndex groupIndex = new GroupIndex();
      groupIndex.setId(group.getId().toString());
      groupIndex.setName(group.getName());
      groupIndex.setDescription(group.getDescription());
      groupIndex.setCreationDate(group.getCreationDate());
      groupIndex.setSuspended(group.isSuspended());
      groupIndex.setSuspendedReason(group.getSuspendedReason());
      groupIndexService.save(groupIndex);
  }
}
