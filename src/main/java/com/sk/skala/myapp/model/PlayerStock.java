package com.sk.skala.myapp.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

/**
 * 플레이어가 보유한 주식 정보를 저장하는 엔티티 클래스
 * JPA를 사용하여 데이터베이스와 매핑됨
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "player_stocks") // 테이블 이름 명시적 지정
public class PlayerStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // JPA 엔티티의 기본 키

    @Column(nullable = false)
    private String stockName; // 주식 이름
    
    @Column(nullable = false)
    private int stockPrice; // 주식 가격
    
    @Column(nullable = false)
    private int stockQuantity; // 보유 수량

    // 다대일 관계 설정 - 여러 PlayerStock은 하나의 Player에 속함
    @ManyToOne
    @JoinColumn(name = "player_id")
    private Player player;

    /**
     * 기존 Stock 객체와 수량을 사용해 PlayerStock 객체 초기화
     * @param stock 주식 정보
     * @param quantity 수량
     */
    public PlayerStock(Stock stock, int quantity) {
        this.stockName = stock.getStockName();
        this.stockPrice = stock.getStockPrice();
        this.stockQuantity = quantity;
    }

    /**
     * 주식 이름, 가격, 수량을 문자열로 받아 PlayerStock 객체 초기화
     * @param name 주식 이름
     * @param price 주식 가격 (문자열)
     * @param quantity 수량 (문자열)
     */
    public PlayerStock(String name, String price, String quantity) {
        this.stockName = name;
        this.stockPrice = Integer.parseInt(price);
        this.stockQuantity = Integer.parseInt(quantity);
    }

    @Override
    public String toString() {
        return stockName + ":" + stockPrice + ":" + stockQuantity;
    }
}