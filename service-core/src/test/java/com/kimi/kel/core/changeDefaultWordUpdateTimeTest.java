package com.kimi.kel.core;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.kimi.kel.core.pojo.entities.DefaultWord;
import com.kimi.kel.core.service.DefaultWordService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
public class changeDefaultWordUpdateTimeTest {

    @Resource
    DefaultWordService defaultWordService;

    @Test
    public void test(){
        UpdateWrapper<DefaultWord> defaultWordUpdateWrapper = new UpdateWrapper<>();
        defaultWordUpdateWrapper
                .in("word_spell","A")
                .set("description","character a ");
        defaultWordService.update(defaultWordUpdateWrapper);
    }
}
