package com.jshan.jpalock.pessimistic.repository;

import com.jshan.jpalock.pessimistic.domain.Keyword;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface KeywordRepository extends JpaRepository<Keyword, Long> {

    public Optional<Keyword> findById(Long id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query(value = "select k from Keyword k where k.id = ?1")
//    @QueryHints({@QueryHint(name = "javax.persistence.lock.timeout", value ="3000")}) // lock timeout for 3 seconds
    public Optional<Keyword> findByIdWithPessimisticWrite(Long id);

}
