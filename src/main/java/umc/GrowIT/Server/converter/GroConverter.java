package umc.GrowIT.Server.converter;

import umc.GrowIT.Server.domain.Gro;
import umc.GrowIT.Server.domain.Item;
import umc.GrowIT.Server.domain.User;
import umc.GrowIT.Server.web.dto.GroDTO.GroResponseDTO;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GroConverter {


    public static GroResponseDTO.CreateResponseDTO toGroResponseDTO(Gro gro) {
        return GroResponseDTO.CreateResponseDTO.builder()
                .id(gro.getId())
                .user_id(gro.getUser().getId())
                .name(gro.getName())
                .level(gro.getLevel())
                .build();
    }


    public static Gro toGro(User user, String name) {
        return Gro.builder()
                .user(user)
                .name(name)
                .level(1)
                .build();
    }

    public static GroResponseDTO.GroAndEquippedItemsDTO toGroAndEquippedItemsDTO(Gro gro, String groImageUrl, Map<Item, String> itemUrls) {
        // 1. GroDTO 생성
        GroResponseDTO.GroDTO groDTO = GroResponseDTO.GroDTO.builder()
                .id(gro.getId())
                .level(gro.getLevel())
                .groImageUrl(groImageUrl)
                .build();

        // 2. EquippedItemsDTO 리스트 생성
        List<GroResponseDTO.EquippedItemsDTO> equippedItems = itemUrls.entrySet().stream()
                .map(item -> GroResponseDTO.EquippedItemsDTO.builder()
                        .id(item.getKey().getId())
                        .name(item.getKey().getName())
                        .category(item.getKey().getCategory())
                        .itemImageUrl(item.getValue())
                        .build())
                .collect(Collectors.toList());

        // 3. GroAndEquippedItemsDTO 생성 후 반환
        return GroResponseDTO.GroAndEquippedItemsDTO.builder()
                .gro(groDTO)
                .equippedItems(equippedItems)
                .build();
    }
}
