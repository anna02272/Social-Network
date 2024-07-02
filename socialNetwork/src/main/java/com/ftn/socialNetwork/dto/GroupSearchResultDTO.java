package com.ftn.socialNetwork.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GroupSearchResultDTO {
    private String name;
    private String description;
    private String rules;
    private Integer postCount;
    private Float postAverageLikes;

    public GroupSearchResultDTO(String name, String description, String rules, Integer postCount, Float postAverageLikes) {
        this.name = name;
        this.description = description;
        this.rules = rules;
        this.postCount = postCount;
        this.postAverageLikes = postAverageLikes;
    }

}
