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
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "groups_table")
@FilterDef(name = "suspendedGroupFilter", parameters = @ParamDef(name = "isSuspended", type = "boolean"))
@Filter(name = "suspendedGroupFilter", condition = "is_suspended = :isSuspended")
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column
    private String rules;

    @Column(nullable = false,  columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime creationDate;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean isSuspended;

    @Column
    private String suspendedReason;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL)
    private List<GroupAdmin> groupAdmin;

    @OneToMany
    private List<Post> post;

    @OneToMany
    private  List<GroupRequest> groupRequest;
//    @OneToMany
//    private List<Banned> banned;
    @Column
    private String pdfFile;

}
