package umc.GrowIT.Server.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import umc.GrowIT.Server.web.dto.TermDTO.TermRequestDTO;

import java.util.List;


@Service
@RequiredArgsConstructor
public class TermCommandServiceImpl implements TermCommandService {

    @Override
    public void createUserTerms(List<TermRequestDTO.UserTermDTO> userTermDTO) {

    }
}
