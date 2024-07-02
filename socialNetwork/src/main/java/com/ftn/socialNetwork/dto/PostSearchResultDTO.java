package com.ftn.socialNetwork.dto;

import com.ftn.socialNetwork.indexmodel.CommentIndex;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class PostSearchResultDTO {
    private String title;
    private String content;
    private Integer likeCount;
    private Integer commentCount;
    private Map<String, List<String>> highlights;

    public  PostSearchResultDTO(String title, String content, Integer likeCount, Integer commentCount) {
        this.title = title;
        this.content = content;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
    }

}
