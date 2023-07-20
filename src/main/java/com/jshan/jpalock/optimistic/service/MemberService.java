package com.jshan.jpalock.optimistic.service;

import com.jshan.jpalock.optimistic.domain.Member;
import com.jshan.jpalock.optimistic.repository.MemberRepository;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public int getPoints(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(NoSuchElementException::new);
        return member.getPoint();
    }

    //TODO: implement the client service to invoke this method,
    //      and make the handling process there for the OptimisticExceptions.
    //      for example, have a retry mechanism, with looping process.
    @Transactional
    public void earnPointsWithOptimisticLock(Long id, int points) {
        Member member = memberRepository.findByIdWithLock(id).orElseThrow(NoSuchElementException::new);
        member.earnPoints(points);
    }
}
