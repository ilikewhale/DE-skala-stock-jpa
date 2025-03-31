package com.sk.skala.myapp.service;

import com.sk.skala.myapp.model.Stock;
import com.sk.skala.myapp.repository.StockRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class StockService {
    private final StockRepository stockRepository;
    private Thread stockSimulationThread;
    private boolean running = false;

    @PostConstruct
    public void initialize() {
        stockRepository.loadStockList();
    }

    public List<Stock> getAllStocks() {
        return stockRepository.getStockList();
    }

    public Stock findStockByIndex(int index) {
        return stockRepository.findStock(index);
    }

    public Stock findStockByName(String name) {
        return stockRepository.findStock(name);
    }

    public void addStock(String name, int price) {
        Stock newStock = new Stock(name, price);
        stockRepository.addStock(newStock);
    }

    public String getStockListForDisplay() {
        return stockRepository.getStockListForMenu();
    }

    // 주식 가격을 변경하는 메서드
    public void changeStockPrice(Stock stock) {
        Random random = new Random();
        int fluctuation = random.nextInt(21) - 10; // -10 ~ +10 사이의 랜덤한 값 생성
        stock.setStockPrice(stock.getStockPrice() + fluctuation);

        // 가격이 0 이하로 내려가지 않도록 보정
        if (stock.getStockPrice() < 0) {
            stock.setStockPrice(0);
        }
    }

    // 주식 가격 시뮬레이션을 시작하는 메서드 (5초마다 가격 변동)
    public void startStockMarketSimulation() {
        if (running) return;

        running = true;
        stockSimulationThread = new Thread(() -> {
            while (running) {
                try {
                    // 모든 주식 가격을 5초마다 랜덤하게 변동
                    for (Stock stock : stockRepository.getStockList()) {
                        changeStockPrice(stock); // 가격 변경
                        System.out.println("변경된 주식 가격: " + stock);  // 변동된 주식 가격 출력
                    }
                    stockRepository.saveStockList();  // 변경된 가격 저장

                    // 5초 대기
                    Thread.sleep(5000);
                    System.out.println("********💡실시간 업데이트 중💡********");
                } catch (InterruptedException e) {
                    System.out.println("실시간 업데이트를 잠시 멈춰드릴게요");
                    running = false;
                    break;  // 스레드 종료
                }
            }
        });
        stockSimulationThread.start();  // 주식 가격 변동 스레드 시작
    }

    // 시뮬레이션 중지
    public void stopStockMarketSimulation() {
        if (stockSimulationThread != null && running) {
            running = false;
            stockSimulationThread.interrupt();
        }
    }
}
