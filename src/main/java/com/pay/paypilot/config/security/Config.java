package com.pay.paypilot.config.security;

import com.pay.paypilot.model.Authority;
import com.pay.paypilot.model.Role;
import com.pay.paypilot.model.User;
import com.pay.paypilot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Collection;
import java.util.HashSet;
@Configuration
@RequiredArgsConstructor
public class Config {

    private final UserRepository userRepository;
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public Collection<? extends GrantedAuthority> getAuthorities(User user) {
        Collection<GrantedAuthority> authorities = new HashSet<>();
        Collection<Role> roles = user.getRoles();
        Collection<Authority> authorityCollection = new HashSet<>();
        if (roles == null) return  authorities;
        roles.forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
            authorityCollection.addAll(role.getAuthorities());
        });
        authorityCollection.forEach(authority -> {
            authorities.add(new SimpleGrantedAuthority(authority.getName()));
        });
        return authorities;
    }
}
