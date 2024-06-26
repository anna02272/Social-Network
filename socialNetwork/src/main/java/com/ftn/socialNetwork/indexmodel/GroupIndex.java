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
@Document(indexName = "group_index")
@Setting(settingPath = "/configuration/serbian-analyzer-config.json")
public class GroupIndex {

    @Id
    private String id;

    @Field(type = FieldType.Text, store = true, name = "name", analyzer = "serbian_simple", searchAnalyzer = "serbian_simple")
    private String name;

    @Field(type = FieldType.Text, store = true, name = "description", analyzer = "serbian_simple", searchAnalyzer = "serbian_simple")
    private String description;

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

    @Field(type = FieldType.Integer, store = true, name = "database_id")
    private Integer databaseId;
}
