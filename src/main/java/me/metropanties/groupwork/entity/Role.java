package me.metropanties.groupwork.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Locale;

@Entity
@Table(name = "roles")
@NoArgsConstructor
@Data
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    public Role(String name) {
        this.name = name.toUpperCase(Locale.ROOT);
    }

}
