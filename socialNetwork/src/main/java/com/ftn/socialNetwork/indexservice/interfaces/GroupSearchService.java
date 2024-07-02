package com.ftn.socialNetwork.indexservice.interfaces;

import com.ftn.socialNetwork.dto.GroupSearchResultDTO;
import com.ftn.socialNetwork.dto.SearchQueryDTO;
import com.ftn.socialNetwork.indexmodel.GroupIndex;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface GroupSearchService {
    Page<GroupSearchResultDTO> nameAndDescriptionSearch(List<String> keywords, Pageable pageable);
    Page<GroupSearchResultDTO> nameAndDescriptionPhraseSearch(List<String> keywords, Pageable pageable);
    Page<GroupSearchResultDTO> rulesSearch(List<String> keywords, Pageable pageable);
    Page<GroupSearchResultDTO> rulesPhraseSearch(List<String> keywords, Pageable pageable);
    Page<GroupSearchResultDTO> searchByPostCountRange(Integer from, Integer to, Pageable pageable);
    Page<GroupSearchResultDTO> searchByAverageLkeCountRange(Float from, Float to, Pageable pageable);
    Page<GroupSearchResultDTO> combinedSearch(SearchQueryDTO searchQuery, Pageable pageable);

    Page<GroupSearchResultDTO> combinedPhraseSearch(SearchQueryDTO searchQuery, Pageable pageable);
}
