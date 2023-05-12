package com.ftn.socialNetwork.service;

import com.ftn.socialNetwork.model.dto.UserDTO;
import com.ftn.socialNetwork.model.entity.User;

import java.util.List;

public interface UserService {

    User createUser(User user);
    User createUser(UserDTO userDTO);
    User updateUser(User user);
    User deleteUser(Long id);
    User findOneById(Long id);
    User findByUsername(String username);
    List<User> findAll();

}

