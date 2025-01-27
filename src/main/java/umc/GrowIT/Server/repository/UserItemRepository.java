package umc.GrowIT.Server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import umc.GrowIT.Server.domain.Item;
import umc.GrowIT.Server.domain.User;
import umc.GrowIT.Server.domain.UserItem;

import java.util.List;
import java.util.Optional;

public interface UserItemRepository extends JpaRepository<UserItem, Long>  {
    Optional<UserItem> findByUserAndItem(User user, Item item);

    @Query("SELECT ui FROM UserItem ui JOIN FETCH ui.item WHERE ui.user.id = :userId")
    List<UserItem> findAllWithItemsByUserId(Long userId);
}