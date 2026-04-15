package com.electromart.backend.config;

import com.electromart.backend.model.Role;
import com.electromart.backend.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    public DataInitializer(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) {
        seedRole("ROLE_USER");
        seedRole("ROLE_ADMIN");
    }

    private void seedRole(String name) {
        if (roleRepository.findByName(name).isEmpty()) {
            roleRepository.save(Role.builder().name(name).build());
            System.out.println("✔  Seeded role: " + name);
        }
    }
}