package upc.ecovolt.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "rooms")
public class Room extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_room")
    private Long id;

    @Column(name = "name", nullable = false, length = 50)
    private String name; // Ej: "Cocina", "Dormitorio Principal"

    @Column(name = "floor_number", nullable = false)
    private Integer floorNumber;

    @Column(name = "orientation", length = 20)
    private String orientation; // Ej: "North", "South"

    @Column(name = "area_sqm", precision = 10, scale = 2)
    private BigDecimal areaSqm;

    @Column(name = "room_type", length = 30)
    private String roomType; // Ej: "Social", "Private", "Service"

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_home", nullable = false)
    private Home home;
}
