package umc.GrowIT.Server.service;

import org.springframework.stereotype.Service;
import umc.GrowIT.Server.dto.KakaoResponseDTO;

@Service
public class KakaoServiceImpl implements KakaoService {

    @Override
    public KakaoResponseDTO.KakaoTokenDTO getToken(String code){
        return null;
    }

}
