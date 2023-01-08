package com.nowcoder.community;

import com.nowcoder.community.mapper.DiscussPostMapper;
import com.nowcoder.community.mapper.elasticsearch.DiscussPostRepository;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;




@SpringBootTest
public class ElasticserachTests {

    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Autowired
    private DiscussPostRepository discussPostRepository;


    private ElasticsearchTemplate elasticsearchTemplate;

    /**
     * Description: 存入数据到ES
     * date: 2023/1/8 23:23
     *
     * @author: Deng
     * @since JDK 1.8
     */
    @Test
    public void testInsert() {
        //将数据存到ES中
        discussPostRepository.save(discussPostMapper.selectDiscussPostById(21));//类型是discussPost
        discussPostRepository.save(discussPostMapper.selectDiscussPostById(22));//类型是discussPost
    }

    @Test
    public void testInsertList() {
        discussPostRepository.saveAll(discussPostMapper.selectDiscussPosts(3, 0, 10));
    }

    /**
     * Description: 搜索
     * date: 2023/1/8 23:23
     * 排序，分页，结果高亮显示
     *
     * @author: Deng
     * @since JDK 1.8
     */

//    @Test
//    public void testSearchByRepository() {
//        SearchQuery searchQuery = new NativeSearchQueryBuilder()
//                .withQuery(QueryBuilders.multiMatchQuery("互联网", "title", "content"))
//                .withSort(SortBuilders.fieldSort("type").order(SortOrder.DESC))
//                .withSort(SortBuilders.fieldSort("score").order(SortOrder.DESC))
//                .withSort(SortBuilders.fieldSort("createTime").order(SortOrder.DESC))
//                .withPageable(PageRequest.of(0, 10))
//                .withHighlightFields(
//                        new HighlightBuilder.Field("title").preTags("<em>").postTags("</em>"),
//                        new HighlightBuilder.Field("content").preTags("<em>").postTags("</em>")
//                ).build();
//    }
}
