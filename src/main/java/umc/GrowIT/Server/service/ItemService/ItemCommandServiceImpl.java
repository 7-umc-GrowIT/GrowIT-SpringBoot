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
import umc.GrowIT.Server.domain.enums.ItemStatus;
import umc.GrowIT.Server.repository.ItemRepository.ItemRepository;
import umc.GrowIT.Server.repository.UserItemRepository;
import umc.GrowIT.Server.repository.UserRepository;
import umc.GrowIT.Server.web.dto.ItemDTO.ItemResponseDTO;
import umc.GrowIT.Server.web.dto.ItemEquipDTO.ItemEquipResponseDTO;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemCommandServiceImpl implements ItemCommandService {

    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final UserItemRepository userItemRepository;

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

        // 4. 사용자의 현재 보유 중인 크레딧과 아이템의 가격을 비교
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

    @Override
    public ItemEquipResponseDTO updateItemStatus(Long userId, Long itemId, String status) {

        UserItem userItem = userItemRepository.findByUserIdAndItemId(userId, itemId)
                .orElseThrow(() -> new ItemHandler(ErrorStatus.ITEM_NOT_OWNED));

        // 현재 상태와 요청된 상태가 같을 때 각각 다른 에러 발생
        if (userItem.getStatus() != ItemStatus.valueOf(status)) {
            if (ItemStatus.valueOf(status) == ItemStatus.UNEQUIPPED && userItem.getStatus() != ItemStatus.UNEQUIPPED) { //현재 해제상태라고 요청이 왔는데(착용 요청) 착용상태인 경우
                throw new ItemHandler(ErrorStatus.ITEM_ALREADY_EQUIPPED);
            }

            if(ItemStatus.valueOf(status) == ItemStatus.EQUIPPED && userItem.getStatus() != ItemStatus.EQUIPPED) { //착상태라고 요청이 왔는데(해제 요청) 착용중이지 않은 경우
                throw new ItemHandler(ErrorStatus.ITEM_NOT_EQUIPPED);
            }
        }

        userItem.toggleStatus();

        return ItemConverter.itemEquipDTO(userItem);
    }
}
