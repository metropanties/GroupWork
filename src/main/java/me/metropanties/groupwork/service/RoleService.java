package me.metropanties.groupwork.service;

import me.metropanties.groupwork.entity.Role;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public interface RoleService {

    void saveRole(@NotNull String roleName);

    void deleteRoleByID(long roleID);

    void deleteRole(@NotNull Role role);

    boolean exists(long roleID);

    boolean exists(@NotNull String name);

    Optional<Role> getRoleByID(long roleID);

    List<Role> getAll();

}
