package com.jshan.jpalock.optimistic.service;

import static org.junit.jupiter.api.Assertions.*;

import com.jshan.jpalock.optimistic.domain.Member;
import com.jshan.jpalock.optimistic.repository.MemberRepository;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.OptimisticLockingFailureException;

@SpringBootTest
class MemberServiceTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    MemberService memberService;

    Member member;

    @BeforeEach
    void init() {
        member = Member.builder().name("HAN,JINSEOK").point(1).build();
        memberRepository.save(member);
    }

    @AfterEach
    void clean() {
        memberRepository.deleteAllInBatch();
    }

    @Test
    void with_lock_test() throws InterruptedException {

        int threadNumber = 5;
        int initialPoint = member.getPoint();
        int incrementPoint = 1;

        ExecutorService executorService = Executors.newFixedThreadPool(threadNumber);
        CountDownLatch countDownLatch = new CountDownLatch(threadNumber);

        for(int i = 0; i < threadNumber; i++) {
            executorService.execute(() -> {
                try {
                    memberService.earnPointsWithOptimisticLock(member.getId(), incrementPoint);
                } catch (OptimisticLockingFailureException ex) {
                    System.out.println("##### FAILED");
                }
                countDownLatch.countDown();
            });
        }

        countDownLatch.await();

        assertEquals(initialPoint + incrementPoint*threadNumber, memberService.getPoints(member.getId()));
    }
}