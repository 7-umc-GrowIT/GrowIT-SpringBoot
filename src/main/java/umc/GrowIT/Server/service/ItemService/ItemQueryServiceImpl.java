package umc.GrowIT.Server.service.ItemService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.GrowIT.Server.converter.ItemConverter;
import umc.GrowIT.Server.domain.Item;
import umc.GrowIT.Server.domain.enums.ItemCategory;
import umc.GrowIT.Server.repository.ItemRepository.ItemRepository;
import umc.GrowIT.Server.web.dto.ItemDTO.ItemResponseDTO;

import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemQueryServiceImpl implements ItemQueryService {
    private final ItemRepository itemRepository;

    @Override
    public ItemResponseDTO.ItemListDTO getItemList(ItemCategory category) {
        List<Item> itemList = itemRepository.findAllByCategory(category);
        return ItemConverter.toItemListDTO(itemList);
    }
}
