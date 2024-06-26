package com.ftn.socialNetwork.indexservice.interfaces;

import com.ftn.socialNetwork.indexmodel.GroupIndex;
import org.springframework.web.multipart.MultipartFile;

public interface GroupIndexingService {

    GroupIndex save(GroupIndex groupIndex);

    void deleteById(String id);

    String indexDocument(MultipartFile documentFile);
}
