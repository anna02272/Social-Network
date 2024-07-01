package com.ftn.socialNetwork.indexservice.interfaces;

import com.ftn.socialNetwork.dto.SearchQueryDTO;
import com.ftn.socialNetwork.indexmodel.GroupIndex;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface GroupSearchService {
    Page<GroupIndex> nameAndDescriptionSearch(List<String> keywords, Pageable pageable);
    Page<GroupIndex> nameAndDescriptionPhraseSearch(List<String> keywords, Pageable pageable);
    Page<GroupIndex> rulesSearch(List<String> keywords, Pageable pageable);
    Page<GroupIndex> rulesPhraseSearch(List<String> keywords, Pageable pageable);
    Page<GroupIndex> searchByPostCountRange(Integer from, Integer to, Pageable pageable);
    Page<GroupIndex> searchByAverageLkeCountRange(Float from, Float to, Pageable pageable);
    Page<GroupIndex> combinedSearch(SearchQueryDTO searchQuery, Pageable pageable);

    Page<GroupIndex> combinedPhraseSearch(SearchQueryDTO searchQuery, Pageable pageable);
}
