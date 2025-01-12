package umc.GrowIT.Server.converter;

import umc.GrowIT.Server.domain.Item;
import umc.GrowIT.Server.web.dto.ItemDTO.ItemResponseDTO;

import java.util.List;
import java.util.stream.Collectors;

public class ItemConverter {

    //미션 1개 반환
    public static ItemResponseDTO.ItemDTO toItemDTO(Item item) {
        return ItemResponseDTO.ItemDTO.builder()
                .id(item.getId())
                .name(item.getName())
                .price(item.getPrice())
                .imageUrl(item.getImageUrl())
                .category(item.getCategory().toString())
                .purchased(false)
                .build();
    }

    //미션리스트 반환
    public static ItemResponseDTO.ItemListDTO toItemListDTO(List<Item> itemList) {
        return ItemResponseDTO.ItemListDTO.builder()
                .itemList(itemList.stream()
                        .map(ItemConverter::toItemDTO)
                        .collect(Collectors.toList()))
                .build();
    }
}
