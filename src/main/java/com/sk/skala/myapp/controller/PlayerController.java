package com.sk.skala.myapp.controller;

import com.sk.skala.myapp.model.Player;
import com.sk.skala.myapp.service.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/players")
@RequiredArgsConstructor
public class PlayerController {
    private final PlayerService playerService;

    @GetMapping
    public ResponseEntity<List<Player>> getAllPlayers() {
        return ResponseEntity.ok(playerService.getAllPlayers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Player> getPlayerById(@PathVariable String id) {
        Player player = playerService.findPlayer(id);
        if (player == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(player);
    }

    @PostMapping
    public ResponseEntity<Player> createPlayer(@RequestParam String id, @RequestParam int money) {
        Player player = playerService.createNewPlayer(id, money);
        return ResponseEntity.ok(player);
    }

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

    @GetMapping("/{id}/stocks")
    public ResponseEntity<String> getPlayerStocks(@PathVariable String id) {
        String stocksDisplay = playerService.getPlayerStockDisplay(id);
        return ResponseEntity.ok(stocksDisplay);
    }
}