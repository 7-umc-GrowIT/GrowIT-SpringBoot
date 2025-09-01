package umc.GrowIT.Server.domain;


import jakarta.persistence.*;
import lombok.*;
import umc.GrowIT.Server.domain.common.BaseEntity;
import umc.GrowIT.Server.domain.enums.ItemCategory;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 아이템 이름
    @Column(nullable = false, length = 50)
    private String name;

    // 아이템 가격
    @Column(nullable = false)
    private Integer price;

    // 아이템 카테고리
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ItemCategory category;

    // 상점용 이미지키
    @Column(nullable = false, columnDefinition = "TEXT")
    private String imageKey;

    // 그로용 이미지키
    @Column(nullable = false, columnDefinition = "TEXT")
    private String groImageKey;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
    private List<UserItem> userItems = new ArrayList<>();
}
