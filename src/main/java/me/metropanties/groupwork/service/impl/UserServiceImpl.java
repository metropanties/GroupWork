package me.metropanties.groupwork.service.impl;

import lombok.RequiredArgsConstructor;
import me.metropanties.groupwork.entity.Role;
import me.metropanties.groupwork.entity.User;
import me.metropanties.groupwork.exception.UsernameAlreadyExists;
import me.metropanties.groupwork.repository.RoleRepository;
import me.metropanties.groupwork.repository.UserRepository;
import me.metropanties.groupwork.service.UserService;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Example;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public void saveUser(@NotNull User user) {
        String username = user.getUsername();
        if (exists(username))
            throw new UsernameAlreadyExists(String.format("%s username is already in use!", username));

        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        user.addRole(roleRepository.findByName("USER").orElseThrow());
        userRepository.save(user);
    }

    @Override
    public void deleteUser(@NotNull User user) {
        if (!userRepository.exists(Example.of(user)))
            return;

        userRepository.delete(user);
    }

    @Override
    public void deleteUserByID(long userID) {
        if (!exists(userID))
            return;

        userRepository.deleteById(userID);
    }

    @Override
    public boolean addRole(long userID, @NotNull String roleName) {
        return getUserByID(userID).map(user -> {
            Role role = roleRepository.findByName(roleName).orElse(null);
            if (role == null)
                return false;

            user.addRole(role);
            userRepository.save(user);
            return true;
        }).orElse(false);
    }

    @Override
    public boolean updateUserActivation(long userID, boolean activation) {
        return getUserByID(userID).map(user -> {
            user.setDisabled(activation);
            userRepository.save(user);
            return true;
        }).orElse(false);
    }

    @Override
    public boolean exists(long userID) {
        return userRepository.existsById(userID);
    }

    @Override
    public boolean exists(@NotNull String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public Optional<User> getUserByID(long userID) {
        return userRepository.findById(userID);
    }

    @Override
    public Optional<User> getUserByUsername(@NotNull String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return getUserByUsername(username)
                .map(user -> new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), user.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority(role.getName())).toList()))
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User with the username of %s not found!", username)));
    }

}
