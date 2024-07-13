//package shop.kkeujeok.kkeujeokbackend.member.domain.repository;
//
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.transaction.annotation.Transactional;
//import shop.kkeujeok.kkeujeokbackend.member.domain.Member;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@DataJpaTest
//public class MemberRepositoryTest {
//
//    @Autowired
//    private MemberRepository memberRepository;
//
//    @DisplayName("이메일을 통해 회원을 찾는다.")
//    @Test
//    @Transactional
//    void 이메일을_통해_회원을_찾는다() {
//        Member 끄적 = Member.builder()
//                .email("끄적_이메일")
//                .build();
//
//        memberRepository.save(끄적);
//
//        assertThat(memberRepository.findByEmail("끄적_이메일").get().getId()).isEqualTo(끄적.getId());
//    }
//}
