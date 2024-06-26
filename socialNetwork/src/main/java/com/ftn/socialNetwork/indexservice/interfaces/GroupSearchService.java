package com.ftn.socialNetwork.indexservice.interfaces;

import com.ftn.socialNetwork.indexmodel.GroupIndex;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface GroupSearchService {
    Page<GroupIndex> nameSearch(List<String> keywords, Pageable pageable);
    Page<GroupIndex> descriptionSearch(List<String> keywords, Pageable pageable);

//    Page<GroupIndex> advancedSearch(List<String> expression, Pageable pageable);
}
