package com.jshan.jpalock.service;

import static org.junit.jupiter.api.Assertions.*;

import com.jshan.jpalock.pessimistic.domain.Keyword;
import com.jshan.jpalock.pessimistic.repository.KeywordRepository;
import com.jshan.jpalock.pessimistic.service.KeywordService;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

//@DataJpaTest
@SpringBootTest
class KeywordRepositoryTest {

    @Autowired
    KeywordRepository keywordRepository;

    @Autowired
    KeywordService keywordService;

    Long keywordId;

    @BeforeEach
    void init() {
        Keyword keyword = Keyword.builder().keyword("TEST1").count(0L).build();
        keywordRepository.save(keyword);
        keywordId = keyword.getId();
    }

    @AfterEach
    void clean() {
        keywordRepository.deleteAllInBatch(); //(keywordId);
    }

    @Test
    void without_lock_test() throws InterruptedException {

        int threadNumber = 20;

        ExecutorService service = Executors.newFixedThreadPool(threadNumber);
        CountDownLatch latch = new CountDownLatch(threadNumber);

        for(int i = 0; i < threadNumber; i++) {
            service.execute(() -> {
                keywordService.incrementWithoutLock(keywordId);
                latch.countDown();
            });
        }

        latch.await();

        //then
        assertEquals(threadNumber, keywordService.getNumber(keywordId));
    }

    @Test
    void with_lock_test() throws InterruptedException {

        int threadNumber = 3;

        ExecutorService service = Executors.newFixedThreadPool(threadNumber);
        CountDownLatch latch = new CountDownLatch(threadNumber);

        for(int i = 0; i < threadNumber; i++) {
            service.execute(() -> {
                keywordService.incrementWithPessimisticWrite(keywordId);
                latch.countDown();
            });
        }

        latch.await();

        //then
        assertEquals(threadNumber, keywordService.getNumber(keywordId));
    }
}
