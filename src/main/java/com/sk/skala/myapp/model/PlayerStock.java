package com.sk.skala.myapp.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PlayerStock extends Stock {
    private int stockQuantity;

    public PlayerStock(Stock stock, int quantity) {
        this.setStockName(stock.getStockName());
        this.setStockPrice(stock.getStockPrice());
        this.stockQuantity = quantity;
    }

    public PlayerStock(String name, String price, String quantity) {
        this.setStockName(name);
        this.setStockPrice(Integer.parseInt(price));
        this.stockQuantity = Integer.parseInt(quantity);
    }

    @Override
    public String toString() {
        return super.toString() + ":" + this.stockQuantity;
    }
}