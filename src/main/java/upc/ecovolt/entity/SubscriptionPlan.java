package upc.ecovolt.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "subscription_plans")
public class SubscriptionPlan extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_plan")
    private Long id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "monthly_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal monthlyPrice;

    @Column(name = "device_limit", nullable = false)
    private Integer deviceLimit;

    @Column(name = "support_level", nullable = false, length = 30)
    private String supportLevel;

    @Column(name = "billing_cycle", nullable = false, length = 20)
    private String billingCycle;

}
