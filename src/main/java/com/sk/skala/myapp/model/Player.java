package com.sk.skala.myapp.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;

@Data
@Entity
@NoArgsConstructor
public class Player {
    private String playerId;
    private int playerMoney;
    private List<PlayerStock> playerStocks = new ArrayList<>();

    public Player(String id) {
        this.playerId = id;
        this.playerMoney = 10000;
    }
}