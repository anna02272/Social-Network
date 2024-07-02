package com.ftn.socialNetwork.indexservice.interfaces;

import com.ftn.socialNetwork.dto.PostSearchResultDTO;
import com.ftn.socialNetwork.dto.SearchQueryDTO;
import com.ftn.socialNetwork.indexmodel.PostIndex;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PostSearchService {
    Page<PostSearchResultDTO> titleAndContentSearch(List<String> keywords, Pageable pageable);
    Page<PostSearchResultDTO> titleAndContentPhraseSearch(List<String> keywords, Pageable pageable);
    Page<PostSearchResultDTO> searchByLikeCountRange(Integer from, Integer to, Pageable pageable);
    Page<PostSearchResultDTO> searchByCommentCountRange(Integer from, Integer to, Pageable pageable);
    Page<PostSearchResultDTO> commentTextSearch(List<String> keywords, Pageable pageable);
    Page<PostSearchResultDTO> commentTextPhraseSearch(List<String> keywords, Pageable pageable);
    Page<PostSearchResultDTO> combinedSearch(SearchQueryDTO searchQuery, Pageable pageable);
    Page<PostSearchResultDTO> combinedPhraseSearch(SearchQueryDTO searchQuery, Pageable pageable);
}
