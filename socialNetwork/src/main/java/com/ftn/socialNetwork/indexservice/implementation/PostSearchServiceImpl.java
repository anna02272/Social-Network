package com.ftn.socialNetwork.indexservice.implementation;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.RangeQuery;
import co.elastic.clients.json.JsonData;
import com.ftn.socialNetwork.dto.GroupSearchResultDTO;
import com.ftn.socialNetwork.dto.PostSearchResultDTO;
import com.ftn.socialNetwork.dto.SearchQueryDTO;
import com.ftn.socialNetwork.indexmodel.GroupIndex;
import com.ftn.socialNetwork.indexmodel.PostIndex;
import com.ftn.socialNetwork.indexservice.interfaces.PostSearchService;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.common.unit.Fuzziness;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHitSupport;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.HighlightQuery;
import org.springframework.data.elasticsearch.core.query.highlight.Highlight;
import org.springframework.data.elasticsearch.core.query.highlight.HighlightField;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PostSearchServiceImpl implements PostSearchService {

    private final ElasticsearchOperations elasticsearchTemplate;
    @Override
    public Page<PostSearchResultDTO> titleAndContentSearch(List<String> keywords, Pageable pageable) {
        List<HighlightField> requiredHighlights = Arrays.asList(
                new HighlightField("title"),
                new HighlightField("content"),
                new HighlightField("content_sr"),
                new HighlightField("content_en")
        );

        Query searchQuery = buildSearchQuery(keywords);
        NativeQuery queryBuilder = new NativeQueryBuilder()
                .withQuery(searchQuery)
                .withPageable(pageable)
                .withHighlightQuery(new HighlightQuery(new Highlight(requiredHighlights), PostIndex.class))
                .build();

        return runQuery(queryBuilder);
    }

    @Override
    public Page<PostSearchResultDTO> titleAndContentPhraseSearch(List<String> keywords, Pageable pageable) {
        List<HighlightField> requiredHighlights = Arrays.asList(
                new HighlightField("title"),
                new HighlightField("content"),
                new HighlightField("content_sr"),
                new HighlightField("content_en")
        );

        Query searchQuery = buildPhraseSearchQuery(keywords);
        NativeQuery queryBuilder = new NativeQueryBuilder()
                .withQuery(searchQuery)
                .withPageable(pageable)
                .withHighlightQuery(new HighlightQuery(new Highlight(requiredHighlights), PostIndex.class))
                .build();

        return runQuery(queryBuilder);
    }

    @Override
    public Page<PostSearchResultDTO> searchByLikeCountRange(Integer from, Integer to, Pageable pageable) {
        var rangeQuery = buildRangeQuery(from, to);
        var searchQueryBuilder = new NativeQueryBuilder().withQuery(rangeQuery).withPageable(pageable);
        return runQuery(searchQueryBuilder.build());
    }

    @Override
    public Page<PostSearchResultDTO> searchByCommentCountRange(Integer from, Integer to, Pageable pageable) {
        var rangeQuery = buildCommentRangeQuery(from, to);
        var searchQueryBuilder = new NativeQueryBuilder().withQuery(rangeQuery).withPageable(pageable);
        return runQuery(searchQueryBuilder.build());
    }

    @Override
    public Page<PostSearchResultDTO> commentTextSearch(List<String> keywords, Pageable pageable) {
        List<HighlightField> requiredHighlights = List.of(
                new HighlightField("comments.text")
        );

        Query searchQuery = buildCommentSearchQuery(keywords);
        NativeQuery queryBuilder = new NativeQueryBuilder()
                .withQuery(searchQuery)
                .withPageable(pageable)
                .withHighlightQuery(new HighlightQuery(new Highlight(requiredHighlights), PostIndex.class))
                .build();

        return runQuery(queryBuilder);
    }

    @Override
    public Page<PostSearchResultDTO> commentTextPhraseSearch(List<String> keywords, Pageable pageable) {
        List<HighlightField> requiredHighlights = List.of(
                new HighlightField("comments.text")
        );

        Query searchQuery = buildCommentPhraseSearchQuery(keywords);
        NativeQuery queryBuilder = new NativeQueryBuilder()
                .withQuery(searchQuery)
                .withPageable(pageable)
                .withHighlightQuery(new HighlightQuery(new Highlight(requiredHighlights), PostIndex.class))
                .build();

        return runQuery(queryBuilder);
    }
    @Override
    public Page<PostSearchResultDTO> combinedSearch(SearchQueryDTO searchQuery, Pageable pageable) {
        List<HighlightField> requiredHighlights = Arrays.asList(
                new HighlightField("title"),
                new HighlightField("content"),
                new HighlightField("content_sr"),
                new HighlightField("content_en")
        );

        Query combinedQuery = buildCombinedSearchQuery(
                        searchQuery.title(),
                        searchQuery.content(),
                        searchQuery.pdfContent(),
                        searchQuery.likeCount(),
                        searchQuery.commentCount(),
                        searchQuery.operation()
                );

        NativeQuery queryBuilder = new NativeQueryBuilder()
                .withQuery(combinedQuery)
                .withPageable(pageable)
                .withHighlightQuery(new HighlightQuery(new Highlight(requiredHighlights), GroupIndex.class))
                .build();

        return runQuery(queryBuilder);
    }

    @Override
    public Page<PostSearchResultDTO> combinedPhraseSearch(SearchQueryDTO searchQuery, Pageable pageable) {
        List<HighlightField> requiredHighlights = Arrays.asList(
                new HighlightField("title"),
                new HighlightField("content"),
                new HighlightField("content_sr"),
                new HighlightField("content_en")
        );

        Query combinedQuery = buildCombinedPhraseSearchQuery(
                searchQuery.title(),
                searchQuery.content(),
                searchQuery.pdfContent(),
                searchQuery.likeCount(),
                searchQuery.commentCount(),
                searchQuery.operation()
        );

        NativeQuery queryBuilder = new NativeQueryBuilder()
                .withQuery(combinedQuery)
                .withPageable(pageable)
                .withHighlightQuery(new HighlightQuery(new Highlight(requiredHighlights), GroupIndex.class))
                .build();

        return runQuery(queryBuilder);
    }

    private Query buildSearchQuery(List<String> tokens) {
        return BoolQuery.of(q -> q.must(mb -> mb.bool(b -> {
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

    private Page<PostSearchResultDTO> runQuery(NativeQuery searchQuery) {
        var searchHits = elasticsearchTemplate.search(searchQuery, PostIndex.class, IndexCoordinates.of("post_index"));

        List<PostSearchResultDTO> results = new ArrayList<>();
        for (SearchHit<PostIndex> hit : searchHits.getSearchHits()) {
            PostIndex postIndex = hit.getContent();
            Map<String, List<String>> highlights = hit.getHighlightFields();

            PostSearchResultDTO resultDTO = new PostSearchResultDTO(
                    postIndex.getTitle(),
                    postIndex.getContent(),
                    postIndex.getLikeCount(),
                    postIndex.getCommentCount()
            );
            resultDTO.setHighlights(highlights);

            results.add(resultDTO);
        }

        return new PageImpl<>(results, searchQuery.getPageable(), searchHits.getTotalHits());
    }
}