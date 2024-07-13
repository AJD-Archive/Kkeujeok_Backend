package shop.kkeujeok.kkeujeokbackend.member.domain.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import shop.kkeujeok.kkeujeokbackend.member.domain.Member;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MemberRepositoryTest {

    @Mock
    private MemberRepository memberRepository;

    @Test
    void testFindByEmail() {
        // given
        String email = "test@example.com";
        Member member = Member.builder()
                .email(email)
                .build();

        when(memberRepository.findByEmail(anyString())).thenReturn(Optional.of(member));

        // when
        Optional<Member> foundMember = memberRepository.findByEmail(email);

        // then
        assertThat(foundMember).isPresent();
        assertThat(foundMember.get().getEmail()).isEqualTo(email);
    }
}
