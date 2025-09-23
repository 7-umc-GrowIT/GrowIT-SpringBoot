package umc.GrowIT.Server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import umc.GrowIT.Server.domain.Item;
import umc.GrowIT.Server.domain.enums.ItemCategory;
import umc.GrowIT.Server.domain.enums.ItemStatus;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {
    // 카테고리별 아이템 조회
    @Query("SELECT i FROM Item i WHERE i.category = :category ORDER BY i.price ASC")
    List<Item> findAllByCategoryOrderByPriceAtAsc(ItemCategory category);

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
