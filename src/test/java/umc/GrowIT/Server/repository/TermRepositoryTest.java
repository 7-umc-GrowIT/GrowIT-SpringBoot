package umc.GrowIT.Server.repository;

import umc.GrowIT.Server.domain.Term;
import umc.GrowIT.Server.repository.TermRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static umc.GrowIT.Server.domain.enums.TermType.MANDATORY;
import static umc.GrowIT.Server.domain.enums.TermType.OPTIONAL;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class TermRepositoryTest {
    @Autowired
    private TermRepository termRepository;

    @Test
    public void createTermTest(){
        Term term1 = Term.builder()
                .title("이용약관 (1)")
                .content("이용약관 (1) 내용")
                .type(MANDATORY)
                .build();
        termRepository.save(term1);

        Term term2 = Term.builder()
                .title("이용약관 (2)")
                .content("이용약관 (2) 내용")
                .type(MANDATORY)
                .build();
        termRepository.save(term2);

        Term term3 = Term.builder()
                .title("이용약관 (3)")
                .content("이용약관 (3) 내용")
                .type(MANDATORY)
                .build();
        termRepository.save(term3);

        Term term4 = Term.builder()
                .title("이용약관 (4)")
                .content("이용약관 (4) 내용")
                .type(MANDATORY)
                .build();
        termRepository.save(term4);

        Term term5 = Term.builder()
                .title("개인정보 수집 이용 동의")
                .content("개인정보 수집 이용 동의 내용")
                .type(OPTIONAL)
                .build();
        termRepository.save(term5);

        Term term6 = Term.builder()
                .title("개인정보 제3자 제공 동의")
                .content("개인정보 제3자 제공 동의 내용")
                .type(OPTIONAL)
                .build();
        termRepository.save(term6);

        assertThat(termRepository.findAll().size()).isEqualTo(6);
    }

    @Test
    public void selectTermTest(){

        assertThat(termRepository.findById(5L).get().getTitle()).isEqualTo("개인정보 수집 이용 동의");

    }

    @Test
    public void deleteTermTest(){
        for (int i=7; i<=12; i++)
            termRepository.deleteById((long)i);

        assertThat(termRepository.findAll().size()).isEqualTo(6);
    }
}