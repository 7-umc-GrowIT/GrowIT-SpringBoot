package umc.GrowIT.Server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.GrowIT.Server.domain.User;
import umc.GrowIT.Server.domain.UserItem;

import java.util.Optional;

public interface UserItemRepository extends JpaRepository<UserItem, Long>  {
}