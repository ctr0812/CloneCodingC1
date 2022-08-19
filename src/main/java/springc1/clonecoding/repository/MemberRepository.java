package springc1.clonecoding.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import springc1.clonecoding.domain.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUsername(String username);
    Optional<Member> findByNickname(String nickname);
}
