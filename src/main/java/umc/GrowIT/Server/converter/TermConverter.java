package umc.GrowIT.Server.converter;

import umc.GrowIT.Server.domain.Term;
import umc.GrowIT.Server.domain.User;
import umc.GrowIT.Server.domain.UserTerm;
import umc.GrowIT.Server.web.dto.TermDTO.TermResponseDTO;

public class TermConverter {

    public static TermResponseDTO.TermDTO toTermDTO(Term term){
        return TermResponseDTO.TermDTO.builder()
                .title(term.getTitle())
                .content(term.getContent())
                .type(term.getType().name())
                .build();
    }

    public static UserTerm toUserTerm(Boolean agreed, Term term, User user){
        return UserTerm.builder()
                .user(user)
                .term(term)
                .agreed(agreed)
                .build();
    }
}
