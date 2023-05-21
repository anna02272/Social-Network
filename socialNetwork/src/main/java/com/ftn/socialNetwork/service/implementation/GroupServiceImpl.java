package com.ftn.socialNetwork.service.implementation;

import com.ftn.socialNetwork.model.entity.Group;
import com.ftn.socialNetwork.repository.GroupRepository;
import com.ftn.socialNetwork.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupServiceImpl implements GroupService {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    public GroupServiceImpl(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

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
}
