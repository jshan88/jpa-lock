package com.jshan.jpalock.optimistic.repository;

import com.jshan.jpalock.optimistic.domain.Member;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    @Lock(LockModeType.OPTIMISTIC)
    @Query(value = "select m from Member m where m.id = ?1")
    Optional<Member> findByIdWithLock(Long id);
}
