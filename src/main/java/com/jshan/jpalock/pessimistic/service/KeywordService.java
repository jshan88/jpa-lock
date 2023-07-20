package com.jshan.jpalock.pessimistic.service;

import com.jshan.jpalock.pessimistic.domain.Keyword;
import com.jshan.jpalock.pessimistic.repository.KeywordRepository;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class KeywordService {

    private final KeywordRepository keywordRepository;

    @Transactional
    public Long getNumber(Long id) {
        return keywordRepository.findById(id).orElseThrow(NoSuchElementException::new).getCount();
    }

    @Transactional
    public void incrementWithoutLock(Long id) {
        Keyword keyword = keywordRepository.findById(id).orElseThrow(NoSuchElementException::new);
        keyword.incrementByOne();
        log.info("#### increment by one from {}", keyword.getCount());
    }

    @Transactional
    public void incrementWithPessimisticWrite(Long id) {
        Keyword keyword = keywordRepository.findByIdWithPessimisticWrite(id).orElseThrow(NoSuchElementException::new);
        keyword.incrementByOne();
        log.info("#### increment by one from {}", keyword.getCount());
    }
}
