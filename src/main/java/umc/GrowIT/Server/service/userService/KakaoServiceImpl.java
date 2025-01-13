package umc.GrowIT.Server.service.userService;

import org.springframework.stereotype.Service;
import umc.GrowIT.Server.web.dto.UserDTO.KakaoResponseDTO;

@Service
public class KakaoServiceImpl implements KakaoService {

    @Override
    public KakaoResponseDTO.KakaoTokenDTO getToken(String code){
        return null;
    }

}
