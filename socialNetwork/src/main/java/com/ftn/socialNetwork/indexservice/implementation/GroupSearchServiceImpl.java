package com.ftn.socialNetwork.indexservice.implementation;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import com.ftn.socialNetwork.indexmodel.GroupIndex;
import com.ftn.socialNetwork.indexservice.interfaces.GroupSearchService;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.common.unit.Fuzziness;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHitSupport;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupSearchServiceImpl implements GroupSearchService {

    private final ElasticsearchOperations elasticsearchTemplate;
    @Override
    public Page<GroupIndex> nameAndDescriptionSearch(List<String> keywords, Pageable pageable) {
        var searchQueryBuilder =
                new NativeQueryBuilder().withQuery((co.elastic.clients.elasticsearch._types.query_dsl.Query) buildSearchQuery(keywords))
                        .withPageable(pageable);

        return runQuery(searchQueryBuilder.build());
    }

//    @Override
//    public Page<GroupIndex> advancedSearch(List<String> expression, Pageable pageable) {
//        if (expression.size() != 3) {
//            throw new MalformedQueryException("Search query malformed.");
//        }
//
//        String operation = expression.get(1);
//        expression.remove(1);
//        var searchQueryBuilder =
//                new NativeQueryBuilder().withQuery(buildAdvancedSearchQuery(expression, operation))
//                        .withPageable(pageable);
//
//        return runQuery(searchQueryBuilder.build());
//    }

    private Query buildSearchQuery(List<String> tokens) {
        return (Query) BoolQuery.of(q -> q.must(mb -> mb.bool(b -> {
            tokens.forEach(token -> {
                b.should(sb -> sb.match(
                        m -> m.field("name").fuzziness(Fuzziness.ONE.asString()).query(token)));
                b.should(sb -> sb.match(
                        m -> m.field("description").fuzziness(Fuzziness.ONE.asString()).query(token)));
                b.should(sb -> sb.match(m -> m.field("content_sr").query(token)));
                b.should(sb -> sb.match(m -> m.field("content_en").query(token)));
            });
            return b;
        })))._toQuery();
    }

//    private Query buildAdvancedSearchQuery(List<String> operands, String operation) {
//        return BoolQuery.of(q -> q.must(mb -> mb.bool(b -> {
//            var field1 = operands.get(0).split(":")[0];
//            var value1 = operands.get(0).split(":")[1];
//            var field2 = operands.get(1).split(":")[0];
//            var value2 = operands.get(1).split(":")[1];
//
//            switch (operation) {
//                case "AND" -> {
//                    b.must(sb -> sb.match(
//                            m -> m.field(field1).fuzziness(Fuzziness.ONE.asString()).query(value1)));
//                    b.must(sb -> sb.match(m -> m.field(field2).query(value2)));
//                }
//                case "OR" -> {
//                    b.should(sb -> sb.match(
//                            m -> m.field(field1).fuzziness(Fuzziness.ONE.asString()).query(value1)));
//                    b.should(sb -> sb.match(m -> m.field(field2).query(value2)));
//                }
//                case "NOT" -> {
//                    b.must(sb -> sb.match(
//                            m -> m.field(field1).fuzziness(Fuzziness.ONE.asString()).query(value1)));
//                    b.mustNot(sb -> sb.match(m -> m.field(field2).query(value2)));
//                }
//            }
//
//            return b;
//        })))._toQuery();
//    }

    private Page<GroupIndex> runQuery(NativeQuery searchQuery) {

        var searchHits = elasticsearchTemplate.search(searchQuery, GroupIndex.class,
                IndexCoordinates.of("group_index"));

        var searchHitsPaged = SearchHitSupport.searchPageFor(searchHits, searchQuery.getPageable());

        return (Page<GroupIndex>) SearchHitSupport.unwrapSearchHits(searchHitsPaged);
    }
}