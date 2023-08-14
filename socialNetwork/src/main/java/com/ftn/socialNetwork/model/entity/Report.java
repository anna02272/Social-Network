package com.ftn.socialNetwork.model.entity;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "reports")
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private EReportReason reason;
    @Column(nullable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDate timestamp;
    @Column(nullable = false)
    private boolean accepted;
  @Column(nullable = false, columnDefinition = "boolean default false")
  private Boolean isDeleted ;
    @ManyToOne
    private User user;
    @ManyToOne
    private Post post;
    @ManyToOne
    private Comment comment;
    @ManyToOne
    private User reportedUser;
}
