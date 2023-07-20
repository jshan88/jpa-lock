package com.jshan.jpalock.pessimistic.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class Keyword {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String keyword;

    private Long count;

    @Builder
    public Keyword(String keyword, Long count) {
        this.keyword = keyword;
        this.count = count;
    }

    public void incrementByOne() {
        this.count += 1;
    }
}
