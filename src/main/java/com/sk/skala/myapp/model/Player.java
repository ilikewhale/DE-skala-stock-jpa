package com.sk.skala.myapp.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class Player {
    private String playerId;
    private int playerMoney;
    private List<PlayerStock> playerStocks = new ArrayList<>();

    public Player(String id) {
        this.playerId = id;
        this.playerMoney = 10000;
    }
    
    public void addStock(PlayerStock stock) {
        boolean stockExists = false;

        for (PlayerStock existingStock : playerStocks) {
            if (existingStock.getStockName().equals(stock.getStockName())) {
                existingStock.setStockPrice(stock.getStockPrice());
                existingStock.setStockQuantity(existingStock.getStockQuantity() + stock.getStockQuantity());
                stockExists = true;
                break;
            }
        }

        if (!stockExists) {
            playerStocks.add(stock);
        }
    }

    public void updatePlayerStock(PlayerStock stock) {
        for (int i = 0; i < playerStocks.size(); i++) {
            PlayerStock existingStock = playerStocks.get(i);
            if (existingStock.getStockName().equals(stock.getStockName())) {
                existingStock.setStockPrice(stock.getStockPrice());
                existingStock.setStockQuantity(stock.getStockQuantity());
                if (existingStock.getStockQuantity() == 0) {
                    playerStocks.remove(i);
                }
                break;
            }
        }
    }

    public PlayerStock findStock(int index) {
        if (index >= 0 && index < playerStocks.size()) {
            return playerStocks.get(index);
        }
        return null;
    }

    public String getPlayerStocksForFile() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < playerStocks.size(); i++) {
            if (i > 0) {
                sb.append("|");
            }
            sb.append(playerStocks.get(i));
        }
        return sb.toString();
    }

    public String getPlayerStocksForMenu() {
        StringBuilder sb = new StringBuilder();
        if (playerStocks.isEmpty()) {
            sb.append("보유한 주식 목록이 없습니다.");
        } else {
            for (int i = 0; i < playerStocks.size(); i++) {
                sb.append(i + 1);
                sb.append(". ");
                sb.append(playerStocks.get(i).toString());
                sb.append(System.lineSeparator());
            }
        }
        return sb.toString();
    }
}