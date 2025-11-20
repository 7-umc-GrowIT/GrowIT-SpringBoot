package umc.GrowIT.Server.service.itemService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import umc.GrowIT.Server.domain.Item;
import umc.GrowIT.Server.util.S3Util;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PreSignedUrlCacheServiceImpl implements PreSignedUrlCacheService {
    private final S3Util s3Util;

    // 상점 리스트용 이미지의 Pre-signed URL 생성
    @Cacheable(value = "imageUrls", key = "#items.!['image_' + id]")
    public Map<Long, String> createItemPreSignedUrl(List<Item> items) {
        return items.stream()
                .collect(Collectors.toMap(
                        Item::getId,
                        item -> s3Util.toGetPresignedUrl(item.getImageKey(), Duration.ofHours(24))
                ));
    }

    // 그로 착용용 이미지의 Pre-signed URL 생성
    @Cacheable(value = "groImageUrls", key = "#items.!['groImage_' + id]")
    public Map<Long, String> createGroItemPreSignedUrl(List<Item> items) {
        return items.stream()
                .collect(Collectors.toMap(
                        Item::getId,
                        item -> s3Util.toGetPresignedUrl(item.getGroImageKey(), Duration.ofHours(24))
                ));
    }

}
