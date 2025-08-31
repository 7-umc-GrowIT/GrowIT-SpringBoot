package umc.GrowIT.Server.service.itemService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.GrowIT.Server.converter.ItemConverter;
import umc.GrowIT.Server.domain.Item;
import umc.GrowIT.Server.domain.enums.ItemCategory;
import umc.GrowIT.Server.domain.enums.ItemStatus;
import umc.GrowIT.Server.repository.ItemRepository;
import umc.GrowIT.Server.util.S3Util;
import umc.GrowIT.Server.web.dto.ItemDTO.ItemResponseDTO;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class ItemQueryServiceImpl implements ItemQueryService {

    private final ItemRepository itemRepository;
    private final S3Util s3Util;

    @Override
    @Transactional(readOnly = true)
    public ItemResponseDTO.ItemListDTO getItemList(ItemCategory category, Long userId) {
        List<Item> itemList = itemRepository.findAllByCategory(category);

        // status 조회 - null 처리 추가
        Map<Long, ItemStatus> itemStatuses = new HashMap<>();
        itemList.forEach(item -> {
            ItemStatus status = itemRepository.findStatusByUserIdAndItemId(userId, item.getId());
            itemStatuses.put(item.getId(), status);  // null이어도 저장 가능
        });

        // 구매여부 조회
        Map<Long, Boolean> purchaseStatuses = itemList.stream()
                .collect(Collectors.toMap(
                        Item::getId,
                        item -> itemRepository.existsByUserItemsUserIdAndId(userId, item.getId())
                ));

        // Pre-signed URL 생성
        Map<Item, String> itemUrls = createItemPreSignedUrl(itemList);
        Map<Item, String> groItemUrls = createGroItemPreSignedUrl(itemList);

        return ItemConverter.toItemListDTO(itemList, userId, itemStatuses, purchaseStatuses, itemUrls, groItemUrls);
    }

    @Override
    @Transactional(readOnly = true)
    public ItemResponseDTO.ItemListDTO getUserOwnedItemList(ItemCategory category, Long userId) {
        // 기존의 잘 작동하던 코드 유지
        List<Item> userItems = itemRepository.findItemsByUserIdAndCategory(userId, category);

        // status 조회 - null 처리 추가
        Map<Long, ItemStatus> itemStatuses = new HashMap<>();
        userItems.forEach(item -> {
            ItemStatus status = itemRepository.findStatusByUserIdAndItemId(userId, item.getId());
            itemStatuses.put(item.getId(), status);  // null이어도 저장 가능
        });

        // 구매여부 조회
        Map<Long, Boolean> purchaseStatuses = userItems.stream()
                .collect(Collectors.toMap(
                        Item::getId,
                        item -> itemRepository.existsByUserItemsUserIdAndId(userId, item.getId())
                ));

        // Pre-signed URL 생성 로직만 추가
        Map<Item, String> itemUrls = createItemPreSignedUrl(userItems);
        Map<Item, String> groItemUrls = createGroItemPreSignedUrl(userItems);

        // converter에 URL 맵 전달
        return ItemConverter.toItemListDTO(userItems, userId, itemStatuses, purchaseStatuses, itemUrls, groItemUrls);
    }

    // 상점 리스트용 이미지의 Pre-signed URL 생성
    private Map<Item, String> createItemPreSignedUrl(List<Item> items) {
        return items.stream()
                .collect(Collectors.toMap(
                        item -> item,
                        item -> s3Util.toGetPresignedUrl(item.getImageKey(), Duration.ofMinutes(15))
                ));
    }

    // 그로 착용용 이미지의 Pre-signed URL 생성
    private Map<Item, String> createGroItemPreSignedUrl(List<Item> items) {
        return items.stream()
                .collect(Collectors.toMap(
                        item -> item,
                        item -> s3Util.toGetPresignedUrl(item.getGroImageKey(), Duration.ofMinutes(15))
                ));
    }
}



