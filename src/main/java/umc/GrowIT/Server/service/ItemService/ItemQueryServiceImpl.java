package umc.GrowIT.Server.service.ItemService;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.GrowIT.Server.converter.ItemConverter;
import umc.GrowIT.Server.domain.Item;
import umc.GrowIT.Server.domain.enums.ItemCategory;
import umc.GrowIT.Server.repository.ItemRepository.ItemRepository;
import umc.GrowIT.Server.repository.UserItemRepository;
import umc.GrowIT.Server.web.dto.ItemDTO.ItemResponseDTO;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class ItemQueryServiceImpl implements ItemQueryService {

    private final AmazonS3 amazonS3;
    private final ItemRepository itemRepository;
    private final UserItemRepository userItemRepository;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    @Override
    @Transactional(readOnly = true)
    public ItemResponseDTO.ItemListDTO getItemList(ItemCategory category, Long userId) {
        List<Item> itemList = itemRepository.findAllByCategory(category);

        // 각 아이템에 대한 Pre-signed URL 생성
        Map<Item, String> itemUrls = createItemPreSignedUrl(itemList);
        Map<Item, String> groItemUrls = createGroItemPreSignedUrl(itemList);

        return ItemConverter.toItemListDTO(itemList, userId, itemRepository, itemUrls, groItemUrls);
    }

    @Override
    @Transactional(readOnly = true)
    public ItemResponseDTO.ItemListDTO getUserOwnedItemList(ItemCategory category, Long userId) {
        // 기존의 잘 작동하던 코드 유지
        List<Item> userItems = itemRepository.findItemsByUserIdAndCategory(userId, category);

        // Pre-signed URL 생성 로직만 추가
        Map<Item, String> itemUrls = createItemPreSignedUrl(userItems);
        Map<Item, String> groItemUrls = createGroItemPreSignedUrl(userItems);

        // converter에 URL 맵 전달
        return ItemConverter.toItemListDTO(userItems, userId, itemRepository, itemUrls, groItemUrls);
    }

    // 상점 리스트용 이미지의 Pre-signed URL 생성
    private Map<Item, String> createItemPreSignedUrl(List<Item> items) {
        return items.stream()
                .collect(Collectors.toMap(
                        item -> item,
                        item -> {
                            Date expiration = new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(15));
                            GeneratePresignedUrlRequest generatePresignedUrlRequest =
                                    new GeneratePresignedUrlRequest(bucketName, item.getImageKey())
                                            .withMethod(HttpMethod.GET)
                                            .withExpiration(expiration);
                            return amazonS3.generatePresignedUrl(generatePresignedUrlRequest).toString();
                        }
                ));
    }

    // 그로 착용용 이미지의 Pre-signed URL 생성
    private Map<Item, String> createGroItemPreSignedUrl(List<Item> items) {
        return items.stream()
                .collect(Collectors.toMap(
                        item -> item,
                        item -> {
                            Date expiration = new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(15));
                            GeneratePresignedUrlRequest generatePresignedUrlRequest =
                                    new GeneratePresignedUrlRequest(bucketName, item.getGroImageKey())
                                            .withMethod(HttpMethod.GET)
                                            .withExpiration(expiration);
                            return amazonS3.generatePresignedUrl(generatePresignedUrlRequest).toString();
                        }
                ));
    }
}
