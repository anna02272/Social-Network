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

     void updateGroupIndex(Group group);

    void suspendGroupIndex(Group group);

    void updatePostCount(String groupId);

    void deletePostCount(String groupId);

    void updateLikeCount(String groupId);
    void deleteLikeCount(String groupId);
}
