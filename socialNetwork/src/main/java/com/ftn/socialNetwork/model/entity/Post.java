package com.ftn.socialNetwork.model.entity;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

import java.time.LocalDateTime;
import java.util.List;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "posts")
@FilterDef(name = "deletedPostFilter", parameters = @ParamDef(name = "isDeleted", type = "boolean"))
@Filter(name = "deletedPostFilter", condition = "is_deleted = :isDeleted")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime creationDate;

  @Column(nullable = false, columnDefinition = "boolean default false")
  private Boolean isDeleted ;
    @ManyToOne
    private User user;
    @OneToMany
    private List<Image> image;

  @OneToMany
  private List<Reaction> reactions;

  @OneToMany
  private List<Report> report;
  @OneToMany
  private List<Comment> comments;



}
