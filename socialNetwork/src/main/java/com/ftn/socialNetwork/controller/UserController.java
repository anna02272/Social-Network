package com.ftn.socialNetwork.controller;

import com.ftn.socialNetwork.model.dto.JwtAuthenticationRequest;
import com.ftn.socialNetwork.model.dto.UserDTO;
import com.ftn.socialNetwork.model.dto.UserTokenState;
import com.ftn.socialNetwork.model.entity.ChangePassword;
import com.ftn.socialNetwork.model.entity.Post;
import com.ftn.socialNetwork.model.entity.User;
import com.ftn.socialNetwork.security.TokenUtils;
import com.ftn.socialNetwork.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@RestController
@RequestMapping("api/users")
public class UserController {

    @Autowired
    UserService userService;


    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    TokenUtils tokenUtils;
    private Set<String> loggedInUsers = new HashSet<>();

    private boolean isUserLoggedIn(String username) {
        return loggedInUsers.contains(username);
    }

    private void updateUserLoginStatus(String username, boolean isLoggedIn) {
        if (isLoggedIn) {
            loggedInUsers.add(username);
        } else {
            loggedInUsers.remove(username);
        }
    }


    @PostMapping("/signup")
    public ResponseEntity<UserDTO> create(@RequestBody @Validated UserDTO newUser){
      if (userService.existsByUsername(newUser.getUsername())) {
        return new ResponseEntity<>(null, HttpStatus.CONFLICT);
      }
      if (userService.existsByEmail(newUser.getEmail())) {
        return new ResponseEntity<>(null, HttpStatus.CONFLICT);
      }
        User createdUser = userService.createUser(newUser);

        if(createdUser == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
        }
        UserDTO userDTO = new UserDTO(createdUser);

        return new ResponseEntity<>(userDTO, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<UserTokenState> createAuthenticationToken(
            @RequestBody JwtAuthenticationRequest authenticationRequest, HttpServletResponse response) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
       authenticationRequest.getUsername(), authenticationRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String jwt = tokenUtils.generateToken(userDetails);
        int expiresIn = tokenUtils.getExpiredIn();

        updateUserLoginStatus(authenticationRequest.getUsername(), true);
        User user = userService.findByUsername(authenticationRequest.getUsername());
        user.setLastLogin(LocalDateTime.now());
        userService.updateUser(user);

        return ResponseEntity.ok(new UserTokenState(jwt, expiresIn));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            updateUserLoginStatus(authentication.getName(), false);

            SecurityContextHolder.getContext().setAuthentication(null);
        }

        return ResponseEntity.ok("Logged out successfully.");
    }


  @GetMapping("/find/{id}")
  public ResponseEntity<User> getUserById(@PathVariable Long id) throws ChangeSetPersister.NotFoundException {
    User user = userService.findOneById(id);
    return ResponseEntity.ok(user);
  }
    @GetMapping("/all")
    public List<User> loadAll() {
        return this.userService.findAll();
    }

    @GetMapping("/whoami")
    public User user(Principal user) {
        return this.userService.findByUsername(user.getName());
    }

    @PostMapping("/changePassword")
    public ResponseEntity<Void> changePassword(@RequestBody ChangePassword changePasswordRequest, Principal principal) {
        String username = principal.getName();

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                username, changePasswordRequest.getCurrentPassword()));

        if (!authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if (!changePasswordRequest.getNewPassword().equals(changePasswordRequest.getConfirmPassword())) {
            return ResponseEntity.badRequest().build();
        }

        userService.changePassword(username, changePasswordRequest.getNewPassword());

        return ResponseEntity.noContent().build();
    }
  @PutMapping("/update/{id}")
  public ResponseEntity<User> update(@PathVariable("id") Long userId, @RequestBody User user) throws ChangeSetPersister.NotFoundException {
    User existing = userService.findOneById(userId);
    if (existing == null) {
      return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }
    if (user.getProfileName() != null) {
      existing.setProfileName(user.getProfileName());
    }
    if (user.getFirstName() != null) {
      existing.setFirstName(user.getFirstName());
    }
    if (user.getLastName() != null) {
      existing.setLastName(user.getLastName());
    }
    if (user.getDescription() != null) {
      existing.setDescription(user.getDescription());
    }
    User updated = userService.updateUser(existing);
    return new ResponseEntity<>(updated, HttpStatus.OK);
  }


}
