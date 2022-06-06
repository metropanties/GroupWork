package me.metropanties.groupwork.service.impl;

import lombok.RequiredArgsConstructor;
import me.metropanties.groupwork.entity.Role;
import me.metropanties.groupwork.exception.RoleNotFound;
import me.metropanties.groupwork.repository.RoleRepository;
import me.metropanties.groupwork.service.RoleService;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public void saveRole(@NotNull String roleName) {
        if (exists(roleName))
            return;

        roleRepository.save(new Role(roleName));
    }

    @Override
    public void deleteRoleByID(long roleID) {
        if (!exists(roleID))
            throw new RoleNotFound(String.format("Role with id of %s not found!", roleID));

        roleRepository.deleteById(roleID);
    }

    @Override
    public void deleteRole(@NotNull Role role) {
        if (!roleRepository.exists(Example.of(role)))
            throw new RoleNotFound("Role not found!");

        roleRepository.delete(role);
    }

    @Override
    public boolean exists(long roleID) {
        return roleRepository.existsById(roleID);
    }

    @Override
    public boolean exists(@NotNull String name) {
        return roleRepository.existsByName(name);
    }

    @Override
    public Optional<Role> getRoleByID(long roleID) {
        return roleRepository.findById(roleID);
    }

    @Override
    public List<Role> getAll() {
        return roleRepository.findAll();
    }

}
