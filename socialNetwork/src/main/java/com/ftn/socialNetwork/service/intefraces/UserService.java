package com.ftn.socialNetwork.service.intefraces;

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
   void changePassword(String username, String newPassword);
   boolean existsByUsername(String username);
   boolean existsByEmail(String email) ;
  List<User> searchUsersByNameOrLastName(String keyword);
  List<User> searchUsersByFirstNameAndLastName(String firstName, String lastName) ;
}

