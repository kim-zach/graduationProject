package com.kimi.kel.core.utils;

import com.kimi.kel.core.pojo.entities.DefaultWord;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import sun.rmi.runtime.Log;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Set;

@Slf4j
public class RulesCheckUtil {

    @Resource
    private RedisTemplate redisTemplate;

    public  boolean check(String word){
        char[] chars = word.toCharArray();
        Set<String> originalSet = new HashSet<>();

        redisTemplate.opsForSet().unionAndStore("kel:core:" + chars[0] + "Set",originalSet,originalSet);

        for(Character c : chars){
            redisTemplate.opsForSet().intersectAndStore("kel:core:" + c + "Set",originalSet,originalSet);
        }
        Set members = redisTemplate.opsForSet().members(originalSet);
        log.info("members:{}",members);
//        for(Object s : members){
//
//        }
        return true;
    }
}
