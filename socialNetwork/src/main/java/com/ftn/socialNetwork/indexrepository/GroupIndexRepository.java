package com.ftn.socialNetwork.indexrepository;

import com.ftn.socialNetwork.indexmodel.GroupIndex;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupIndexRepository
    extends ElasticsearchRepository<GroupIndex, String> {
}
