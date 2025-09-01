package umc.GrowIT.Server.converter;

import umc.GrowIT.Server.domain.CreditHistory;
import umc.GrowIT.Server.domain.Gro;
import umc.GrowIT.Server.domain.User;
import umc.GrowIT.Server.domain.enums.LoginMethod;
import umc.GrowIT.Server.web.dto.AuthDTO.AuthResponseDTO;
import umc.GrowIT.Server.web.dto.OAuthDTO.OAuthApiResponseDTO;
import umc.GrowIT.Server.web.dto.TokenDTO.TokenResponseDTO;
import umc.GrowIT.Server.web.dto.UserDTO.UserRequestDTO;
import umc.GrowIT.Server.web.dto.UserDTO.UserResponseDTO;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static umc.GrowIT.Server.domain.enums.Role.USER;
import static umc.GrowIT.Server.domain.enums.UserStatus.ACTIVE;

public class UserConverter {

    public static User toUser(UserRequestDTO.UserInfoDTO userInfoDTO) {
        return User.builder()
                .name(userInfoDTO.getName())
                .primaryEmail(userInfoDTO.getEmail())
                .password(userInfoDTO.getPassword())
                .currentCredit(0)
                .totalCredit(0)
                .role(USER)
                .status(ACTIVE)
                .build();
    }

    public static User toUser(OAuthApiResponseDTO.OAuthUserInfoDTO oAuthUserInfoDTO) {
        return User.builder()
                .name(oAuthUserInfoDTO.getName())
                .primaryEmail(oAuthUserInfoDTO.getEmail())
                .password(null)
                .currentCredit(0)
                .totalCredit(0)
                .role(USER)
                .status(ACTIVE)
                .build();
    }

    public static AuthResponseDTO.LogoutResponseDTO toLogoutDTO(){
        return AuthResponseDTO.LogoutResponseDTO.builder()
                .message("로그아웃이 완료되었습니다.")
                .build();
    }

    public static UserResponseDTO.MyPageDTO toMyPageDTO(User user, Gro gro){
        return UserResponseDTO.MyPageDTO.builder()
                .userId(user.getId())
                .name(gro.getName())
                .build();
    }

    public static AuthResponseDTO.LoginResponseDTO toLoginResponseDTO(TokenResponseDTO.TokenDTO tokenDTO, LoginMethod loginMethod) {
        return AuthResponseDTO.LoginResponseDTO.builder()
                .tokens(tokenDTO)
                .loginMethod(loginMethod)
                .build();
    }

    public static UserResponseDTO.CreditHistoryResponseDTO toCreditHistoryResponseDTO(
            List<CreditHistory> histories, YearMonth yearMonth, boolean hasNext) {

        Map<String, List<UserResponseDTO.CreditRecordDTO>> dailyHistories = histories.stream()
                .collect(Collectors.groupingBy(
                        history -> history.getCreatedAt()
                                .toLocalDate()
                                .format(DateTimeFormatter.ofPattern("yyyy.MM.dd")),
                        LinkedHashMap::new,
                        Collectors.mapping(
                                UserConverter::toCreditRecordDTO,
                                Collectors.toList()
                        )
                ));

        return UserResponseDTO.CreditHistoryResponseDTO.builder()
                .yearMonth(yearMonth.getYear() + "년 " + String.format("%02d", yearMonth.getMonthValue()) + "월")
                .dailyHistories(dailyHistories)
                .hasNext(hasNext)
                .build();
    }

    private static UserResponseDTO.CreditRecordDTO toCreditRecordDTO(CreditHistory history) {
        return UserResponseDTO.CreditRecordDTO.builder()
                .time(history.getCreatedAt().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm:ss")))
                .description(history.getDescription())
                .amount(history.getAmount())
                .type(history.getAmount() > 0 ? "적립" : "사용")
                .build();
    }
}
