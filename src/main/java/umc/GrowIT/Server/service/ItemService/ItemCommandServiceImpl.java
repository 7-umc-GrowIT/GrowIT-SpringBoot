package umc.GrowIT.Server.service.ItemService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.GrowIT.Server.apiPayload.code.status.ErrorStatus;
import umc.GrowIT.Server.apiPayload.exception.ItemHandler;
import umc.GrowIT.Server.converter.ItemConverter;
import umc.GrowIT.Server.converter.UserConverter;
import umc.GrowIT.Server.domain.Item;
import umc.GrowIT.Server.domain.User;
import umc.GrowIT.Server.domain.UserItem;
import umc.GrowIT.Server.repository.ItemRepository.ItemRepository;
import umc.GrowIT.Server.repository.UserItemRepository;
import umc.GrowIT.Server.repository.UserRepository;
import umc.GrowIT.Server.web.dto.ItemDTO.ItemResponseDTO;

@Service
@RequiredArgsConstructor
public class ItemCommandServiceImpl implements ItemCommandService {

    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final UserItemRepository userItemRepository;

    @Override
    public ItemResponseDTO.PurchaseItemResponseDTO purchase(Long itemId, Long userId) {
        // 1. 사용자 조회하고 없으면 오류
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ItemHandler(ErrorStatus.USER_NOT_FOUND));

        // 2. 아이템 조회하고 없으면 오류
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemHandler(ErrorStatus.ITEM_NOT_FOUND));

        // 3. 사용자의 현재 보유 중인 크레딧과 아이템의 가격을 비교
        // 보유 크레딧 < 아이템 가격 = 오류 발생
        if(user.getCurrentCredit() < item.getPrice()) {
            throw new ItemHandler(ErrorStatus.INSUFFICIENT_CREDIT);
        }

        // 보유 크레딧 >= 아이템 가격 = 아이템 구매
        // 보유 크레딧 최신화
        user.updateCurrentCredit(user.getCurrentCredit() - item.getPrice());
        userRepository.save(user);

        // user_item에 저장
        UserItem newUserItem = ItemConverter.toUserItem();
        newUserItem.setUser(user);
        newUserItem.setItem(item);
        UserItem savedUserItem = userItemRepository.save(newUserItem);

        // converter 작업
        return ItemConverter.toPurchaseItemResponseDTO(savedUserItem);
    }
}
