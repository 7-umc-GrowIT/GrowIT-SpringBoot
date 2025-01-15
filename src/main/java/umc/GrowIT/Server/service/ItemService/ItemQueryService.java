package umc.GrowIT.Server.service.ItemService;

import umc.GrowIT.Server.domain.enums.ItemCategory;
import umc.GrowIT.Server.web.dto.ItemDTO.ItemResponseDTO;

public interface ItemQueryService {
    public ItemResponseDTO.ItemListDTO getItemList(ItemCategory category, Long userId);
}
