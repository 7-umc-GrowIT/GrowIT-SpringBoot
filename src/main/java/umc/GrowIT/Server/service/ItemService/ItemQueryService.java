package umc.GrowIT.Server.service.ItemService;

import umc.GrowIT.Server.domain.enums.ItemCategory;
import umc.GrowIT.Server.web.dto.ItemDTO.ItemResponseDTO;

public interface ItemQueryService {
    ItemResponseDTO.ItemListDTO getItemList(ItemCategory itemCategory);
}
