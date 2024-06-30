package com.ftn.socialNetwork.indexmodel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "comment_index")
@Setting(settingPath = "/configuration/serbian-analyzer-config.json")
public class CommentIndex {

    @Field(type = FieldType.Keyword)
    private String id;

    @Field(type = FieldType.Text, store = true, name = "text", analyzer = "serbian_simple", searchAnalyzer = "serbian_simple")
    private String text;
}