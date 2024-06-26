package com.ftn.socialNetwork.service.implementation;

import com.ftn.socialNetwork.model.dto.UserDTO;
import com.ftn.socialNetwork.model.entity.EUserType;
import com.ftn.socialNetwork.model.entity.User;
import com.ftn.socialNetwork.repository.UserRepository;
import com.ftn.socialNetwork.service.intefraces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }
    @Override
    public User createUser(UserDTO userDTO) {

        Optional<User> user = userRepository.findFirstByUsername(userDTO.getUsername());

        if(user.isPresent()){
            return null;
        }

        User newUser = new User();
        newUser.setUsername(userDTO.getUsername());
        newUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        newUser.setEmail(userDTO.getEmail());
        newUser.setFirstName(userDTO.getFirstName());
        newUser.setLastName(userDTO.getLastName());
        newUser.setType(EUserType.USER);
        newUser = userRepository.save(newUser);

        return newUser;
    }

    @Override
    public User updateUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User deleteUser(Long id) {
        User user = findOneById(id);
        if (user != null) {
            userRepository.deleteById(id);
        }
        return user;
    }

    @Override
    public User findOneById(Long id) {
        return userRepository.findById(id).orElse(null);
    }


    @Override
    public User findByUsername(String username) {
        Optional<User> user = userRepository.findFirstByUsername(username);
        if (!user.isEmpty()) {
            return user.get();
        }
        return null;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }
    @Override
    public void changePassword(String username, String newPassword) {
        User user = userRepository.findByUsername(username);

        user.setPassword(passwordEncoder.encode(newPassword));

        userRepository.save(user);
    }
  @Override
  public boolean existsByUsername(String username) {
    return userRepository.existsByUsername(username);
  }
  @Override
  public boolean existsByEmail(String email) {
    return userRepository.existsByEmail(email);
  }
  @Override
  public List<User> searchUsersByNameOrLastName(String keyword) {
    List<User> users = userRepository.findAll();
    List<User> foundUsers = new ArrayList<>();

    for (User user : users) {
      if (user.getFirstName().toLowerCase().contains(keyword.toLowerCase()) ||
        user.getLastName().toLowerCase().contains(keyword.toLowerCase())) {
        foundUsers.add(user);
      }
    }

    return foundUsers;
  }
  @Override
  public List<User> searchUsersByFirstNameAndLastName(String firstName, String lastName) {
    List<User> users = userRepository.findAll();
    List<User> foundUsers = new ArrayList<>();

    for (User user : users) {
      if (user.getFirstName().toLowerCase().contains(firstName.toLowerCase()) &&
        user.getLastName().toLowerCase().contains(lastName.toLowerCase())) {
        foundUsers.add(user);
      }
    }

    return foundUsers;
  }
}
