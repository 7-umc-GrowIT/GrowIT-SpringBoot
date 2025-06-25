package umc.GrowIT.Server.domain;

import jakarta.persistence.*;
import lombok.*;
import umc.GrowIT.Server.domain.common.BaseEntity;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CreditProduct extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_id", nullable = false, unique = true) // 상훔 id는 고유해야함
    private String productId; // App Store에서 사용하는 상품 ID

    // 상품 이름
    @Column(name = "name", nullable = false)
    private String name;

    // 상품 가격
    @Column(name = "price", nullable = false)
    private Integer price;

    // 상품 설명
    @Column(name = "description", length = 255)
    private String description;

    // 크레딧 수량
    @Column(name = "credit_amount", nullable = false)
    private Integer creditAmount;

    // 활성화 여부
    @Column(name = "active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;
}
