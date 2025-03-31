package com.sk.skala.myapp.service;

import com.sk.skala.myapp.model.Player;
import com.sk.skala.myapp.model.PlayerStock;
import com.sk.skala.myapp.model.Stock;
import com.sk.skala.myapp.repository.PlayerJpaRepository;
import com.sk.skala.myapp.repository.PlayerStockJpaRepository;
import com.sk.skala.myapp.repository.StockJpaRepository;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * 플레이어 관련 비즈니스 로직을 처리하는 서비스 클래스
 */
@Service
@RequiredArgsConstructor
public class PlayerService {
    private final PlayerJpaRepository playerJpaRepository;
    private final StockJpaRepository stockJpaRepository;
    private final PlayerStockJpaRepository playerStockJpaRepository;

    /**
     * 서비스 초기화 메서드
     */
    @PostConstruct
    public void initialize() {
        // 초기화 작업이 필요한 경우 이곳에 작성
    }

    /**
     * 모든 플레이어 정보를 조회
     * @return List<Player> 플레이어 목록
     */
    public List<Player> getAllPlayers() {
        return playerJpaRepository.findAll();
    }

    /**
     * 플레이어 ID로 플레이어 정보를 조회
     * @param id 플레이어 ID
     * @return Player 플레이어 정보 (없을 경우 null)
     */
    public Player findPlayer(String id) {
        return playerJpaRepository.findByPlayerId(id).orElse(null);
    }

    /**
     * 새로운 플레이어를 생성
     * @param playerId 플레이어 ID
     * @param initialMoney 초기 자금
     * @return Player 생성된 플레이어 정보
     */
    @Transactional
    public Player createNewPlayer(String playerId, int initialMoney) {
        // 이미 존재하는 플레이어 ID인지 확인
        if (playerJpaRepository.findByPlayerId(playerId).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 플레이어 ID입니다: " + playerId);
        }
        
        Player player = new Player(playerId);
        player.setPlayerMoney(initialMoney);
        return playerJpaRepository.save(player);
    }

    /**
     * 플레이어가 주식을 구매
     * @param playerId 플레이어 ID
     * @param stockIndex 주식 인덱스 (ID)
     * @param quantity 구매 수량
     * @return boolean 구매 성공 여부
     */
    @Transactional
    public boolean buyStock(String playerId, int stockIndex, int quantity) {
        Player player = findPlayer(playerId);
        if (player == null) {
            return false;
        }

        Stock selectedStock = stockJpaRepository.findById((long) stockIndex).orElse(null);
        if (selectedStock == null) {
            return false;
        }

        int totalCost = selectedStock.getStockPrice() * quantity;
        int playerMoney = player.getPlayerMoney();
        
        if (totalCost <= playerMoney) {
            player.setPlayerMoney(playerMoney - totalCost);
            
            // 이미 해당 주식을 가지고 있는지 확인
            Optional<PlayerStock> existingStockOpt = playerStockJpaRepository.findByPlayerAndStockName(
                player, selectedStock.getStockName());
            
            if (existingStockOpt.isPresent()) {
                // 이미 있는 주식이면 수량 증가
                PlayerStock existingStock = existingStockOpt.get();
                existingStock.setStockPrice(selectedStock.getStockPrice());
                existingStock.setStockQuantity(existingStock.getStockQuantity() + quantity);
                playerStockJpaRepository.save(existingStock);
            } else {
                // 새로운 주식이면 생성
                PlayerStock newPlayerStock = new PlayerStock(selectedStock, quantity);
                newPlayerStock.setPlayer(player);
                playerStockJpaRepository.save(newPlayerStock);
            }
            
            playerJpaRepository.save(player);
            return true;
        }
        
        return false;
    }

    /**
     * 플레이어가 주식을 판매
     * @param playerId 플레이어 ID
     * @param stockIndex 주식 인덱스 (목록 내 위치)
     * @param quantity 판매 수량
     * @return boolean 판매 성공 여부
     */
    @Transactional
    public boolean sellStock(String playerId, int stockIndex, int quantity) {
        Player player = findPlayer(playerId);
        if (player == null) {
            return false;
        }

        List<PlayerStock> playerStocks = playerStockJpaRepository.findByPlayer(player);
        if (stockIndex < 0 || stockIndex >= playerStocks.size()) {
            return false;
        }
        
        PlayerStock playerStock = playerStocks.get(stockIndex);
        if (playerStock.getStockQuantity() < quantity) {
            return false;
        }

        // 실제 주식 정보에서 최신 가격을 가져옴
        Stock baseStock = stockJpaRepository.findByStockName(playerStock.getStockName()).orElse(null);
        if (baseStock == null) {
            return false;
        }

        // 판매 금액을 플레이어 돈에 추가
        int sellAmount = baseStock.getStockPrice() * quantity;
        player.setPlayerMoney(player.getPlayerMoney() + sellAmount);

        // 주식 수량 업데이트
        playerStock.setStockQuantity(playerStock.getStockQuantity() - quantity);
        
        // 수량이 0이 되면 주식 삭제
        if (playerStock.getStockQuantity() <= 0) {
            playerStockJpaRepository.delete(playerStock);
        } else {
            playerStockJpaRepository.save(playerStock);
        }
        
        playerJpaRepository.save(player);
        return true;
    }

    /**
     * 플레이어 보유 주식 목록을 문자열로 반환
     * @param playerId 플레이어 ID
     * @return String 주식 목록 문자열
     */
    public String getPlayerStockDisplay(String playerId) {
        Player player = findPlayer(playerId);
        if (player == null) {
            return "플레이어를 찾을 수 없습니다.";
        }
        
        List<PlayerStock> playerStocks = playerStockJpaRepository.findByPlayer(player);
        return formatPlayerStocksForMenu(playerStocks);
    }

    /**
     * 주식 목록을 파일 저장용 문자열로 변환
     * @param player 플레이어 정보
     * @return String 파일 저장용 문자열
     */
    public String getPlayerStocksForFile(Player player) {
        StringBuilder sb = new StringBuilder();
        List<PlayerStock> playerStocks = playerStockJpaRepository.findByPlayer(player);
        
        for (int i = 0; i < playerStocks.size(); i++) {
            if (i > 0) {
                sb.append("|");
            }
            sb.append(playerStocks.get(i));
        }
        return sb.toString();
    }

    /**
     * 주식 목록을 메뉴 형식 문자열로 변환
     * @param playerStocks 플레이어 주식 목록
     * @return String 메뉴 형식 문자열
     */
    private String formatPlayerStocksForMenu(List<PlayerStock> playerStocks) {
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