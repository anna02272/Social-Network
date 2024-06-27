package com.ftn.socialNetwork.service.implementation;

import com.ftn.socialNetwork.indexmodel.GroupIndex;
import com.ftn.socialNetwork.indexservice.interfaces.FileService;
import com.ftn.socialNetwork.indexservice.interfaces.GroupIndexingService;
import com.ftn.socialNetwork.model.entity.Group;
import com.ftn.socialNetwork.repository.GroupRepository;
import com.ftn.socialNetwork.service.intefraces.GroupService;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.UUID;

@Service
public class GroupServiceImpl implements GroupService {
    private final GroupRepository groupRepository;
    private final GroupIndexingService groupIndexService;
    private final FileService fileService;
    private final EntityManager entityManager;

    @Autowired
    public GroupServiceImpl(GroupRepository groupRepository, GroupIndexingService groupIndexService, FileService fileService, EntityManager entityManager) {
        this.groupRepository = groupRepository;
        this.groupIndexService = groupIndexService;
        this.fileService = fileService;
        this.entityManager = entityManager;
    }

    @Override
    public Group createGroup(Group group, MultipartFile pdfFile) {
        Group createdGroup = groupRepository.save(group);
        groupIndexService.indexGroup(pdfFile, createdGroup);
        return createdGroup;
    }

    @Override
    public Group updateGroup(Group group) {
        Group updatedGroup = groupRepository.save(group);
//        indexGroup(updatedGroup);
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
}
