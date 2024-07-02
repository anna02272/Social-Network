package com.ftn.socialNetwork.indexservice.interfaces;

import com.ftn.socialNetwork.dto.SearchQueryDTO;
import com.ftn.socialNetwork.indexmodel.PostIndex;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PostSearchService {
    Page<PostIndex> titleAndContentSearch(List<String> keywords, Pageable pageable);
    Page<PostIndex> titleAndContentPhraseSearch(List<String> keywords, Pageable pageable);
    Page<PostIndex> searchByLikeCountRange(Integer from, Integer to, Pageable pageable);
    Page<PostIndex> searchByCommentCountRange(Integer from, Integer to, Pageable pageable);
    Page<PostIndex> commentTextSearch(List<String> keywords, Pageable pageable);
    Page<PostIndex> commentTextPhraseSearch(List<String> keywords, Pageable pageable);

    Page<PostIndex> combinedSearch(SearchQueryDTO searchQuery, Pageable pageable);

    Page<PostIndex> combinedPhraseSearch(SearchQueryDTO searchQuery, Pageable pageable);
}
