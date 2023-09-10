package com.ftn.socialNetwork.model.entity;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "comments")
@FilterDef(name = "deletedCommentFilter", parameters = @ParamDef(name = "isDeleted", type = "boolean"))
@Filter(name = "deletedCommentFilter", condition = "is_deleted = :isDeleted")

public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String text;

    @Column(nullable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDate timeStamp;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private Boolean isDeleted ;

    @ManyToOne
    private User user;

    @ManyToOne
    private Post post;

  @OneToMany
  private List<Comment> replies;

  @ManyToOne
  private Comment parentComment;
  @OneToMany
  private List<Reaction> reactions;

  @OneToMany
  private List<Report> report;
}
