package com.sk.skala.myapp.service;

import com.sk.skala.myapp.model.Player;
import com.sk.skala.myapp.model.PlayerStock;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayerStockFormatService {
    
    public String getPlayerStocksForFile(Player player) {
        StringBuilder sb = new StringBuilder();
        List<PlayerStock> playerStocks = player.getPlayerStocks();
        for (int i = 0; i < playerStocks.size(); i++) {
            if (i > 0) {
                sb.append("|");
            }
            sb.append(playerStocks.get(i));
        }
        return sb.toString();
    }
    
    public String getPlayerStocksForMenu(Player player) {
        StringBuilder sb = new StringBuilder();
        List<PlayerStock> playerStocks = player.getPlayerStocks();
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