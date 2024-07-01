package com.ftn.socialNetwork.indexservice.implementation;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.RangeQuery;
import co.elastic.clients.json.JsonData;
import com.ftn.socialNetwork.dto.SearchQueryDTO;
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
                new NativeQueryBuilder().withQuery(buildSearchQuery(keywords))
                        .withPageable(pageable);

        return runQuery(searchQueryBuilder.build());
    }

    @Override
    public Page<GroupIndex> nameAndDescriptionPhraseSearch(List<String> keywords, Pageable pageable) {
        var searchQueryBuilder =
                new NativeQueryBuilder().withQuery(buildPhraseSearchQuery(keywords))
                        .withPageable(pageable);

        return runQuery(searchQueryBuilder.build());
    }

    @Override
    public Page<GroupIndex> rulesSearch(List<String> keywords, Pageable pageable) {
        var searchQueryBuilder =
                new NativeQueryBuilder().withQuery(buildRulesSearchQuery(keywords))
                        .withPageable(pageable);

        return runQuery(searchQueryBuilder.build());
    }

    @Override
    public Page<GroupIndex> rulesPhraseSearch(List<String> keywords, Pageable pageable) {
        var searchQueryBuilder =
                new NativeQueryBuilder().withQuery(buildPhraseRulesSearchQuery(keywords))
                        .withPageable(pageable);

        return runQuery(searchQueryBuilder.build());
    }

    @Override
    public Page<GroupIndex> searchByPostCountRange(Integer from, Integer to, Pageable pageable) {
        var rangeQuery = buildRangeQuery(from, to);
        var searchQueryBuilder = new NativeQueryBuilder().withQuery(rangeQuery).withPageable(pageable);
        return runQuery(searchQueryBuilder.build());
    }

    @Override
    public Page<GroupIndex> searchByAverageLkeCountRange(Float from, Float to, Pageable pageable) {
        var rangeQuery = buildLikeRangeQuery(from, to);
        var searchQueryBuilder = new NativeQueryBuilder().withQuery(rangeQuery).withPageable(pageable);
        return runQuery(searchQueryBuilder.build());
    }

    @Override
    public Page<GroupIndex> combinedSearch(SearchQueryDTO searchQuery, Pageable pageable) {
        var searchQueryBuilder = new NativeQueryBuilder()
                .withQuery(buildCombinedSearchQuery(
                        searchQuery.name(),
                        searchQuery.description(),
                        searchQuery.pdfContent(),
                        searchQuery.rules(),
                        searchQuery.postAverageLikes(),
                        searchQuery.postCount(),
                        searchQuery.operation()
                ))
                .withPageable(pageable);
        return runQuery(searchQueryBuilder.build());
    }

    @Override
    public Page<GroupIndex> combinedPhraseSearch(SearchQueryDTO searchQuery, Pageable pageable) {
        var searchQueryBuilder = new NativeQueryBuilder()
                .withQuery(buildCombinedPhraseSearchQuery(
                        searchQuery.name(),
                        searchQuery.description(),
                        searchQuery.pdfContent(),
                        searchQuery.rules(),
                        searchQuery.postAverageLikes(),
                        searchQuery.postCount(),
                        searchQuery.operation()
                ))
                .withPageable(pageable);
        return runQuery(searchQueryBuilder.build());
    }

    private Query buildSearchQuery(List<String> tokens) {
        return BoolQuery.of(q -> q.must(mb -> mb.bool(b -> {
            tokens.forEach(token -> {
                b.should(sb -> sb.match(
                        m -> m.field("name").fuzziness(Fuzziness.ONE.asString()).query(token)));
                b.should(sb -> sb.match(
                        m -> m.field("description").fuzziness(Fuzziness.ONE.asString()).query(token)));
                b.should(sb -> sb.match(m -> m.field("content_sr").fuzziness(Fuzziness.ONE.asString()).query(token)));
                b.should(sb -> sb.match(m -> m.field("content_en").fuzziness(Fuzziness.ONE.asString()).query(token)));
            });
            return b;
        })))._toQuery();
    }

    private Query buildPhraseSearchQuery(List<String> tokens) {
        return BoolQuery.of(q -> q.must(mb -> mb.bool(b -> {
            tokens.forEach(token -> {
                b.should(sb -> sb.matchPhrase(
                        m -> m.field("name").query(token)));
                b.should(sb -> sb.matchPhrase(
                        m -> m.field("description").query(token)));
                b.should(sb -> sb.matchPhrase(m -> m.field("content_sr").query(token)));
                b.should(sb -> sb.matchPhrase(m -> m.field("content_en").query(token)));
            });
            return b;
        })))._toQuery();
    }

    private Query buildRulesSearchQuery(List<String> tokens) {
        return BoolQuery.of(q -> q.must(mb -> mb.bool(b -> {
            tokens.forEach(token -> {
                b.should(sb -> sb.match(
                        m -> m.field("rules").fuzziness(Fuzziness.ONE.asString()).query(token)));
            });
            return b;
        })))._toQuery();
    }

    private Query buildPhraseRulesSearchQuery(List<String> tokens) {
        return BoolQuery.of(q -> q.must(mb -> mb.bool(b -> {
            tokens.forEach(token -> {
                b.should(sb -> sb.matchPhrase(
                        m -> m.field("rules").query(token)));
            });
            return b;
        })))._toQuery();
    }

    private Query buildRangeQuery(Integer from, Integer to) {
        return BoolQuery.of(q -> q.filter(f -> f.range(RangeQuery.of(r -> r
                .field("postCount")
                .gte(JsonData.of(from != null ? from : 0))
                .lte(JsonData.of(to != null ? to : Integer.MAX_VALUE))
        ))))._toQuery();
    }

    private Query buildLikeRangeQuery(Float from, Float to) {
        return BoolQuery.of(q -> q.filter(f -> f.range(RangeQuery.of(r -> r
                .field("postAverageLikes")
                .gte(JsonData.of(from != null ? from : 0))
                .lte(JsonData.of(to != null ? to : Float.MAX_VALUE))
        ))))._toQuery();
    }

    private Query buildCombinedSearchQuery(String name, String description,  String pdfContent, String rules, List<Integer> postAverageLikes, List<Integer> postCount, String operation) {
        return BoolQuery.of(q -> q.must(mb -> mb.bool(b -> {
            if ("AND".equalsIgnoreCase(operation)) {
                if (name != null && !name.isEmpty()) {
                    b.must(sb -> sb.bool(subBool -> subBool
                            .should(subShould -> subShould.match(m -> m.field("name").fuzziness(Fuzziness.ONE.asString()).query(name)))));
                }
                if (description != null && !description.isEmpty()) {
                    b.must(sb -> sb.bool(subBool -> subBool
                            .should(subShould -> subShould.match(m -> m.field("description").fuzziness(Fuzziness.ONE.asString()).query(description)))));
                }
                if (rules != null && !rules.isEmpty()) {
                    b.must(sb -> sb.bool(subBool -> subBool
                            .should(subShould -> subShould.match(m -> m.field("rules").fuzziness(Fuzziness.ONE.asString()).query(rules)))));
                }
                if (pdfContent != null && !pdfContent.isEmpty()) {
                    b.must(sb -> sb.bool(subBool -> subBool
                            .should(subShould -> subShould.match(m -> m.field("content_sr").fuzziness(Fuzziness.ONE.asString()).query(pdfContent)))
                            .should(subShould -> subShould.match(m -> m.field("content_en").fuzziness(Fuzziness.ONE.asString()).query(pdfContent)))));
                }
                if (postAverageLikes != null && !postAverageLikes.isEmpty()) {
                        for (int i = 0; i < postAverageLikes.size(); i += 2) {
                            int lowerBound = postAverageLikes.get(i);
                            int upperBound = postAverageLikes.get(i + 1);
                            b.must(sb -> sb.range(r -> r.field("postAverageLikes").gte(JsonData.of(lowerBound)).lte(JsonData.of(upperBound))));
                        }
                }
                if (postCount != null && !postCount.isEmpty()) {
                    for (int i = 0; i < postCount.size(); i += 2) {
                        int lowerBound = postCount.get(i);
                        int upperBound = postCount.get(i + 1);
                        b.must(sb -> sb.range(r -> r.field("postCount").gte(JsonData.of(lowerBound)).lte(JsonData.of(upperBound))));
                    }
                }
            } else if ("OR".equalsIgnoreCase(operation)) {
                if (name != null && !name.isEmpty()) {
                    b.should(sb -> sb.bool(subBool -> subBool
                            .should(subShould -> subShould.match(m -> m.field("name").fuzziness(Fuzziness.ONE.asString()).query(name)))));
                }
                if (description != null && !description.isEmpty()) {
                    b.should(sb -> sb.bool(subBool -> subBool
                            .should(subShould -> subShould.match(m -> m.field("description").fuzziness(Fuzziness.ONE.asString()).query(description)))));
                }
                if (rules != null && !rules.isEmpty()) {
                    b.should(sb -> sb.bool(subBool -> subBool
                            .should(subShould -> subShould.match(m -> m.field("rules").fuzziness(Fuzziness.ONE.asString()).query(rules)))));
                }
                if (pdfContent != null && !pdfContent.isEmpty()) {
                    b.should(sb -> sb.match(m -> m.field("content_sr").fuzziness(Fuzziness.ONE.asString()).query(pdfContent)));
                    b.should(sb -> sb.match(m -> m.field("content_en").fuzziness(Fuzziness.ONE.asString()).query(pdfContent)));
                }
                if (postAverageLikes != null && !postAverageLikes.isEmpty()) {
                    for (int i = 0; i < postAverageLikes.size(); i += 2) {
                        int lowerBound = postAverageLikes.get(i);
                        int upperBound = postAverageLikes.get(i + 1);
                        b.should(sb -> sb.range(r -> r.field("postAverageLikes").gte(JsonData.of(lowerBound)).lte(JsonData.of(upperBound))));
                    }
                }
                if (postCount != null && !postCount.isEmpty()) {
                    for (int i = 0; i < postCount.size(); i += 2) {
                        int lowerBound = postCount.get(i);
                        int upperBound = postCount.get(i + 1);
                        b.should(sb -> sb.range(r -> r.field("postCount").gte(JsonData.of(lowerBound)).lte(JsonData.of(upperBound))));
                    }
                }
            }
            return b;
        })))._toQuery();
    }

    private Query buildCombinedPhraseSearchQuery(String name, String description,  String pdfContent, String rules, List<Integer> postAverageLikes, List<Integer> postCount, String operation) {
        return BoolQuery.of(q -> q.must(mb -> mb.bool(b -> {
            if ("AND".equalsIgnoreCase(operation)) {
                if (name != null && !name.isEmpty()) {
                    b.must(sb -> sb.bool(subBool -> subBool
                            .should(subShould -> subShould.matchPhrase(m -> m.field("name").query(name)))));
                }
                if (description != null && !description.isEmpty()) {
                    b.must(sb -> sb.bool(subBool -> subBool
                            .should(subShould -> subShould.matchPhrase(m -> m.field("description").query(description)))));
                }
                if (rules != null && !rules.isEmpty()) {
                    b.must(sb -> sb.bool(subBool -> subBool
                            .should(subShould -> subShould.matchPhrase(m -> m.field("rules").query(rules)))));
                }
                if (pdfContent != null && !pdfContent.isEmpty()) {
                    b.must(sb -> sb.bool(subBool -> subBool
                           .should(subShould -> subShould.matchPhrase(m -> m.field("content_sr").query(pdfContent)))
                            .should(subShould -> subShould.matchPhrase(m -> m.field("content_en").query(pdfContent)))
                    ));
                }
                if (postAverageLikes != null && !postAverageLikes.isEmpty()) {
                    for (int i = 0; i < postAverageLikes.size(); i += 2) {
                        int lowerBound = postAverageLikes.get(i);
                        int upperBound = postAverageLikes.get(i + 1);
                        b.must(sb -> sb.range(r -> r.field("postAverageLikes").gte(JsonData.of(lowerBound)).lte(JsonData.of(upperBound))));
                    }
                }
                if (postCount != null && !postCount.isEmpty()) {
                    for (int i = 0; i < postCount.size(); i += 2) {
                        int lowerBound = postCount.get(i);
                        int upperBound = postCount.get(i + 1);
                        b.must(sb -> sb.range(r -> r.field("postCount").gte(JsonData.of(lowerBound)).lte(JsonData.of(upperBound))));
                    }
                }
            } else if ("OR".equalsIgnoreCase(operation)) {
                if (name != null && !name.isEmpty()) {
                    b.should(sb -> sb.bool(subBool -> subBool
                             .should(subShould -> subShould.matchPhrase(m -> m.field("name").query(name)))));
                }
                if (description != null && !description.isEmpty()) {
                    b.should(sb -> sb.bool(subBool -> subBool
                            .should(subShould -> subShould.matchPhrase(m -> m.field("description").query(description)))));
                }
                if (rules != null && !rules.isEmpty()) {
                    b.should(sb -> sb.bool(subBool -> subBool
                            .should(subShould -> subShould.matchPhrase(m -> m.field("rules").query(rules)))));
                }
                if (pdfContent != null && !pdfContent.isEmpty()) {
                    b.should(sb -> sb.matchPhrase(m -> m.field("content_sr").query(pdfContent)));
                    b.should(sb -> sb.matchPhrase(m -> m.field("content_en").query(pdfContent)));
                }
                if (postAverageLikes != null && !postAverageLikes.isEmpty()) {
                    for (int i = 0; i < postAverageLikes.size(); i += 2) {
                        int lowerBound = postAverageLikes.get(i);
                        int upperBound = postAverageLikes.get(i + 1);
                        b.should(sb -> sb.range(r -> r.field("postAverageLikes").gte(JsonData.of(lowerBound)).lte(JsonData.of(upperBound))));
                    }
                }
                if (postCount != null && !postCount.isEmpty()) {
                    for (int i = 0; i < postCount.size(); i += 2) {
                        int lowerBound = postCount.get(i);
                        int upperBound = postCount.get(i + 1);
                        b.should(sb -> sb.range(r -> r.field("postCount").gte(JsonData.of(lowerBound)).lte(JsonData.of(upperBound))));
                    }
                }
            }
            return b;
        })))._toQuery();
    }

    private Page<GroupIndex> runQuery(NativeQuery searchQuery) {

        var searchHits = elasticsearchTemplate.search(searchQuery, GroupIndex.class,
                IndexCoordinates.of("group_index"));

        var searchHitsPaged = SearchHitSupport.searchPageFor(searchHits, searchQuery.getPageable());

        return (Page<GroupIndex>) SearchHitSupport.unwrapSearchHits(searchHitsPaged);
    }
}