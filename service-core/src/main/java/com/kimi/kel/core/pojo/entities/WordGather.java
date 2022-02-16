package com.kimi.kel.core.pojo.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WordGather {

    /**
     * 保存玩家信息
     */
    private List<Player> playerList;

    /**
     * 保存上一次的牌
     */
    private List<Card> lastCards;

    /**
     * 保存上一次出牌的玩家名
     */
    private String lastCardPlayerName;
}
