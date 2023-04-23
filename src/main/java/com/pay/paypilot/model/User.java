package com.pay.paypilot.model;

import com.pay.paypilot.enums.Gender;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Table(name = "user_table")
public class User implements UserDetails
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    private String firstName;
    private String lastName;
    private String otherName;
    private String uuid;
    @OneToOne
    @JoinColumn(name = "address",referencedColumnName = "addressId")
    private Address address;
    private String email;
    private String password;
    private Integer userBvn;
    private LocalDate dateOfBirth;
    private Gender gender;
    private Boolean isEmailVerified = false;
    private Boolean isBvnVerified = false;
    private Boolean isLocked = false;
    private String confirmationToken;
    private String phoneNumber;
    private int loginCount;
//    @Enumerated(EnumType.STRING)
    @OneToOne
    private Role role;
    private Boolean enable=false;
    @ManyToMany(cascade = {CascadeType.PERSIST}, fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "users_id", referencedColumnName = "userId"),
            inverseJoinColumns = @JoinColumn(name = "roles_id", referencedColumnName = "id"))
    private Collection<Role> roles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.getName()));
    }
    @Override
    public String getUsername() {
        return email;
    }
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    @Override
    public boolean isAccountNonLocked() {
        return !isLocked;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    @Override
    public boolean isEnabled() {
        return enable;
    }
}
