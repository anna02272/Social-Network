package com.ftn.socialNetwork.controller;

import com.ftn.socialNetwork.model.dto.JwtAuthenticationRequest;
import com.ftn.socialNetwork.model.dto.UserDTO;
import com.ftn.socialNetwork.model.dto.UserTokenState;
import com.ftn.socialNetwork.model.entity.User;
import com.ftn.socialNetwork.security.TokenUtils;
import com.ftn.socialNetwork.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletResponse;
import java.util.List;


@RestController
@RequestMapping("api/users")
public class UserController {

    @Autowired
    UserService userService;


    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    TokenUtils tokenUtils;

    @PostMapping("/signup")
    public ResponseEntity<UserDTO> create(@RequestBody @Validated UserDTO newUser){

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

        UserDetails user = (UserDetails) authentication.getPrincipal();
        String jwt = tokenUtils.generateToken(user);
        int expiresIn = tokenUtils.getExpiredIn();


        return ResponseEntity.ok(new UserTokenState(jwt, expiresIn));
    }

    @GetMapping("/all")
    public List<User> loadAll() {
        return this.userService.findAll();
    }

}
