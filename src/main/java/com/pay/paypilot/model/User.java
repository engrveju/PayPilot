package com.pay.paypilot.model;

import com.pay.paypilot.enums.Roles;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.Duration;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User extends Person{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private Boolean isVerified;
    private int loginAttempts;
    private Duration duration;
    private Boolean isEnabled;
    private Boolean isLocked;
    private Roles role;
}
