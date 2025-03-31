package com.sk.skala.myapp.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Stock {
    private String stockName;
    private int stockPrice;

    @Override
    public String toString() {
        return stockName + ":" + stockPrice;
    }
}
