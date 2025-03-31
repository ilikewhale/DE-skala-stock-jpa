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
    private final PlayerStockFormatService formatService;

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
            addStock(player, new PlayerStock(selectedStock, quantity));
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

        PlayerStock playerStock = findStock(player, stockIndex);
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
        updatePlayerStock(player, playerStock);
        playerRepository.savePlayerList();
        
        return true;
    }

    public String getPlayerStockDisplay(String playerId) {
        Player player = findPlayer(playerId);
        if (player == null) {
            return "플레이어를 찾을 수 없습니다.";
        }
        return formatService.getPlayerStocksForMenu(player);
    }
    
    // Player 클래스에서 이동된 메서드들 (포맷팅 관련 메서드 제거)
    
    public void addStock(Player player, PlayerStock stock) {
        boolean stockExists = false;

        for (PlayerStock existingStock : player.getPlayerStocks()) {
            if (existingStock.getStockName().equals(stock.getStockName())) {
                existingStock.setStockPrice(stock.getStockPrice());
                existingStock.setStockQuantity(existingStock.getStockQuantity() + stock.getStockQuantity());
                stockExists = true;
                break;
            }
        }

        if (!stockExists) {
            player.getPlayerStocks().add(stock);
        }
    }

    public void updatePlayerStock(Player player, PlayerStock stock) {
        List<PlayerStock> playerStocks = player.getPlayerStocks();
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

    public PlayerStock findStock(Player player, int index) {
        List<PlayerStock> playerStocks = player.getPlayerStocks();
        if (index >= 0 && index < playerStocks.size()) {
            return playerStocks.get(index);
        }
        return null;
    }
}