package me.metropanties.groupwork.controller.controllers;

import lombok.RequiredArgsConstructor;
import me.metropanties.groupwork.controller.ControllerConstants;
import me.metropanties.groupwork.entity.Role;
import me.metropanties.groupwork.exception.RoleNotFound;
import me.metropanties.groupwork.service.RoleService;
import me.metropanties.groupwork.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(ControllerConstants.API_ENDPOINT + "/admin")
@PreAuthorize("hasAuthority('ADMINISTRATOR')")
@RequiredArgsConstructor
public class AdminController {

    private final RoleService roleService;
    private final UserService userService;

    /*
     * Role
     */

    @PostMapping("/roles/{roleName}")
    public ResponseEntity<Object> createRole(@PathVariable String roleName) {
        if (roleName == null) {
            Map<String, Object> body = Map.of(
                    "message", "Role name cannot be null!",
                    "code", HttpStatus.BAD_REQUEST.value()
            );
            return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
        }

        roleName = roleName.toLowerCase(Locale.ROOT);
        roleService.saveRole(roleName);
        Map<String, Object> body = Map.of(
                "message", String.format("Successfully created %s role!", roleName),
                "code", HttpStatus.CREATED.value()
        );
        return new ResponseEntity<>(body, HttpStatus.CREATED);
    }

    @PostMapping("/roles/{roleID}")
    public ResponseEntity<Object> deleteRole(@PathVariable long roleID) {
        try {
            roleService.deleteRoleByID(roleID);
            Map<String, Object> body = Map.of(
                    "message", "Successfully deleted role!",
                    "code", HttpStatus.OK.value()
            );
            return new ResponseEntity<>(body, HttpStatus.OK);
        } catch (RoleNotFound e) {
            Map<String, Object> body = Map.of(
                    "message", String.format("Role with id of %s not found!", roleID),
                    "code", HttpStatus.NOT_FOUND.value()
            );
            return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/roles/{roleID}")
    public ResponseEntity<Object> getRole(@PathVariable long roleID) {
        Optional<Role> role = roleService.getRoleByID(roleID);
        if (role.isPresent()) {
            Map<String, Object> body = Map.of(
                    "role", role.get(),
                    "code", HttpStatus.FOUND.value()
            );
            return new ResponseEntity<>(body, HttpStatus.FOUND);
        } else {
            Map<String, Object> body = Map.of(
                    "message", String.format("Role with id of %s not found!", roleID),
                    "code", HttpStatus.NOT_FOUND.value()
            );
            return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/roles")
    public ResponseEntity<Object> getAllRoles() {
        Map<String, Object> body = Map.of(
                "roles", roleService.getAll(),
                "code", HttpStatus.OK.value()
        );
        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    /*
     * User
     */

    @PatchMapping("/users/{userid}/{roleName}")
    public ResponseEntity<Object> addRoleToUser(@PathVariable long userid, @PathVariable String roleName) {
        boolean added = userService.addRole(userid, roleName);
        if (added) {
            Map<String, Object> body = Map.of(
                    "message", "Successfully added role to user!",
                    "code", HttpStatus.OK.value()
            );
            return new ResponseEntity<>(body, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("/users/{userID}")
    public ResponseEntity<Object> updateUserActivation(@PathVariable long userID, @RequestParam boolean activation) {
        boolean disabled = userService.updateUserActivation(userID, activation);
        if (disabled) {
            Map<String, Object> body = Map.of(
                    "message", "Successfully disabled user.",
                    "code", HttpStatus.OK.value()
            );
            return new ResponseEntity<>(body, HttpStatus.OK);
        } else {
            Map<String, Object> body = Map.of(
                    "message", "Successfully disabled user.",
                    "code", HttpStatus.NOT_FOUND.value()
            );
            return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/users")
    public ResponseEntity<Object> getAllUsers() {
        return new ResponseEntity<>(Map.of(
                "users", userService.getAll(),
                "code", HttpStatus.OK.value()
        ), HttpStatus.OK);
    }

}
