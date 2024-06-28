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

    @PostMapping("/groups/nameAndDescription")
    public Page<GroupIndex> nameAndDescriptionSearch(@RequestBody SearchQueryDTO simpleSearchQuery,
                                         Pageable pageable) {
        return searchService.nameAndDescriptionSearch(simpleSearchQuery.keywords(), pageable);
    }

    @GetMapping("/groups/postCountRange")
    public Page<GroupIndex> searchByPostCountRange(
            @RequestParam(value = "from", required = false) Integer from,
            @RequestParam(value = "to", required = false) Integer to,
            Pageable pageable) {
        return searchService.searchByPostCountRange(from, to, pageable);
    }

//    @PostMapping("/groups/advanced")
//    public Page<GroupIndex> advancedSearch(@RequestBody SearchQueryDTO advancedSearchQuery,
//                                           Pageable pageable) {
//        return searchService.advancedSearch(advancedSearchQuery.keywords(), pageable);
//    }
}
