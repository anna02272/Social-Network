package com.ftn.socialNetwork.model.entity;

import javax.persistence.*;

import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "images")
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = true)
    private String path;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

   @ManyToOne
    private Post post;

}
