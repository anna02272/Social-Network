package com.ftn.socialNetwork.indexcontroller;

import com.ftn.socialNetwork.dto.SearchQueryDTO;
import com.ftn.socialNetwork.indexmodel.PostIndex;
import com.ftn.socialNetwork.indexservice.interfaces.PostSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class PostSearchController {

    private final PostSearchService searchService;

    @PostMapping("/posts/titleAndContent")
    public Page<PostIndex> titleAndContentSearch(@RequestBody SearchQueryDTO simpleSearchQuery,
                                                 Pageable pageable) {
        return searchService.titleAndContentSearch(simpleSearchQuery.keywords(), pageable);
    }

}
