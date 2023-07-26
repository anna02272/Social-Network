package com.ftn.socialNetwork.model.entity;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "groupAdmins")
public class GroupAdmin{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

  @JsonIgnore
    @OneToOne
    private Group group;

    @OneToOne
    private User user;
}
