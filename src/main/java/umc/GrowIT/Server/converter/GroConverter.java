package umc.GrowIT.Server.converter;

import umc.GrowIT.Server.domain.Gro;
import umc.GrowIT.Server.domain.User;
import umc.GrowIT.Server.web.dto.GroDTO.GroResponseDTO;

public class GroConverter {


    public static GroResponseDTO toGroResponseDTO(Gro gro) {
        return GroResponseDTO.builder()
                .id(gro.getId())
                .user_id(gro.getUser().getId())
                .name(gro.getName())
                .level(gro.getLevel())
                .build();
    }


    public static Gro toGro(User user, String name) {
        return Gro.builder()
                .user(user)
                .name(name)
                .level(1)
                .build();
    }
}
