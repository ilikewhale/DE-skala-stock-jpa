package com.sk.skala.myapp.repository;

import com.sk.skala.myapp.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 주식 정보에 접근하기 위한 JPA 리포지토리 인터페이스
 * Spring Data JPA가 자동으로 구현체를 생성함
 */
@Repository
public interface StockJpaRepository extends JpaRepository<Stock, Long> {
    
    /**
     * 주식 이름으로 주식 정보를 조회
     * @param stockName 주식 이름
     * @return Optional<Stock> 주식 정보 (없을 경우 빈 Optional)
     */
    Optional<Stock> findByStockName(String stockName);
}