package com.ftn.socialNetwork.indexcontroller;

import com.ftn.socialNetwork.dto.SearchQueryDTO;
import com.ftn.socialNetwork.indexmodel.GroupIndex;
import com.ftn.socialNetwork.indexmodel.PostIndex;
import com.ftn.socialNetwork.indexservice.interfaces.PostSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/posts/likeCountRange")
    public Page<PostIndex> searchByLikeCountRange(
            @RequestParam(value = "from", required = false) Integer from,
            @RequestParam(value = "to", required = false) Integer to,
            Pageable pageable) {
        return searchService.searchByLikeCountRange(from, to, pageable);
    }

    @GetMapping("/posts/commentCountRange")
    public Page<PostIndex> searchByCommentCountRange(
            @RequestParam(value = "from", required = false) Integer from,
            @RequestParam(value = "to", required = false) Integer to,
            Pageable pageable) {
        return searchService.searchByCommentCountRange(from, to, pageable);
    }

    @PostMapping("/posts/commentText")
    public Page<PostIndex> commentTextSearch(@RequestBody SearchQueryDTO simpleSearchQuery,
                                                 Pageable pageable) {
        return searchService.commentTextSearch(simpleSearchQuery.keywords(), pageable);
    }

}
