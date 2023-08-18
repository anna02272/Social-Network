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
@Table(name = "banned")
public class Banned {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDate timeStamp;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean isBlocked;
    @ManyToOne
    private GroupAdmin groupAdmin;
    @ManyToOne
    private Group group;
    @ManyToOne
    private  User bannedUser;
    @ManyToOne
    private  User user;
}
