package umc.GrowIT.Server.converter;

import umc.GrowIT.Server.domain.Item;
import umc.GrowIT.Server.domain.User;
import umc.GrowIT.Server.domain.UserItem;
import umc.GrowIT.Server.domain.enums.ItemStatus;
import umc.GrowIT.Server.repository.ItemRepository.ItemRepository;
import umc.GrowIT.Server.web.dto.ItemDTO.ItemResponseDTO;
import umc.GrowIT.Server.web.dto.ItemEquipDTO.ItemEquipResponseDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ItemConverter {

    //아이템 1개 반환
    public static ItemResponseDTO.ItemDTO toItemDTO(
            Item item,
            Long userId,
            ItemRepository itemRepository,
            String itemUrl,
            String groItemUrl) {

        ItemStatus status = itemRepository.findStatusByUserIdAndItemId(userId, item.getId());

        return ItemResponseDTO.ItemDTO.builder()
                .id(item.getId())
                .name(item.getName())
                .price(item.getPrice())
                .imageUrl(itemUrl)
                .groImageUrl(groItemUrl)
                .shopBackgroundColor(item.getShopBackgroundColor())
                .category(item.getCategory().toString())
                .status(status != null ? status.toString() : null)
                .purchased(itemRepository.existsByUserItemsUserIdAndId(userId, item.getId()))
                .build();
    }

    public static ItemResponseDTO.ItemListDTO toItemListDTO(
            List<Item> itemList,
            Long userId,
            ItemRepository itemRepository,
            Map<Item, String> itemUrls,
            Map<Item, String> groItemUrls) {

        return ItemResponseDTO.ItemListDTO.builder()
                .itemList(itemList.stream()
                        .map(item -> toItemDTO(
                                item,
                                userId,
                                itemRepository,
                                itemUrls.get(item),
                                groItemUrls.get(item)))
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

    public static ItemEquipResponseDTO itemEquipDTO(UserItem userItem){


        return ItemEquipResponseDTO.builder()
                .itemId(userItem.getItem().getId())
                .name(userItem.getItem().getName())
                .category(userItem.getItem().getCategory().name())
                .status(userItem.getStatus().name())
                .updatedAt(LocalDateTime.now().toString())
                .build();

    }
}
