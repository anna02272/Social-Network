package com.ftn.socialNetwork.dto;

import co.elastic.clients.elasticsearch._types.query_dsl.RangeQuery;

import java.util.List;

public record SearchQueryDTO(
        List<String> keywords,
        String name,
        String description,
        String pdfContent,
        String rules,
        List<Integer> postAverageLikes,
        List<Integer> postCount,
        String operation
) {
}
