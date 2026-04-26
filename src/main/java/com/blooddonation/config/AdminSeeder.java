package com.blooddonation.config;

import com.blooddonation.entity.User;
import com.blooddonation.enums.Role;
import com.blooddonation.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Automatically seeds an Admin user into the database if one does not exist.
 * This is crucial for cloud deployments where the database starts completely empty.
 */
@Component
public class AdminSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminSeeder(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        // Check if an admin already exists
        if (userRepository.existsByEmail("admin2@blooddonation.com")) {
            return; // Admin already exists, do nothing
        }

        // Create the default admin
        User admin = User.builder()
                .name("Super Admin")
                .email("admin2@blooddonation.com")
                .phone("0000000000")
                .password(passwordEncoder.encode("admin123"))
                .role(Role.ADMIN)
                .isAvailable(false)
                .build();

        userRepository.save(admin);
        System.out.println("✅ [Admin Seeder] Default Admin account created successfully!");
    }
}
