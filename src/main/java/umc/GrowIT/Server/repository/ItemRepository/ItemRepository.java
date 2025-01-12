package umc.GrowIT.Server.repository.ItemRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.GrowIT.Server.domain.Item;
import umc.GrowIT.Server.domain.enums.ItemCategory;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long>, ItemRepositoryCustom {
    List<Item> findAllByCategory(ItemCategory category);

}
