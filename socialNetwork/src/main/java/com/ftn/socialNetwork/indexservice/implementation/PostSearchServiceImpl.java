package com.ftn.socialNetwork.indexservice.implementation;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.RangeQuery;
import co.elastic.clients.json.JsonData;
import com.ftn.socialNetwork.indexmodel.GroupIndex;
import com.ftn.socialNetwork.indexmodel.PostIndex;
import com.ftn.socialNetwork.indexservice.interfaces.PostSearchService;
import com.ftn.socialNetwork.model.entity.Post;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.common.unit.Fuzziness;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHitSupport;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostSearchServiceImpl implements PostSearchService {

    private final ElasticsearchOperations elasticsearchTemplate;
    @Override
    public Page<PostIndex> titleAndContentSearch(List<String> keywords, Pageable pageable) {
        var searchQueryBuilder =
                new NativeQueryBuilder().withQuery((Query) buildSearchQuery(keywords))
                        .withPageable(pageable);

        return runQuery(searchQueryBuilder.build());
    }

    @Override
    public Page<PostIndex> searchByLikeCountRange(Integer from, Integer to, Pageable pageable) {
        var rangeQuery = buildRangeQuery(from, to);
        var searchQueryBuilder = new NativeQueryBuilder().withQuery(rangeQuery).withPageable(pageable);
        return runQuery(searchQueryBuilder.build());
    }

    private Query buildSearchQuery(List<String> tokens) {
        return (Query) BoolQuery.of(q -> q.must(mb -> mb.bool(b -> {
            tokens.forEach(token -> {
                b.should(sb -> sb.match(
                        m -> m.field("title").fuzziness(Fuzziness.ONE.asString()).query(token)));
                b.should(sb -> sb.match(
                        m -> m.field("content").fuzziness(Fuzziness.ONE.asString()).query(token)));
                b.should(sb -> sb.match(m -> m.field("content_sr").query(token)));
                b.should(sb -> sb.match(m -> m.field("content_en").query(token)));
            });
            return b;
        })))._toQuery();
    }

    private Query buildRangeQuery(Integer from, Integer to) {
        return BoolQuery.of(q -> q.filter(f -> f.range(RangeQuery.of(r -> r
                .field("likeCount")
                .gte(JsonData.of(from != null ? from : 0))
                .lte(JsonData.of(to != null ? to : Integer.MAX_VALUE))
        ))))._toQuery();
    }

    private Page<PostIndex> runQuery(NativeQuery searchQuery) {

        var searchHits = elasticsearchTemplate.search(searchQuery, PostIndex.class,
                IndexCoordinates.of("post_index"));

        var searchHitsPaged = SearchHitSupport.searchPageFor(searchHits, searchQuery.getPageable());

        return (Page<PostIndex>) SearchHitSupport.unwrapSearchHits(searchHitsPaged);
    }
}