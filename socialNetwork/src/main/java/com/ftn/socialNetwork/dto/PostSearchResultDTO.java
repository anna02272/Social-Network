package com.ftn.socialNetwork.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PostSearchResultDTO {
    private String title;
    private String content;
    private Integer likeCount;
    private Integer commentCount;

    public  PostSearchResultDTO(String title, String content, Integer likeCount, Integer commentCount) {
        this.title = title;
        this.content = content;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
    }

}
