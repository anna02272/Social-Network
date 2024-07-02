package com.ftn.socialNetwork.indexmodel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "group_index")
@Setting(settingPath = "/configuration/serbian-analyzer-config.json")
public class GroupIndex {

    @Id
    private String id;

    @Field(type = FieldType.Text, store = true, name = "name", analyzer = "serbian_simple", searchAnalyzer = "serbian_simple")
    private String name;

    @Field(type = FieldType.Text, store = true, name = "description", analyzer = "serbian_simple", searchAnalyzer = "serbian_simple")
    private String description;

    @Field(type = FieldType.Text, store = true, name = "rules", analyzer = "serbian_simple", searchAnalyzer = "serbian_simple")
    private String rules;

    @Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second, store = true, name = "creationDate")
    private LocalDateTime creationDate;

    @Field(type = FieldType.Boolean, store = true, name = "isSuspended")
    private boolean isSuspended;

    @Field(type = FieldType.Text, store = true, name = "suspendedReason")
    private String suspendedReason;

    @Field(type = FieldType.Text, store = true, name = "content_sr", analyzer = "serbian_simple", searchAnalyzer = "serbian_simple")
    private String contentSr;

    @Field(type = FieldType.Text, store = true, name = "content_en", analyzer = "english", searchAnalyzer = "english")
    private String contentEn;

    @Field(type = FieldType.Text, store = true, name = "server_filename", index = false)
    private String serverFilename;

    @Field(type = FieldType.Text, store = true, name = "pdfFile", index = false)
    private String pdfFileUrl;

    @Field(type = FieldType.Integer, store = true, name = "postCount", index = false)
    private Integer postCount;

    @Field(type = FieldType.Float, store = true, name = "postAverageLikes", index = false)
    private Float postAverageLikes;

    @Field(type = FieldType.Integer, store = true, name = "totalLikes", index = false)
    private Integer totalLikes;

    @Field(type = FieldType.Integer, store = true, name = "numberOfPostsWithLikes", index = false)
    private Integer numberOfPostsWithLikes;

    private Map<String, List<String>> highlights;

}