package com.pay.paypilot;
;
import com.pay.paypilot.enums.Authorities;
import com.pay.paypilot.enums.Roles;
import com.pay.paypilot.model.Authority;
import com.pay.paypilot.model.Role;
import com.pay.paypilot.model.User;
import com.pay.paypilot.repository.AuthorityRepository;
import com.pay.paypilot.repository.RoleRepository;
import com.pay.paypilot.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class InitialUserSetUp {
    private final AuthorityRepository authorityRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;


    @EventListener
    @Transactional
    public void onApplicationEvent(ApplicationReadyEvent event) {
        System.out.println("From Application ready event....");
        Authority readAuthority = createAuthority(Authorities.READ_AUTHORITY.name());
        Authority writeAuthority = createAuthority(Authorities.WRITE_AUTHORITY.name());
        Authority deleteAuthority = createAuthority(Authorities.DELETE_AUTHORITY.name());
        createRole(Roles.ROLE_USER.name(), Collections.singletonList(readAuthority));
        createRole(Roles.ROLE_ADMIN.name(), Arrays.asList(readAuthority, writeAuthority));
        Role roleSuperAdmin = createRole(Roles.ROLE_SUPER_ADMIN.name(), Arrays.asList(readAuthority, writeAuthority, deleteAuthority));
        if (roleSuperAdmin == null) return;

        User superAdminUser = new User();
        superAdminUser.setFirstName("Super");
        superAdminUser.setLastName("Super");
        superAdminUser.setEmail("super@admin.com");
        superAdminUser.setPassword(bCryptPasswordEncoder.encode("123456789"));
        superAdminUser.setRoles(List.of(roleSuperAdmin));

        User storedSuperUser = userRepository.findByEmail("super@admin.com").orElse(null);
        if(storedSuperUser == null) {
            userRepository.save(superAdminUser);
        }
    }
    @Transactional
    Authority createAuthority(String authorityName) {
        Authority authority = authorityRepository.findByName(authorityName);
        if (authority == null) {
            authority = new Authority(authorityName);
            authorityRepository.save(authority);
        }
        return authority;
    }
    @Transactional
    Role createRole(String roleName, List<Authority> authorities) {
        Role role = roleRepository.findByName(roleName);
        if (role == null) {
            role = new Role(roleName);
            role.setAuthorities(authorities);
            roleRepository.save(role);
        }
        return role;
    }
}
