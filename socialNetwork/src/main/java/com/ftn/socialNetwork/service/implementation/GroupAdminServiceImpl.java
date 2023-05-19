package com.ftn.socialNetwork.service.implementation;

import com.ftn.socialNetwork.model.entity.GroupAdmin;
import com.ftn.socialNetwork.model.entity.User;
import com.ftn.socialNetwork.repository.GroupAdminRepository;
import com.ftn.socialNetwork.service.GroupAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


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

}
