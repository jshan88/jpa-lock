package com.jshan.jpalock.optimistic.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Version;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private int point;

    @Version
    private Integer version;

    @Builder
    public Member(String name, int point) {
        this.name = name;
        this.point = point;
    }

    public void earnPoints(int points) {
        point += points;
    }
}
