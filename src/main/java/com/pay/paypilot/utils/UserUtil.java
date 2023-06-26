package com.pay.paypilot.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pay.paypilot.model.User;
import lombok.Data;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@Data
public class UserUtil {

    public String getAuthenticatedUserEmail(){

      UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

      return userDetails.getUsername();
    }

    public User currentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principle = authentication.getPrincipal();
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.convertValue(principle, User.class);
    }
}
