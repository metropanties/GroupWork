package me.metropanties.groupwork.security;

import lombok.RequiredArgsConstructor;
import me.metropanties.groupwork.entity.Role;
import me.metropanties.groupwork.repository.RoleRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Example;

import javax.annotation.PostConstruct;
import java.util.Set;

@Configuration
@RequiredArgsConstructor
public class RoleConfig {

    private static final Set<Role> APPLICATION_ROLES = Set.of(
            new Role("ADMINISTRATOR"),
            new Role("USER")
    );

    private final RoleRepository roleRepository;

    private static RoleRepository repository;

    @PostConstruct
    public void init() {
        repository = roleRepository;
        APPLICATION_ROLES.forEach(RoleConfig::saveRole);
    }

    public static void saveRole(@NotNull Role role) {
        if (repository.exists(Example.of(role)))
            return;

        repository.save(role);
    }

}
