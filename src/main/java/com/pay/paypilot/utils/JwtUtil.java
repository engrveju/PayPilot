package com.pay.paypilot.utils;

import com.pay.paypilot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class JwtUtil {
    private final UserRepository usersRepository;

    public UserDetails loadByUsername(String email) throws UsernameNotFoundException {
        return (UserDetails) usersRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException("Not Found"));
    }

    public String loggedUserMail(){
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
