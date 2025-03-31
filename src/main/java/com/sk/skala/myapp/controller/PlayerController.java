package com.sk.skala.myapp.controller;

import com.sk.skala.myapp.model.Player;
import com.sk.skala.myapp.service.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 플레이어 관련 API 엔드포인트를 제공하는 REST 컨트롤러
 */
@RestController
@RequestMapping("/api/players")
@RequiredArgsConstructor
public class PlayerController {
    private final PlayerService playerService;

    /**
     * 모든 플레이어 정보를 조회
     * @return 플레이어 목록
     */
    @GetMapping
    public ResponseEntity<List<Player>> getAllPlayers() {
        return ResponseEntity.ok(playerService.getAllPlayers());
    }

    /**
     * 특정 플레이어 정보를 ID로 조회
     * @param id 플레이어 ID
     * @return 플레이어 정보
     */
    @GetMapping("/{id}")
    public ResponseEntity<Player> getPlayerById(@PathVariable String id) {
        Player player = playerService.findPlayer(id);
        if (player == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(player);
    }

    /**
     * 새로운 플레이어 생성
     * @param id 플레이어 ID
     * @param money 초기 자금
     * @return 생성된 플레이어 정보
     */
    @PostMapping
    public ResponseEntity<?> createPlayer(@RequestParam String id, @RequestParam int money) {
        try {
            Player player = playerService.createNewPlayer(id, money);
            return ResponseEntity.ok(player);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * 플레이어 주식 구매
     * @param id 플레이어 ID
     * @param stockIndex 주식 인덱스 (ID)
     * @param quantity 구매 수량
     * @return 구매 결과 메시지
     */
    @PostMapping("/{id}/buy")
    public ResponseEntity<String> buyStock(
            @PathVariable String id,
            @RequestParam int stockIndex,
            @RequestParam int quantity) {
        
        boolean success = playerService.buyStock(id, stockIndex, quantity);
        if (success) {
            return ResponseEntity.ok("주식 구매 성공");
        } else {
            return ResponseEntity.badRequest().body("주식 구매 실패");
        }
    }

    /**
     * 플레이어 주식 판매
     * @param id 플레이어 ID
     * @param stockIndex 주식 인덱스 (목록 내 위치)
     * @param quantity 판매 수량
     * @return 판매 결과 메시지
     */
    @PostMapping("/{id}/sell")
    public ResponseEntity<String> sellStock(
            @PathVariable String id,
            @RequestParam int stockIndex,
            @RequestParam int quantity) {
        
        boolean success = playerService.sellStock(id, stockIndex, quantity);
        if (success) {
            return ResponseEntity.ok("주식 판매 성공");
        } else {
            return ResponseEntity.badRequest().body("주식 판매 실패");
        }
    }

    /**
     * 플레이어 보유 주식 목록 조회
     * @param id 플레이어 ID
     * @return 주식 목록 문자열
     */
    @GetMapping("/{id}/stocks")
    public ResponseEntity<String> getPlayerStocks(@PathVariable String id) {
        String stocksDisplay = playerService.getPlayerStockDisplay(id);
        return ResponseEntity.ok(stocksDisplay);
    }
}