package umc.GrowIT.Server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import umc.GrowIT.Server.domain.Item;
import umc.GrowIT.Server.domain.User;
import umc.GrowIT.Server.domain.UserItem;
import umc.GrowIT.Server.domain.enums.ItemCategory;
import umc.GrowIT.Server.domain.enums.ItemStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserItemRepository extends JpaRepository<UserItem, Long>  {
    Optional<UserItem> findByUserAndItem(User user, Item item);

    @Query("SELECT ui FROM UserItem ui JOIN FETCH ui.item WHERE ui.user.id = :userId AND ui.status = 'EQUIPPED'")
    List<UserItem> findEquippedItemsByUserId(@Param("userId") Long userId);

    //해당 아이템을 사용자가 보유했는지 (userId, itemId)
    Optional<UserItem> findByUserIdAndItemId(Long userId, Long itemId);

    @Query("SELECT ui.item FROM UserItem ui WHERE ui.user.id = :userId AND ui.item.category = :category")
    List<Item> findItemsByUserIdAndCategory(@Param("userId") Long userId, @Param("category") ItemCategory category);


    @Query("SELECT ui FROM UserItem ui " +
            "JOIN ui.item i " +
            "WHERE ui.user.id = :userId " +
            "AND i.category = :category " +
            "AND ui.status = :status")
    Optional<UserItem> findByUserIdAndItemCategoryAndStatus(
            @Param("userId") Long userId,
            @Param("category") ItemCategory category,
            @Param("status") ItemStatus status
    );


    @Modifying
    @Query("DELETE FROM UserItem ui WHERE ui.user.id = :userId")
    void deleteByUserId(@Param("userId") Long userId);

    // TODO 회원탈퇴 API 네이티브 삭제 메소드 - 추후 확인 필요
//    @Modifying
//    @Query(value = "DELETE FROM user_item WHERE user_id = :userId", nativeQuery = true)
//    void deleteByUserIdNative(@Param("userId") Long userId);
}