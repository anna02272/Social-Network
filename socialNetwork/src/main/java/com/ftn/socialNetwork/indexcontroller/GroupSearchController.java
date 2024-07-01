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

    @PostMapping("/groups/phrase/nameAndDescription")
    public Page<GroupIndex> nameAndDescriptionPhraseSearch(@RequestBody SearchQueryDTO simpleSearchQuery,
                                                     Pageable pageable) {
        return searchService.nameAndDescriptionPhraseSearch(simpleSearchQuery.keywords(), pageable);
    }

    @PostMapping("/groups/rules")
    public Page<GroupIndex> rulesSearch(@RequestBody SearchQueryDTO simpleSearchQuery,
                                        Pageable pageable) {
        return searchService.rulesSearch(simpleSearchQuery.keywords(), pageable);
    }

    @PostMapping("/groups/phrase/rules")
    public Page<GroupIndex> rulesPhraseSearch(@RequestBody SearchQueryDTO simpleSearchQuery,
                                        Pageable pageable) {
        return searchService.rulesPhraseSearch(simpleSearchQuery.keywords(), pageable);
    }

    @GetMapping("/groups/postCountRange")
    public Page<GroupIndex> searchByPostCountRange(
            @RequestParam(value = "from", required = false) Integer from,
            @RequestParam(value = "to", required = false) Integer to,
            Pageable pageable) {
        return searchService.searchByPostCountRange(from, to, pageable);
    }

    @GetMapping("/groups/averageLikeCountRange")
    public Page<GroupIndex> searchByAverageLikeCountRange(
            @RequestParam(value = "from", required = false) Float from,
            @RequestParam(value = "to", required = false) Float to,
            Pageable pageable) {
        return searchService.searchByAverageLkeCountRange(from, to, pageable);
    }

    @PostMapping("/groups/combined")
    public Page<GroupIndex> combinedSearch(@RequestBody SearchQueryDTO combinedSearchQuery, Pageable pageable) {
        return searchService.combinedSearch(combinedSearchQuery, pageable);
    }

    @PostMapping("/groups/phrase/combined")
    public Page<GroupIndex> combinedPhraseSearch(@RequestBody SearchQueryDTO combinedSearchQuery, Pageable pageable) {
        return searchService.combinedPhraseSearch(combinedSearchQuery, pageable);
    }

}
