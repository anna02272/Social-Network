package com.ftn.socialNetwork.indexservice.interfaces;

import com.ftn.socialNetwork.indexmodel.GroupIndex;
import com.ftn.socialNetwork.model.entity.Group;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface GroupIndexingService {

    GroupIndex save(GroupIndex groupIndex);

    void deleteById(String id);

    void indexGroup(MultipartFile documentFile, Group group);
}
