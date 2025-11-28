package uk.ac.ed.acp.cw2.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Data  // Lombok annotation to generate getters, setters, toString, equals, and hashCode methods automatically
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private String name;

    public User() {}
    public User(String email, String password, String name) { this.email = email; this.password = password; this.name = name; }

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<OrderHistory> orders = new ArrayList<>();

    @JsonIgnore  // Don't expose password in JSON
    public String getPassword() {
        return password;
    }
}
