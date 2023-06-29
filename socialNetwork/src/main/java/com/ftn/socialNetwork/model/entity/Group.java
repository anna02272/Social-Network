package com.ftn.socialNetwork.model.entity;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "groups_table")
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


  @JsonIgnore
    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL)
    private List<GroupAdmin> groupAdmin;




}
