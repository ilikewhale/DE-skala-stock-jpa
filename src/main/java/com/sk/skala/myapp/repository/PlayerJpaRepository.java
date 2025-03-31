package com.sk.skala.myapp.repository;

import com.sk.skala.myapp.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 플레이어 정보에 접근하기 위한 JPA 리포지토리 인터페이스
 * Spring Data JPA가 자동으로 구현체를 생성함
 */
@Repository
public interface PlayerJpaRepository extends JpaRepository<Player, Long> {
    
    /**
     * 플레이어 ID로 플레이어 정보를 조회
     * @param playerId 플레이어 ID
     * @return Optional<Player> 플레이어 정보 (없을 경우 빈 Optional)
     */
    Optional<Player> findByPlayerId(String playerId);
}