package umc.GrowIT.Server.converter;

import umc.GrowIT.Server.domain.Item;
import umc.GrowIT.Server.domain.User;
import umc.GrowIT.Server.domain.UserItem;
import umc.GrowIT.Server.domain.enums.ItemStatus;

public class UserItemConverter {
    public static UserItem toUserItem(User user, Item item) {
        return UserItem.builder()
                .user(user)
                .item(item)
                .status(ItemStatus.EQUIPPED)  // 초기 생성 시 착용 상태로 설정
                .build();
    }
}
