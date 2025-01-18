package umc.GrowIT.Server.service.ItemService;

import umc.GrowIT.Server.web.dto.ItemDTO.ItemResponseDTO;

public interface ItemCommandService {
    ItemResponseDTO.PurchaseItemResponseDTO purchase(Long itemId, Long userId);
}
