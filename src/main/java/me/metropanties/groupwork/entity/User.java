package me.metropanties.groupwork.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@NoArgsConstructor
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String password;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    private Boolean disabled = false;

    @CreatedDate
    @JsonProperty("created_at")
    private Date createdAt = new Date();

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public boolean hasRole(@NotNull Role role) {
        return this.roles.contains(role);
    }

    public void addRole(@NotNull Role role) {
        if (hasRole(role))
            return;

        this.roles.add(role);
    }

    public void removeRole(@NotNull Role role) {
        if (!hasRole(role))
            return;

        this.roles.remove(role);
    }

    @JsonProperty("profile_picture")
    public String getProfilePicture() {
        return String.format("http://localhost:8080/api/v1/files/%s-%s.png", this.id, this.username);
    }

}
