package com.nowcoder.community.event;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.entity.Event;
import com.nowcoder.community.entity.Message;
import com.nowcoder.community.service.DiscussPostService;
import com.nowcoder.community.service.ElasticserchService;
import com.nowcoder.community.service.MessageService;
import com.nowcoder.community.util.CommunityConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
* Description: 消费者
* date: 2023/1/5 18:56
 *
* @author: Deng
* @since JDK 1.8
*/
@Component
@Slf4j
public class EventConsumer implements CommunityConstant {

    @Resource
    private MessageService messageService;

    @Resource
    private DiscussPostService discussPostService;

//    @Resource
//    private ElasticserchService elasticsearchService;

    //消费3个主题的消息 即发送消息
    @KafkaListener(topics = {TOPIC_COMMENT, TOPIC_LIKE, TOPIC_FOLLOW})
    public void handleCommentMessage(ConsumerRecord record) {
        if (record == null || record.value() == null) {
            log.error("消息的内容为空！");
            return;
        }

        Event event = JSONObject.parseObject(record.value().toString(), Event.class);
        if (event == null) {
            log.error("消息格式错误！");
            return;
        }
        //发送站内通知 构造message对象
        Message message = new Message();
        message.setFromId(SYSTEM_USER_ID);//系统用户id
        message.setToId(event.getEntityUserId());
        message.setConversationId(event.getTopic());//存的是主题
        message.setCreateTime(new Date());

        //聚合消息  content 不方便存的字段，放到content字段
        Map<String, Object> content = new HashMap<>();
        content.put("userId", event.getUserId());//知道了事件是谁触发的
        content.put("entityType", event.getEntityType());//帖子的类型
        content.put("entityId", event.getEntityId());

        if (!event.getData().isEmpty()) {
            for (Map.Entry<String, Object> entry : event.getData().entrySet()) { //遍历
                content.put(entry.getKey(), entry.getValue());
            }
        }
        message.setContent(JSONObject.toJSONString(content));//将content转成json格式的字符串存入message的content中
        messageService.addMessage(message);
    }

    // 消费发帖事件
    @KafkaListener(topics = {TOPIC_PUBLISH})
    public void handlePublishMessage(ConsumerRecord record) {
        if (record == null || record.value() == null) {
            log.error("消息的内容为空!");
            return;
        }

        Event event = JSONObject.parseObject(record.value().toString(), Event.class);
        if (event == null) {
            log.error("消息格式错误!");
            return;
        }

        DiscussPost post = discussPostService.findDiscussPostById(event.getEntityId());
//        elasticsearchService.saveDiscussPost(post);
    }

    // 消费删帖事件
    @KafkaListener(topics = {TOPIC_DELETE})
    public void handleDeleteMessage(ConsumerRecord record) {
        if (record == null || record.value() == null) {
            log.error("消息的内容为空!");
            return;
        }

        Event event = JSONObject.parseObject(record.value().toString(), Event.class);
        if (event == null) {
            log.error("消息格式错误!");
            return;
        }
//        elasticsearchService.deleteDiscussPost(event.getEntityId());
    }

}
