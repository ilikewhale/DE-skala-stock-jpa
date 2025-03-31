package com.sk.skala.myapp.repository;

import com.sk.skala.myapp.model.Player;
import com.sk.skala.myapp.model.PlayerStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 플레이어 주식 정보에 접근하기 위한 JPA 리포지토리 인터페이스
 * Spring Data JPA가 자동으로 구현체를 생성함
 */
@Repository
public interface PlayerStockJpaRepository extends JpaRepository<PlayerStock, Long> {
    
    /**
     * 플레이어 정보로 해당 플레이어의 모든 주식 정보를 조회
     * @param player 플레이어 정보
     * @return List<PlayerStock> 플레이어가 보유한 주식 목록
     */
    List<PlayerStock> findByPlayer(Player player);
    
    /**
     * 플레이어 정보와 주식 이름으로 해당 플레이어의 특정 주식 정보를 조회
     * @param player 플레이어 정보
     * @param stockName 주식 이름
     * @return Optional<PlayerStock> 플레이어가 보유한 특정 주식 정보 (없을 경우 빈 Optional)
     */
    Optional<PlayerStock> findByPlayerAndStockName(Player player, String stockName);
}