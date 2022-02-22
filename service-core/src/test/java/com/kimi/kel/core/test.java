package com.kimi.kel.core;

import com.kimi.common.util.MD5;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class test {

    @Test
    public void testMD5(){
        String administrator = MD5.encrypt("administrator");
        System.out.println(administrator);
    }
}
