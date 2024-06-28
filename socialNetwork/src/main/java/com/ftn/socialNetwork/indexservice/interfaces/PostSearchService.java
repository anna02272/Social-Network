package com.ftn.socialNetwork.indexservice.interfaces;

import com.ftn.socialNetwork.indexmodel.PostIndex;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PostSearchService {
    Page<PostIndex> titleAndContentSearch(List<String> keywords, Pageable pageable);

}
