package com.sk.skala.myapp.controller;

import com.sk.skala.myapp.model.Stock;
import com.sk.skala.myapp.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stocks")
@RequiredArgsConstructor
public class StockController {
    private final StockService stockService;

    @GetMapping
    public ResponseEntity<List<Stock>> getAllStocks() {
        return ResponseEntity.ok(stockService.getAllStocks());
    }

    @GetMapping("/{index}")
    public ResponseEntity<Stock> getStockByIndex(@PathVariable int index) {
        Stock stock = stockService.findStockByIndex(index);
        if (stock == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(stock);
    }

    @PostMapping
    public ResponseEntity<Void> addStock(@RequestParam String name, @RequestParam int price) {
        stockService.addStock(name, price);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/simulation/start")
    public ResponseEntity<String> startSimulation() {
        stockService.startStockMarketSimulation();
        return ResponseEntity.ok("Stock market simulation started");
    }

    @PostMapping("/simulation/stop")
    public ResponseEntity<String> stopSimulation() {
        stockService.stopStockMarketSimulation();
        return ResponseEntity.ok("Stock market simulation stopped");
    }
}