package com.ftn.socialNetwork.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "friendRequests")
public class FriendRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private boolean approved;

    @Column(nullable = false, columnDefinition = "CREATEDAT DEFAULT CURRENT_CREATEDAT")
    private LocalDateTime created_at;
    @Column(nullable = false)
    private LocalDateTime at;
    @ManyToOne
    private User user;
}
