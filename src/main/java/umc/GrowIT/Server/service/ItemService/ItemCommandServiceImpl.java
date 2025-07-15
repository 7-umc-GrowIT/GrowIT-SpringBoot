package umc.GrowIT.Server.service.ItemService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.GrowIT.Server.apiPayload.code.status.ErrorStatus;
import umc.GrowIT.Server.apiPayload.exception.ItemHandler;
import umc.GrowIT.Server.converter.ItemConverter;
import umc.GrowIT.Server.domain.Item;
import umc.GrowIT.Server.domain.User;
import umc.GrowIT.Server.domain.UserItem;
import umc.GrowIT.Server.domain.enums.ItemStatus;
import umc.GrowIT.Server.repository.ItemRepository.ItemRepository;
import umc.GrowIT.Server.repository.UserItemRepository;
import umc.GrowIT.Server.repository.UserRepository;
import umc.GrowIT.Server.repository.UserSubscriptionRepository;
import umc.GrowIT.Server.web.dto.ItemDTO.ItemResponseDTO;
import umc.GrowIT.Server.web.dto.ItemEquipDTO.ItemEquipResponseDTO;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ItemCommandServiceImpl implements ItemCommandService {

    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final UserItemRepository userItemRepository;
    private final UserSubscriptionRepository userSubscriptionRepository;

    @Override //아이템 구매 기능
    public ItemResponseDTO.PurchaseItemResponseDTO purchase(Long itemId, Long userId) {
        // 1. 사용자 조회하고 없으면 오류
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ItemHandler(ErrorStatus.USER_NOT_FOUND));

        // 2. 아이템 조회하고 없으면 오류
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemHandler(ErrorStatus.ITEM_NOT_FOUND));

        // 3. 사용자가 이미 보유 중인 아이템인지 체크
        userItemRepository.findByUserAndItem(user, item)
                .ifPresent(ownedItem -> {
                    throw new ItemHandler(ErrorStatus.ITEM_OWNED);
                });

        // 4. 구독 여부 조회
        boolean isSubscribed = userSubscriptionRepository.isUserActivelySubscribed(userId);

        // 5. 비구독자의 경우에만 크레딧 차감
        if (!isSubscribed) {
            // 사용자의 현재 보유 중인 크레딧과 아이템의 가격을 비교
            // 보유 크레딧 < 아이템 가격 = 오류 발생
            if(user.getCurrentCredit() < item.getPrice()) {
                throw new ItemHandler(ErrorStatus.INSUFFICIENT_CREDIT);
            }

            // 보유 크레딧 >= 아이템 가격 = 아이템 구매
            // 보유 크레딧 최신화
            user.updateCurrentCredit(user.getCurrentCredit() - item.getPrice());
            userRepository.save(user);
        }

        // 6. user_item에 저장 (구매/구독 타입 관리)
        UserItem newUserItem = ItemConverter.toUserItem(isSubscribed);
        newUserItem.setUser(user);
        newUserItem.setItem(item);
        UserItem savedUserItem = userItemRepository.save(newUserItem);

        // 7. converter 작업
        return ItemConverter.toPurchaseItemResponseDTO(savedUserItem);
    }


    @Override
    public ItemEquipResponseDTO updateItemStatus(Long userId, Long itemId, String requestStatus) {


        UserItem userItem = userItemRepository.findByUserIdAndItemId(userId, itemId)
                .orElseThrow(() -> new ItemHandler(ErrorStatus.ITEM_NOT_OWNED));

        // 요청된 상태와 현재 상태가 같으면 에러 발생
        if (userItem.getStatus() == ItemStatus.valueOf(requestStatus)) {
            if (requestStatus.equals("EQUIPPED")) {
                throw new ItemHandler(ErrorStatus.ITEM_ALREADY_EQUIPPED);
            } else {
                throw new ItemHandler(ErrorStatus.ITEM_NOT_EQUIPPED);
            }
        }

        // 아이템 착용 요청인 경우, 같은 카테고리의 기존 착용 아이템 해제
        if (requestStatus.equals("EQUIPPED")) {
            UserItem equippedItem = userItemRepository.findByUserIdAndItemCategoryAndStatus(
                    userId,
                    userItem.getItem().getCategory(),
                    ItemStatus.EQUIPPED
            ).orElse(null);

            if (equippedItem != null) {
                equippedItem.setStatus(ItemStatus.UNEQUIPPED);
                userItemRepository.save(equippedItem);
            }
        }

        // 요청된 상태로 직접 변경
        userItem.setStatus(ItemStatus.valueOf(requestStatus));
        userItemRepository.save(userItem);

        return ItemConverter.itemEquipDTO(userItem);
    }
}
