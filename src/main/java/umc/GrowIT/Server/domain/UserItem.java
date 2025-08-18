package umc.GrowIT.Server.domain;


import jakarta.persistence.*;
import lombok.*;
import umc.GrowIT.Server.domain.common.BaseEntity;
import umc.GrowIT.Server.domain.enums.ItemStatus;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserItem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ItemStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
//    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;


    public void setUser(User user) {
        this.user = user;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public void setStatus(ItemStatus status) { this.status = status; }

    // 착용상태(status)를 바꾸는 메서드
    public void toggleStatus() {
        System.out.println("변경전 상태 : " + this.status);
        if (this.status == ItemStatus.EQUIPPED) {
            this.status = ItemStatus.UNEQUIPPED;
        } else {
            this.status = ItemStatus.EQUIPPED;
        }
        System.out.println("변경후 상태 : " + this.status);
    }
}
