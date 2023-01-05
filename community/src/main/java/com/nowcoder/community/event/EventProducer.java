package com.nowcoder.community.event;

import com.alibaba.fastjson2.JSONObject;
import com.nowcoder.community.entity.Event;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
* Description: 生产者
* date: 2023/1/5 18:53
 *
* @author: Deng
* @since JDK 1.8
*/
@Component
public class EventProducer {
    @Resource
    private KafkaTemplate kafkaTemplate;

    //处理事件
    public void fireEvent(Event event) {
        kafkaTemplate.send(event.getTopic(), JSONObject.toJSONString(event));//发布事件到指定的主题
    }
}
