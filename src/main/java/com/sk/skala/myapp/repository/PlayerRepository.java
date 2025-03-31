package com.sk.skala.myapp.repository;

import com.sk.skala.myapp.model.Player;
import com.sk.skala.myapp.model.PlayerStock;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PlayerRepository {
    // 플레이어 정보를 저장할 파일
    private final String PLAYER_FILE = "data/players.txt";

    // 플레이어 정보 목록 (메모리)
    private List<Player> playerList = new ArrayList<>();

    // 플레이어 정보를 파일에서 읽어옴
    public void loadPlayerList() {
        try (BufferedReader reader = new BufferedReader(new FileReader(PLAYER_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Player player = parseLineToPlayer(line);
                if (player != null) {
                    playerList.add(player);
                }
            }
        } catch (IOException e) {
            System.out.println("플레이어 정보가 없습니다.");
        }
    }

    // 플레이어 정보를 파일에 저장
    public void savePlayerList() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PLAYER_FILE))) {
            for (Player player : playerList) {
                writer.write(player.getPlayerId() + "," + player.getPlayerMoney());
                if (player.getPlayerStocks().size() > 0) {
                    writer.write("," + player.getPlayerStocksForFile());
                }
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("파일에 저장하는 중 오류가 발생했습니다.");
        }
    }

    // 파일 라인을 Player 객체로 변환
    private Player parseLineToPlayer(String line) {
        String[] fields = line.split(",");
        if (fields.length > 1) {
            Player player = new Player();
            player.setPlayerId(fields[0]);
            player.setPlayerMoney(Integer.parseInt(fields[1]));

            if (fields.length > 2 && fields[2].indexOf(":") > 0) {
                player.setPlayerStocks(parseFieldToStockList(fields[2]));
            }
            return player;
        } else {
            System.out.println("라인을 분석할 수 없습니다. line=" + line);
            return null;
        }
    }

    private List<PlayerStock> parseFieldToStockList(String field) {
        List<PlayerStock> list = new ArrayList<>();

        String[] stocks = field.split("\\|");
        for (int i = 0; i < stocks.length; i++) {
            String[] props = stocks[i].split(":");
            if (props.length > 2) {
                list.add(new PlayerStock(props[0], props[1], props[2]));
            }
        }
        return list;
    }

    // 플레이어 검색
    public Player findPlayer(String id) {
        for (Player player : playerList) {
            if (player.getPlayerId().equals(id)) {
                return player;
            }
        }
        return null;
    }

    // 플레이어 추가 후 파일에 저장
    public void addPlayer(Player player) {
        playerList.add(player);
        savePlayerList();
    }
    
    public List<Player> getAllPlayers() {
        return playerList;
    }
}
