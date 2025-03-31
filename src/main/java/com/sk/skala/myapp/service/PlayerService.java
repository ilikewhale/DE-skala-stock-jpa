package com.sk.skala.myapp.service;

import com.sk.skala.myapp.model.Player;
import com.sk.skala.myapp.model.PlayerStock;
import com.sk.skala.myapp.model.Stock;
import com.sk.skala.myapp.repository.PlayerRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlayerService {
    private final PlayerRepository playerRepository;
    private final StockService stockService;

    @PostConstruct
    public void initialize() {
        playerRepository.loadPlayerList();
    }

    public List<Player> getAllPlayers() {
        return playerRepository.getAllPlayers();
    }

    public Player findPlayer(String id) {
        return playerRepository.findPlayer(id);
    }

    public Player createNewPlayer(String playerId, int initialMoney) {
        Player player = new Player(playerId);
        player.setPlayerMoney(initialMoney);
        playerRepository.addPlayer(player);
        return player;
    }

    public boolean buyStock(String playerId, int stockIndex, int quantity) {
        Player player = findPlayer(playerId);
        if (player == null) {
            return false;
        }

        Stock selectedStock = stockService.findStockByIndex(stockIndex);
        if (selectedStock == null) {
            return false;
        }

        int totalCost = selectedStock.getStockPrice() * quantity;
        int playerMoney = player.getPlayerMoney();
        
        if (totalCost <= playerMoney) {
            player.setPlayerMoney(playerMoney - totalCost);
            player.addStock(new PlayerStock(selectedStock, quantity));
            playerRepository.savePlayerList();
            return true;
        }
        
        return false;
    }

    public boolean sellStock(String playerId, int stockIndex, int quantity) {
        Player player = findPlayer(playerId);
        if (player == null) {
            return false;
        }

        PlayerStock playerStock = player.findStock(stockIndex);
        if (playerStock == null || playerStock.getStockQuantity() < quantity) {
            return false;
        }

        Stock baseStock = stockService.findStockByName(playerStock.getStockName());
        if (baseStock == null) {
            return false;
        }

        int playerMoney = player.getPlayerMoney() + baseStock.getStockPrice() * quantity;
        player.setPlayerMoney(playerMoney);

        playerStock.setStockQuantity(playerStock.getStockQuantity() - quantity);
        player.updatePlayerStock(playerStock);
        playerRepository.savePlayerList();
        
        return true;
    }

    public String getPlayerStockDisplay(String playerId) {
        Player player = findPlayer(playerId);
        if (player == null) {
            return "플레이어를 찾을 수 없습니다.";
        }
        return player.getPlayerStocksForMenu();
    }
}