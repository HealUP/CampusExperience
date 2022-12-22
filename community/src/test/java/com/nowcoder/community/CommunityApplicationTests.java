package com.nowcoder.community;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;

import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
class CommunityApplicationTests {

    @Test
    void contextLoads() {
    }

//    @Test
//    public void testBeanConfig(){
//        SimpleDateFormat simpleDateFormat =
//                applicationContext.getBean(SimpleDateFormat.class);
//        System.out.println(simpleDateFormat.format(new Date()));
//    }
}
