package com.ftn.socialNetwork.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false, columnDefinition = "CREATIONDATE DEFAULT CURRENT_CREATIONDATE")
    private LocalDateTime creationDate;

    @ElementCollection
    private List<String> imagePaths;

    @ManyToOne
    private User user;
}
