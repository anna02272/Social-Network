package com.ftn.socialNetwork.indexservice.implementation;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.RangeQuery;
import co.elastic.clients.json.JsonData;
import com.ftn.socialNetwork.dto.SearchQueryDTO;
import com.ftn.socialNetwork.indexmodel.PostIndex;
import com.ftn.socialNetwork.indexservice.interfaces.PostSearchService;
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
                new NativeQueryBuilder().withQuery(buildSearchQuery(keywords))
                        .withPageable(pageable);

        return runQuery(searchQueryBuilder.build());
    }

    @Override
    public Page<PostIndex> titleAndContentPhraseSearch(List<String> keywords, Pageable pageable) {
        var searchQueryBuilder =
                new NativeQueryBuilder().withQuery(buildPhraseSearchQuery(keywords))
                        .withPageable(pageable);

        return runQuery(searchQueryBuilder.build());
    }

    @Override
    public Page<PostIndex> searchByLikeCountRange(Integer from, Integer to, Pageable pageable) {
        var rangeQuery = buildRangeQuery(from, to);
        var searchQueryBuilder = new NativeQueryBuilder().withQuery(rangeQuery).withPageable(pageable);
        return runQuery(searchQueryBuilder.build());
    }

    @Override
    public Page<PostIndex> searchByCommentCountRange(Integer from, Integer to, Pageable pageable) {
        var rangeQuery = buildCommentRangeQuery(from, to);
        var searchQueryBuilder = new NativeQueryBuilder().withQuery(rangeQuery).withPageable(pageable);
        return runQuery(searchQueryBuilder.build());
    }

    @Override
    public Page<PostIndex> commentTextSearch(List<String> keywords, Pageable pageable) {
        var searchQueryBuilder =
                new NativeQueryBuilder().withQuery(buildCommentSearchQuery(keywords))
                        .withPageable(pageable);

        return runQuery(searchQueryBuilder.build());
    }

    @Override
    public Page<PostIndex> commentTextPhraseSearch(List<String> keywords, Pageable pageable) {
        var searchQueryBuilder =
                new NativeQueryBuilder().withQuery(buildCommentPhraseSearchQuery(keywords))
                        .withPageable(pageable);

        return runQuery(searchQueryBuilder.build());
    }
    @Override
    public Page<PostIndex> combinedSearch(SearchQueryDTO searchQuery, Pageable pageable) {
        var searchQueryBuilder = new NativeQueryBuilder()
                .withQuery(buildCombinedSearchQuery(
                        searchQuery.title(),
                        searchQuery.content(),
                        searchQuery.pdfContent(),
                        searchQuery.likeCount(),
                        searchQuery.commentCount(),
                        searchQuery.operation()
                ))
                .withPageable(pageable);
        return runQuery(searchQueryBuilder.build());
    }


    @Override
    public Page<PostIndex> combinedPhraseSearch(SearchQueryDTO searchQuery, Pageable pageable) {
        var searchQueryBuilder = new NativeQueryBuilder()
                .withQuery(buildCombinedPhraseSearchQuery(
                        searchQuery.title(),
                        searchQuery.content(),
                        searchQuery.pdfContent(),
                        searchQuery.likeCount(),
                        searchQuery.commentCount(),
                        searchQuery.operation()
                ))
                .withPageable(pageable);
        return runQuery(searchQueryBuilder.build());
    }

    private Query buildSearchQuery(List<String> tokens) {
        return BoolQuery.of(q -> q.must(mb -> mb.bool(b -> {
            tokens.forEach(token -> {
                b.should(sb -> sb.match(
                        m -> m.field("title").fuzziness(Fuzziness.ONE.asString()).query(token).analyzer("serbian_simple")));
                b.should(sb -> sb.match(
                        m -> m.field("content").fuzziness(Fuzziness.ONE.asString()).query(token)));
                b.should(sb -> sb.match(m -> m.field("content_sr").query(token)));
                b.should(sb -> sb.match(m -> m.field("content_en").query(token)));
            });
            return b;
        })))._toQuery();
    }

    private Query buildPhraseSearchQuery(List<String> tokens) {
        return BoolQuery.of(q -> q.must(mb -> mb.bool(b -> {
            tokens.forEach(token -> {
                b.should(sb -> sb.matchPhrase(
                        m -> m.field("title").query(token)));
                b.should(sb -> sb.matchPhrase(
                        m -> m.field("content").query(token)));
                b.should(sb -> sb.matchPhrase(m -> m.field("content_sr").query(token)));
                b.should(sb -> sb.matchPhrase(m -> m.field("content_en").query(token)));
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

    private Query buildCommentRangeQuery(Integer from, Integer to) {
        return BoolQuery.of(q -> q.filter(f -> f.range(RangeQuery.of(r -> r
                .field("commentCount")
                .gte(JsonData.of(from != null ? from : 0))
                .lte(JsonData.of(to != null ? to : Integer.MAX_VALUE))
        ))))._toQuery();
    }

    private Query buildCommentSearchQuery(List<String> tokens) {
        return BoolQuery.of(q -> q.must(mb -> mb.bool(b -> {
            tokens.forEach(token -> {
                b.should(sb -> sb.match(
                        m -> m.field("comments.text").fuzziness(Fuzziness.ONE.asString()).query(token)));
            });
            return b;
        })))._toQuery();
    }

    private Query buildCommentPhraseSearchQuery(List<String> tokens) {
        return BoolQuery.of(q -> q.must(mb -> mb.bool(b -> {
            tokens.forEach(token -> {
                b.should(sb -> sb.matchPhrase(
                        m -> m.field("comments.text").query(token)));
            });
            return b;
        })))._toQuery();
    }

    private Query buildCombinedSearchQuery(String title, String content,  String pdfContent, List<Integer> likeCount, List<Integer> commentCount, String operation) {
        return BoolQuery.of(q -> q.must(mb -> mb.bool(b -> {
            if ("AND".equalsIgnoreCase(operation)) {
                if (title != null && !title.isEmpty()) {
                    b.must(sb -> sb.bool(subBool -> subBool
                            .should(subShould -> subShould.match(m -> m.field("title").fuzziness(Fuzziness.ONE.asString()).query(title)))));
                }
                if (content != null && !content.isEmpty()) {
                    b.must(sb -> sb.bool(subBool -> subBool
                            .should(subShould -> subShould.match(m -> m.field("content").fuzziness(Fuzziness.ONE.asString()).query(content)))));
                }
                if (pdfContent != null && !pdfContent.isEmpty()) {
                    b.must(sb -> sb.bool(subBool -> subBool
                            .should(subShould -> subShould.match(m -> m.field("content_sr").fuzziness(Fuzziness.ONE.asString()).query(pdfContent)))
                            .should(subShould -> subShould.match(m -> m.field("content_en").fuzziness(Fuzziness.ONE.asString()).query(pdfContent)))));
                }
                if (likeCount != null && !likeCount.isEmpty()) {
                    for (int i = 0; i < likeCount.size(); i += 2) {
                        int lowerBound = likeCount.get(i);
                        int upperBound = likeCount.get(i + 1);
                        b.must(sb -> sb.range(r -> r.field("likeCount").gte(JsonData.of(lowerBound)).lte(JsonData.of(upperBound))));
                    }
                }
                if (commentCount != null && !commentCount.isEmpty()) {
                    for (int i = 0; i < commentCount.size(); i += 2) {
                        int lowerBound = commentCount.get(i);
                        int upperBound = commentCount.get(i + 1);
                        b.must(sb -> sb.range(r -> r.field("commentCount").gte(JsonData.of(lowerBound)).lte(JsonData.of(upperBound))));
                    }
                }
            } else if ("OR".equalsIgnoreCase(operation)) {
                if (title != null && !title.isEmpty()) {
                    b.should(sb -> sb.bool(subBool -> subBool
                            .should(subShould -> subShould.match(m -> m.field("title").fuzziness(Fuzziness.ONE.asString()).query(title)))));
                }
                if (content != null && !content.isEmpty()) {
                    b.should(sb -> sb.bool(subBool -> subBool
                            .should(subShould -> subShould.match(m -> m.field("content").fuzziness(Fuzziness.ONE.asString()).query(content)))));
                }
                if (pdfContent != null && !pdfContent.isEmpty()) {
                    b.should(sb -> sb.match(m -> m.field("content_sr").fuzziness(Fuzziness.ONE.asString()).query(pdfContent)));
                    b.should(sb -> sb.match(m -> m.field("content_en").fuzziness(Fuzziness.ONE.asString()).query(pdfContent)));
                }
                if (likeCount != null && !likeCount.isEmpty()) {
                    for (int i = 0; i < likeCount.size(); i += 2) {
                        int lowerBound = likeCount.get(i);
                        int upperBound = likeCount.get(i + 1);
                        b.should(sb -> sb.range(r -> r.field("likeCount").gte(JsonData.of(lowerBound)).lte(JsonData.of(upperBound))));
                    }
                }
                if (commentCount != null && !commentCount.isEmpty()) {
                    for (int i = 0; i < commentCount.size(); i += 2) {
                        int lowerBound = commentCount.get(i);
                        int upperBound = commentCount.get(i + 1);
                        b.should(sb -> sb.range(r -> r.field("commentCount").gte(JsonData.of(lowerBound)).lte(JsonData.of(upperBound))));
                    }
                }
            }
            return b;
        })))._toQuery();
    }

    private Query buildCombinedPhraseSearchQuery(String title, String content,  String pdfContent, List<Integer> likeCount, List<Integer> commentCount,  String operation) {
        return BoolQuery.of(q -> q.must(mb -> mb.bool(b -> {
            if ("AND".equalsIgnoreCase(operation)) {
                if (title != null && !title.isEmpty()) {
                    b.must(sb -> sb.bool(subBool -> subBool
                            .should(subShould -> subShould.matchPhrase(m -> m.field("title").query(title)))));
                }
                if (content != null && !content.isEmpty()) {
                    b.must(sb -> sb.bool(subBool -> subBool
                            .should(subShould -> subShould.matchPhrase(m -> m.field("content").query(content)))));
                }
                if (pdfContent != null && !pdfContent.isEmpty()) {
                    b.must(sb -> sb.bool(subBool -> subBool
                            .should(subShould -> subShould.matchPhrase(m -> m.field("content_sr").query(pdfContent)))
                            .should(subShould -> subShould.matchPhrase(m -> m.field("content_en").query(pdfContent)))
                    ));
                }
                if (likeCount != null && !likeCount.isEmpty()) {
                    for (int i = 0; i < likeCount.size(); i += 2) {
                        int lowerBound = likeCount.get(i);
                        int upperBound = likeCount.get(i + 1);
                        b.must(sb -> sb.range(r -> r.field("likeCount").gte(JsonData.of(lowerBound)).lte(JsonData.of(upperBound))));
                    }
                }
                if (commentCount != null && !commentCount.isEmpty()) {
                    for (int i = 0; i < commentCount.size(); i += 2) {
                        int lowerBound = commentCount.get(i);
                        int upperBound = commentCount.get(i + 1);
                        b.must(sb -> sb.range(r -> r.field("commentCount").gte(JsonData.of(lowerBound)).lte(JsonData.of(upperBound))));
                    }
                }
            } else if ("OR".equalsIgnoreCase(operation)) {
                if (title != null && !title.isEmpty()) {
                    b.should(sb -> sb.bool(subBool -> subBool
                            .should(subShould -> subShould.matchPhrase(m -> m.field("title").query(title)))));
                }
                if (content != null && !content.isEmpty()) {
                    b.should(sb -> sb.bool(subBool -> subBool
                            .should(subShould -> subShould.matchPhrase(m -> m.field("content").query(content)))));
                }
                if (pdfContent != null && !pdfContent.isEmpty()) {
                    b.should(sb -> sb.matchPhrase(m -> m.field("content_sr").query(pdfContent)));
                    b.should(sb -> sb.matchPhrase(m -> m.field("content_en").query(pdfContent)));
                }
                if (likeCount != null && !likeCount.isEmpty()) {
                    for (int i = 0; i < likeCount.size(); i += 2) {
                        int lowerBound = likeCount.get(i);
                        int upperBound = likeCount.get(i + 1);
                        b.should(sb -> sb.range(r -> r.field("likeCount").gte(JsonData.of(lowerBound)).lte(JsonData.of(upperBound))));
                    }
                }
                if (commentCount != null && !commentCount.isEmpty()) {
                    for (int i = 0; i < commentCount.size(); i += 2) {
                        int lowerBound = commentCount.get(i);
                        int upperBound = commentCount.get(i + 1);
                        b.should(sb -> sb.range(r -> r.field("commentCount").gte(JsonData.of(lowerBound)).lte(JsonData.of(upperBound))));
                    }
                }
            }
            return b;
        })))._toQuery();
    }

    private Page<PostIndex> runQuery(NativeQuery searchQuery) {

        var searchHits = elasticsearchTemplate.search(searchQuery, PostIndex.class,
                IndexCoordinates.of("post_index"));

        var searchHitsPaged = SearchHitSupport.searchPageFor(searchHits, searchQuery.getPageable());

        return (Page<PostIndex>) SearchHitSupport.unwrapSearchHits(searchHitsPaged);
    }
}