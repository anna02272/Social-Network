package com.ftn.socialNetwork.model.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String username;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false, unique = true)
    private String email;
    private LocalDateTime lastLogin;
    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;
    @Column(nullable = false)
    private EUserType type;
    @Column(nullable = true)
    private String description;
    @Column(nullable = true)
    private String profileName;
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Image image;
    @OneToMany
    private List<GroupAdmin> groupAdmins;
    @OneToMany
    private List<Post> post;
    @OneToMany
    private List<Reaction> reaction;
    @OneToMany
    private List<Report> report;
    @OneToMany
    private List<Comment> comments;
    @OneToMany
    private List<GroupRequest> groupRequest;
    @OneToMany
    private List<Banned> banned;
    @OneToMany
    private List<FriendRequest> sentFriendRequests;
    @OneToMany
    private List<FriendRequest> receivedFriendRequests;

}
