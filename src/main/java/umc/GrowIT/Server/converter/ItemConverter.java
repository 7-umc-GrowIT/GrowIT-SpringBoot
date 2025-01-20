package umc.GrowIT.Server.converter;

import umc.GrowIT.Server.domain.Item;
import umc.GrowIT.Server.domain.User;
import umc.GrowIT.Server.domain.UserItem;
import umc.GrowIT.Server.domain.enums.ItemStatus;
import umc.GrowIT.Server.repository.ItemRepository.ItemRepository;
import umc.GrowIT.Server.web.dto.ItemDTO.ItemResponseDTO;

import java.util.List;
import java.util.stream.Collectors;

public class ItemConverter {

    //미션 1개 반환
    public static ItemResponseDTO.ItemDTO toItemDTO(Item item, Long userId, ItemRepository itemRepository) {
        return ItemResponseDTO.ItemDTO.builder()
                .id(item.getId())
                .name(item.getName())
                .price(item.getPrice())
                .imageUrl(item.getImageUrl())
                .category(item.getCategory().toString())
                .purchased(itemRepository.existsByUserItemsUserIdAndId(userId, item.getId()))
                .build();
    }

    //미션리스트 반환
    public static ItemResponseDTO.ItemListDTO toItemListDTO(List<Item> itemList, Long userId, ItemRepository itemRepository) {
        return ItemResponseDTO.ItemListDTO.builder()
                .itemList(itemList.stream()
                        .map(item -> toItemDTO(item, userId, itemRepository))
                        .collect(Collectors.toList()))
                .build();
    }

    public static UserItem toUserItem() {
        return UserItem.builder()
                .status(ItemStatus.UNEQUIPPED)
                .build()
                ;
    }

    public static ItemResponseDTO.PurchaseItemResponseDTO toPurchaseItemResponseDTO(UserItem userItem) {
        return ItemResponseDTO.PurchaseItemResponseDTO.builder()
                .itemId(userItem.getItem().getId())
                .itemName(userItem.getItem().getName())
                .build()
                ;
    }
}
