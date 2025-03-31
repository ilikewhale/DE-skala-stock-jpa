package com.sk.skala.myapp.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;

/**
 * 주식 정보를 저장하는 엔티티 클래스
 * JPA를 사용하여 데이터베이스와 매핑됨
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "stocks") // 테이블 이름 명시적 지정
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // JPA 엔티티의 기본 키

    @Column(nullable = false)
    private String stockName; // 주식 이름

    @Column(nullable = false)
    private int stockPrice; // 주식 가격

    /**
     * 주식 이름과 가격으로 주식 객체를 생성하는 생성자
     * @param stockName 주식 이름
     * @param stockPrice 주식 가격
     */
    public Stock(String stockName, int stockPrice) {
        this.stockName = stockName;
        this.stockPrice = stockPrice;
    }

    @Override
    public String toString() {
        return stockName + ":" + stockPrice;
    }
}