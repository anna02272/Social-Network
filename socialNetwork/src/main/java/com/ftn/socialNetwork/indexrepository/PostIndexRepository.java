package com.ftn.socialNetwork.indexrepository;

import com.ftn.socialNetwork.indexmodel.PostIndex;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostIndexRepository
    extends ElasticsearchRepository<PostIndex, String> {
}