package com.example.test.in;

import com.example.test.model.Role;
import com.example.test.model.User;
import com.example.test.repository.RoleRepository;
import com.example.test.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class In implements CommandLineRunner {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;


    public void run(String... arg) throws Exception {
        Role roleUser = new Role("ROLE_USER");
        Set<Role> userRoles = new HashSet<>();
        if (roleRepository.findByName("ROLE_USER") == null) {
            roleRepository.save(roleUser);
        }
        userRoles.add(roleUser);
        if (userRepository.findByUsername("user") == null &&
                userRepository.findByEmail("user@gmail.com") == null) {
            User userUser = new User("user@gmail.com", "user", passwordEncoder.encode("user"), userRoles);
            userRepository.save(userUser);
        }
    }
}