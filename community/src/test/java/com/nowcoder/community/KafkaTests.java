package com.nowcoder.community;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class KafkaTests {

    @Autowired
    private KafkaProducer kafkaProducer;

    @Test
    public void testKafka() {
        kafkaProducer.sendMessage("test", "你好");//发布到test的主题
        kafkaProducer.sendMessage("test", "在吗");

        try {
            Thread.sleep(1000 * 10);//等待一会
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}

/**
* Description: 消息生产者 注入到IOC容器中管理
* date: 2023/1/5 18:21
 * 生产者发消息主动发的
* @author: Deng
* @since JDK 1.8
*/
@Component
class KafkaProducer {

    @Autowired
    private KafkaTemplate kafkaTemplate;

    //主动的发送消息
    public void sendMessage(String topic, String content) {
        kafkaTemplate.send(topic, content);
    }

}

/**
* Description: 消息消费者 注入到IOC容器中管理
* date: 2023/1/5 18:21
 * 被动的去调用的
* @author: Deng
* @since JDK 1.8
*/

@Component
class KafkaConsumer {

    @KafkaListener(topics = {"test"})//设置主题为test
    public void handleMessage(ConsumerRecord record) {
        System.out.println(record.value());
    }


}
