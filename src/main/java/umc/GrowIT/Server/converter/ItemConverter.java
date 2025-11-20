package umc.GrowIT.Server.converter;

import umc.GrowIT.Server.domain.Item;
import umc.GrowIT.Server.domain.User;
import umc.GrowIT.Server.domain.UserItem;
import umc.GrowIT.Server.domain.enums.ItemStatus;
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
            ItemStatus status,
            boolean isPurchased,
            String itemUrl,
            String groItemUrl) {

        return ItemResponseDTO.ItemDTO.builder()
                .id(item.getId())
                .name(item.getName())
                .price(item.getPrice())
                .imageUrl(itemUrl)
                .groImageUrl(groItemUrl)
                .category(item.getCategory().toString())
                .status(status != null ? status.toString() : null)
                .purchased(isPurchased)
                .build();
    }

    public static ItemResponseDTO.ItemListDTO toItemListDTO(
            List<Item> itemList,
            Long userId,
            Map<Long, ItemStatus> itemStatuses,
            Map<Long, Boolean> purchaseStatuses,
            Map<Long, String> itemUrls,
            Map<Long, String> groItemUrls) {

        return ItemResponseDTO.ItemListDTO.builder()
                .itemList(itemList.stream()
                        .map(item -> toItemDTO(
                                item,
                                userId,
                                itemStatuses.get(item.getId()),
                                purchaseStatuses.get(item.getId()),
                                itemUrls.get(item.getId()),
                                groItemUrls.get(item.getId())))
                        .collect(Collectors.toList()))
                .build();
    }

    public static UserItem toUserItem(User user, Item item) {
        return UserItem.builder()
                .status(ItemStatus.UNEQUIPPED)
                .user(user)
                .item(item)
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
