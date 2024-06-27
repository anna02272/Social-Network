package com.ftn.socialNetwork.indexservice.interfaces;

import com.ftn.socialNetwork.indexmodel.GroupIndex;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface GroupSearchService {
    Page<GroupIndex> nameAndDescriptionSearch(List<String> keywords, Pageable pageable);

//    Page<GroupIndex> advancedSearch(List<String> expression, Pageable pageable);
}
