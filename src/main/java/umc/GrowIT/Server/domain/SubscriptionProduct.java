package umc.GrowIT.Server.domain;

import jakarta.persistence.*;
import lombok.*;
import umc.GrowIT.Server.domain.common.BaseEntity;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class SubscriptionProduct extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_id", nullable = false, unique = true)
    private String productId; // App Store 상품 ID

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "price", nullable = false)
    private Integer price;
}