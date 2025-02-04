package umc.GrowIT.Server.service.ItemService;

import umc.GrowIT.Server.domain.enums.ItemStatus;
import umc.GrowIT.Server.web.dto.ItemDTO.ItemResponseDTO;
import umc.GrowIT.Server.web.dto.ItemEquipDTO.ItemEquipResponseDTO;

public interface ItemCommandService {
    ItemResponseDTO.PurchaseItemResponseDTO purchase(Long itemId, Long userId);

    ItemEquipResponseDTO updateItemStatus(Long userId, Long itemId, String status);
}
