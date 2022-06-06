package me.metropanties.groupwork.service;

import me.metropanties.groupwork.entity.User;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public interface UserService {

    void saveUser(@NotNull User user);

    void deleteUser(@NotNull User user);

    void deleteUserByID(long userID);

    boolean addRole(long userID, @NotNull String roleName);

    boolean updateUserActivation(long userID, boolean activation);

    boolean exists(long userID);

    boolean exists(@NotNull String username);

    Optional<User> getUserByID(long userID);

    Optional<User> getUserByUsername(@NotNull String username);

    List<User> getAll();

}
