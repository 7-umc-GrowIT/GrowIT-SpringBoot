package umc.GrowIT.Server.service.itemService;

import umc.GrowIT.Server.domain.enums.ItemCategory;
import umc.GrowIT.Server.web.dto.ItemDTO.ItemResponseDTO;

public interface ItemQueryService {
    public ItemResponseDTO.ItemListDTO getItemList(ItemCategory category, Long userId);

    public ItemResponseDTO.ItemListDTO getUserOwnedItemList(ItemCategory category, Long userId);
}
