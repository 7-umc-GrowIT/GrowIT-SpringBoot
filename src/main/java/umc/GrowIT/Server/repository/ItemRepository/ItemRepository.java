package umc.GrowIT.Server.repository.ItemRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import umc.GrowIT.Server.domain.Item;
import umc.GrowIT.Server.domain.UserItem;
import umc.GrowIT.Server.domain.enums.ItemCategory;
import umc.GrowIT.Server.domain.enums.ItemStatus;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findAllByCategory(ItemCategory category);

    // UserItem을 통해 현재 사용자의 특정 아이템 구매여부 판별
    boolean existsByUserItemsUserIdAndId(Long userId, Long itemId);

    Optional<Item> findByName(String name);

    //사용자가 보유한 아이템만 조회(userId, category)
    @Query("SELECT ui.item FROM UserItem ui WHERE ui.user.id = :userId AND ui.item.category = :category")
    List<Item> findItemsByUserIdAndCategory(Long userId, ItemCategory category);

    //현재 사용자가 보유중인 아이템의 status만 조회
    @Query("SELECT ui.status FROM UserItem ui WHERE ui.user.id = :userId AND ui.item.id = :itemId")
    ItemStatus findStatusByUserIdAndItemId(Long userId, Long itemId);


}
