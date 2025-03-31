package com.sk.skala.myapp.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Random;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Stock {
    private String stockName;
    private int stockPrice;

    public void changeStockPrice() {
        Random random = new Random();
        int fluctuation = random.nextInt(21) - 10; // -10 ~ +10 사이의 랜덤한 값 생성
        this.stockPrice += fluctuation;

        // 가격이 0 이하로 내려가지 않도록 보정
        if (this.stockPrice < 0) {
            this.stockPrice = 0;
        }
    }

    @Override
    public String toString() {
        return stockName + ":" + stockPrice;
    }
}