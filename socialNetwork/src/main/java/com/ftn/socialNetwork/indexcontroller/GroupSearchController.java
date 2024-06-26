package com.ftn.socialNetwork.indexcontroller;

import com.ftn.socialNetwork.indexmodel.GroupIndex;
import com.ftn.socialNetwork.dto.SearchQueryDTO;
import com.ftn.socialNetwork.indexservice.interfaces.GroupSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class GroupSearchController {

    private final GroupSearchService searchService;

    @PostMapping("/groups/name")
    public Page<GroupIndex> nameSearch(@RequestBody SearchQueryDTO simpleSearchQuery,
                                         Pageable pageable) {
        return searchService.nameSearch(simpleSearchQuery.keywords(), pageable);
    }

    @PostMapping("/groups/description")
    public Page<GroupIndex> descriptionSearch(@RequestBody SearchQueryDTO simpleSearchQuery,
                                         Pageable pageable) {
        return searchService.descriptionSearch(simpleSearchQuery.keywords(), pageable);
    }

//    @PostMapping("/groups/advanced")
//    public Page<GroupIndex> advancedSearch(@RequestBody SearchQueryDTO advancedSearchQuery,
//                                           Pageable pageable) {
//        return searchService.advancedSearch(advancedSearchQuery.keywords(), pageable);
//    }
}
