package com.kimi.kel.core.pojo.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Player {

    /**
     * 玩家名
     */
    private String playName;

    /**
     * 玩家角色
     */
//    private String role;

    /**
     * 手牌数
     */
    private Integer number;
}
