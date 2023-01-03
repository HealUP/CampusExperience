package com.nowcoder.community.service;

import com.nowcoder.community.util.RedisKeyUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
* Description: 点赞服务 使用Redis缓存
* date: 2023/1/3 17:19
 * 
* @author: Deng
* @since JDK 1.8
*/
@Service
public class LikeService {

    @Resource
    private RedisTemplate redisTemplate;

    //判断用户是否点过赞
    
    public void like(int userId, int entityType, int entityId) {
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
        boolean isMember = redisTemplate.opsForSet().isMember(entityLikeKey,userId);
        if (isMember) {
            //该用户点过赞了
            redisTemplate.opsForSet().remove(entityLikeKey, userId);
        } else {
            redisTemplate.opsForSet().add(entityLikeKey, userId);
        }
        
    }
    //查询某实体点赞的数量
    public long findEntityLikeCount(int entityType, int entityId) {
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
        return redisTemplate.opsForSet().size(entityLikeKey);
    }
    //查询某人对某实体的点赞状态
    public int findEntityLikeStatus(int userId,int entityType, int entityId) {
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
        return redisTemplate.opsForSet().isMember(entityLikeKey, userId) ? 1 : 0;
    }

    // 查询某个用户获得的赞
//    public int findUserLikeCount(int userId) {
//        String userLikeKey = RedisKeyUtil.getUserLikeKey(userId);
//        Integer count = (Integer) redisTemplate.opsForValue().get(userLikeKey);
//        return count == null ? 0 : count.intValue();
//    }
}
