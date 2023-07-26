package com.ftn.socialNetwork.model.entity;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "groups_table")
@FilterDef(name = "deletedGroupFilter", parameters = @ParamDef(name = "isDeleted", type = "boolean"))
@Filter(name = "deletedGroupFilter", condition = "is_deleted = :isDeleted")
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false,  columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime creationDate;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean isSuspended;

    @Column(nullable = true, columnDefinition = "varchar(255) default ''")
    private String suspendedReason;

  @Column(nullable = false, columnDefinition = "boolean default false")
  private Boolean isDeleted ;

  @OneToOne(mappedBy = "group", cascade = CascadeType.ALL, optional = true)
  private GroupAdmin groupAdmin;



}
