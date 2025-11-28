package uk.ac.ed.acp.cw2.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "order_history")
@Data  // Lombok annotation to generate getters, setters, toString, equals, and hashCode methods automatically
@NoArgsConstructor  // Lombok annotation to generate a no-arguments constructor
public class OrderHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    private Double lat;
    private Double lng;
    private Double capacity;
    private Boolean cooling;
    private Boolean heating;
    private Double totalCost;
    private Integer totalMoves;
    private String droneId;

    private LocalDateTime createdAt = LocalDateTime.now();  // Default value can be set directly here

}
