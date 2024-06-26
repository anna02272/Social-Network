package com.ftn.socialNetwork.security;

import com.ftn.socialNetwork.model.entity.User;
import com.ftn.socialNetwork.service.intefraces.UserService;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class WebSecurity {

    @Autowired
    private UserService userService;

    public boolean checkGroupId(Authentication authentication, HttpServletRequest request, int id) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userService.findByUsername(userDetails.getUsername());
        if(id == user.getId()) {
            return true;
        }
        return false;
    }
}
