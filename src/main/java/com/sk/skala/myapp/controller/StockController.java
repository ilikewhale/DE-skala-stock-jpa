package com.sk.skala.myapp.controller;

import com.sk.skala.myapp.model.Stock;
import com.sk.skala.myapp.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 주식 관련 API 엔드포인트를 제공하는 REST 컨트롤러
 */
@RestController
@RequestMapping("/api/stocks")
@RequiredArgsConstructor
public class StockController {
    private final StockService stockService;

    /**
     * 모든 주식 정보 조회
     * @return 주식 목록
     */
    @GetMapping
    public ResponseEntity<List<Stock>> getAllStocks() {
        return ResponseEntity.ok(stockService.getAllStocks());
    }

    /**
     * 특정 주식 정보를 인덱스(ID)로 조회
     * @param index 주식 인덱스
     * @return 주식 정보
     */
    @GetMapping("/{index}")
    public ResponseEntity<Stock> getStockByIndex(@PathVariable int index) {
        Stock stock = stockService.findStockByIndex(index);
        if (stock == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(stock);
    }

    /**
     * 새로운 주식 추가
     * @param name 주식 이름
     * @param price 주식 가격
     * @return 응답 상태
     */
    @PostMapping
    public ResponseEntity<?> addStock(@RequestParam String name, @RequestParam int price) {
        try {
            stockService.addStock(name, price);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * 주식 시장 시뮬레이션 시작
     * @return 시작 메시지
     */
    @PostMapping("/simulation/start")
    public ResponseEntity<String> startSimulation() {
        stockService.startStockMarketSimulation();
        return ResponseEntity.ok("Stock market simulation started");
    }

    /**
     * 주식 시장 시뮬레이션 중지
     * @return 중지 메시지
     */
    @PostMapping("/simulation/stop")
    public ResponseEntity<String> stopSimulation() {
        stockService.stopStockMarketSimulation();
        return ResponseEntity.ok("Stock market simulation stopped");
    }
}