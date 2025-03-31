package com.sk.skala.myapp.service;

import com.sk.skala.myapp.model.Stock;
import com.sk.skala.myapp.repository.StockJpaRepository;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

/**
 * 주식 관련 비즈니스 로직을 처리하는 서비스 클래스
 */
@Service
@RequiredArgsConstructor
public class StockService {
    private final StockJpaRepository stockJpaRepository;
    private Thread stockSimulationThread;
    private volatile boolean running = false;

    /**
     * 서비스 초기화 메서드 - 기본 주식 정보 로드
     */
    @PostConstruct
    public void initialize() {
        loadStockList();
    }

    /**
     * 데이터베이스에서 주식 정보를 로드하고, 없으면 기본 데이터 추가
     */
    @Transactional
    public void loadStockList() {
        List<Stock> stockList = stockJpaRepository.findAll();
        if (stockList.isEmpty()) {
            // 기본 주식 데이터 추가
            addStock("apple", 100);
            addStock("happy6team", 800);
            addStock("samsung", 120);
        }
    }

    /**
     * 모든 주식 정보 조회
     * @return List<Stock> 주식 목록
     */
    public List<Stock> getAllStocks() {
        return stockJpaRepository.findAll();
    }

    /**
     * ID로 주식 정보 조회
     * @param index 주식 ID
     * @return Stock 주식 정보 (없을 경우 null)
     */
    public Stock findStockByIndex(int index) {
        return stockJpaRepository.findById((long) index).orElse(null);
    }

    /**
     * 주식 이름으로 주식 정보 조회
     * @param name 주식 이름
     * @return Stock 주식 정보 (없을 경우 null)
     */
    public Stock findStockByName(String name) {
        return stockJpaRepository.findByStockName(name).orElse(null);
    }

    /**
     * 새로운 주식 추가
     * @param name 주식 이름
     * @param price 주식 가격
     */
    @Transactional
    public void addStock(String name, int price) {
        // 이미 존재하는 주식인지 확인
        if (stockJpaRepository.findByStockName(name).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 주식 이름입니다: " + name);
        }
        
        Stock newStock = new Stock(name, price);
        stockJpaRepository.save(newStock);
    }

    /**
     * 주식 목록을 화면 표시용 문자열로 변환
     * @return String 화면 표시용 문자열
     */
    public String getStockListForDisplay() {
        StringBuilder sb = new StringBuilder();
        List<Stock> stockList = stockJpaRepository.findAll();
        for (Stock stock : stockList) {
            sb.append(stock.toString()).append(System.lineSeparator());
        }
        return sb.toString();
    }

    /**
     * 주식 가격 변경
     * @param stock 주식 정보
     */
    @Transactional
    public void changeStockPrice(Stock stock) {
        Random random = new Random();
        int fluctuation = random.nextInt(21) - 10; // -10 ~ +10 사이의 랜덤 가격 변동
        
        int newPrice = stock.getStockPrice() + fluctuation;
        if (newPrice < 1) {
            newPrice = 1; // 최소 가격은 1로 설정
        }
        
        stock.setStockPrice(newPrice);
        stockJpaRepository.save(stock);
    }

    /**
     * 주식 시장 시뮬레이션 시작 - 5초마다 모든 주식 가격 변동
     */
    public void startStockMarketSimulation() {
        if (running) {
            return; // 이미 실행 중이면 종료
        }

        running = true;
        stockSimulationThread = new Thread(() -> {
            try {
                while (running) {
                    // 모든 주식 가격을 변동시킴
                    List<Stock> stockList = stockJpaRepository.findAll();
                    for (Stock stock : stockList) {
                        changeStockPrice(stock);
                        System.out.println("변경된 주식 가격: " + stock);
                    }

                    // 5초 대기
                    System.out.println("********💡실시간 업데이트 중💡********");
                    Thread.sleep(5000);
                }
            } catch (InterruptedException e) {
                System.out.println("실시간 업데이트를 잠시 멈춰드릴게요");
            } finally {
                running = false;
            }
        });
        
        stockSimulationThread.setDaemon(true); // 메인 스레드 종료 시 함께 종료되도록 설정
        stockSimulationThread.start();
    }

    /**
     * 주식 시장 시뮬레이션 중지
     */
    public void stopStockMarketSimulation() {
        if (running && stockSimulationThread != null) {
            running = false;
            stockSimulationThread.interrupt();
            stockSimulationThread = null;
        }
    }
}