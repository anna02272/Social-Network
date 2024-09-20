package com.ftn.socialNetwork.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChangePassword{

    @Id
    private Long id;
    private String currentPassword;
    private String newPassword;
    private String confirmPassword;



}
