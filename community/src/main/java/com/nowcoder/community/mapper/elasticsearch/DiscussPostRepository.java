package com.nowcoder.community.mapper.elasticsearch;

import com.nowcoder.community.entity.DiscussPost;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
* Description: 数据访问层，ES相当于一个特殊的数据库；
* date: 2023/1/8 15:54
 *
* @author: Deng
* @since JDK 1.8
*/


@Repository//spring专门给数据访问层的注解
public interface DiscussPostRepository extends ElasticsearchRepository<DiscussPost, Integer> {
}
