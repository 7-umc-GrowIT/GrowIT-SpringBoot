package umc.GrowIT.Server.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import umc.GrowIT.Server.converter.TermConverter;
import umc.GrowIT.Server.converter.UserConverter;
import umc.GrowIT.Server.domain.Term;
import umc.GrowIT.Server.domain.User;
import umc.GrowIT.Server.domain.UserTerm;
import umc.GrowIT.Server.dto.UserRequestDTO;
import umc.GrowIT.Server.dto.UserResponseDTO;
import umc.GrowIT.Server.repository.TermRepository;
import umc.GrowIT.Server.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class UserCommandServiceImpl implements UserCommandService {

    private final UserRepository userRepository;
    private final TermRepository termRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponseDTO.TokenDTO createUser(UserRequestDTO.UserInfoDTO userInfoDTO) {
        String email = userInfoDTO.getEmail();
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            return null;
        } else {
            //TODO: 비밀번호 인코딩, 토큰 생성
            User newUser = UserConverter.toUser(userInfoDTO);

            newUser.encodePassword(passwordEncoder.encode(newUser.getPassword()));

            //UserInfoDTO의 UserTermDTO(term_id, agreed) -> UserTerm
            List<UserTerm> userTerms = userInfoDTO.getUserTerm().stream()
                    .map(tempUserTerm -> {
                        Term term = termRepository.findById(tempUserTerm.getTermId()).orElseThrow();
                        return TermConverter.toUserTerm(tempUserTerm.getAgreed(), term, newUser);
                    })
                    .collect(Collectors.toList());

            newUser.setUserTerms(userTerms);

            userRepository.save(newUser);

            UserResponseDTO.TokenDTO tokenDTO = new UserResponseDTO.TokenDTO("token");
            return tokenDTO;
        }
    }

    @Override
    public UserResponseDTO.TokenDTO emailLogin(UserRequestDTO.EmailLoginDTO emailLoginDTO){
        return null;
    }

    @Override
    public void updatePassword(UserRequestDTO.PasswordDTO passwordDTO) {

    }
}
