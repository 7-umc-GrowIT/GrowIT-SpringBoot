package umc.GrowIT.Server.util.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreditGrantResult {
    private final boolean granted;   // 크레딧 지급 여부
    private final int amount;        // 실제 지급된 크레딧 수
}
