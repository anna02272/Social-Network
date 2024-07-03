package com.ftn.socialNetwork.indexcontroller;

import com.ftn.socialNetwork.dto.PostSearchResultDTO;
import com.ftn.socialNetwork.dto.SearchQueryDTO;
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
    public Page<PostSearchResultDTO> titleAndContentSearch(@RequestBody SearchQueryDTO simpleSearchQuery,
                                                           Pageable pageable) {
        return searchService.titleAndContentSearch(simpleSearchQuery.keywords(), pageable);
    }

    @PostMapping("/posts/phrase/titleAndContent")
    public Page<PostSearchResultDTO> titleAndContentPhraseSearch(@RequestBody SearchQueryDTO simpleSearchQuery,
                                                 Pageable pageable) {
        return searchService.titleAndContentPhraseSearch(simpleSearchQuery.keywords(), pageable);
    }

    @GetMapping("/posts/likeCountRange")
    public Page<PostSearchResultDTO> searchByLikeCountRange(
            @RequestParam(value = "from", required = false) Integer from,
            @RequestParam(value = "to", required = false) Integer to,
            Pageable pageable) {
        return searchService.searchByLikeCountRange(from, to, pageable);
    }

    @GetMapping("/posts/commentCountRange")
    public Page<PostSearchResultDTO> searchByCommentCountRange(
            @RequestParam(value = "from", required = false) Integer from,
            @RequestParam(value = "to", required = false) Integer to,
            Pageable pageable) {
        return searchService.searchByCommentCountRange(from, to, pageable);
    }

    @PostMapping("/posts/commentText")
    public Page<PostSearchResultDTO> commentTextSearch(@RequestBody SearchQueryDTO simpleSearchQuery,
                                             Pageable pageable) {
        return searchService.commentTextSearch(simpleSearchQuery.keywords(), pageable);
    }

    @PostMapping("/posts/phrase/commentText")
    public Page<PostSearchResultDTO> commentTextPhraseSearch(@RequestBody SearchQueryDTO simpleSearchQuery,
                                             Pageable pageable) {
        return searchService.commentTextPhraseSearch(simpleSearchQuery.keywords(), pageable);
    }
    @PostMapping("/posts/combined")
    public Page<PostSearchResultDTO> combinedSearch(@RequestBody SearchQueryDTO combinedSearchQuery, Pageable pageable) {
        return searchService.combinedSearch(combinedSearchQuery, pageable);
    }

    @PostMapping("/posts/phrase/combined")
    public Page<PostSearchResultDTO> combinedPhraseSearch(@RequestBody SearchQueryDTO combinedSearchQuery, Pageable pageable) {
        return searchService.combinedPhraseSearch(combinedSearchQuery, pageable);
    }
}
