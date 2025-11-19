package umc.GrowIT.Server.service.itemService;

import umc.GrowIT.Server.domain.Item;

import java.util.List;
import java.util.Map;

public interface PreSignedUrlCacheService {
    Map<Long, String> createItemPreSignedUrl(List<Item> items);

    Map<Long, String> createGroItemPreSignedUrl(List<Item> items);
}
