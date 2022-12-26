package com.nowcoder.community.util;

public interface CommunityConstant {
    /**
    * Description: 激活成功
    * date: 2022/12/25 9:54
     *
    * @author: Deng
    * @since JDK 1.8
    */

    int ACTIVATION_SUCCESS = 0 ;

    /**
    * Description: 重复激活
    * date: 2022/12/25 10:01
     *
    * @author: Deng
    * @since JDK 1.8
    */

    int ACTIVATION_REPEAT = 1 ;


    /**
    * Description: 激活失败
    * date: 2022/12/25 10:02
     *
    * @author: Deng
    * @since JDK 1.8
    */

    int ACTIVATION_FAILURE = 2 ;

    /**
    * Description: 默认状态的登录凭证的超时时间
    * date: 2022/12/26 20:52
     * 
    * @author: Deng
    * @since JDK 1.8
    */

    int DEFAULT_EXPIRED_SECONDS = 3600 * 12;
    
    /**
    * Description: 记住状态的登录凭证超时时间
    * date: 2022/12/26 20:52
     * 
    * @author: Deng
    * @since JDK 1.8
    */
    
    int REMEMBER_EXPIRED_SECONDS = 3600 * 24 * 100;

    /**
    * Description: 帖子
    * date: 2022/12/26 20:53
     * 
    * @author: Deng
    * @since JDK 1.8
    */
    int ENTITY_TYPE_POST = 1;

    /**
    * Description: 实体类型: 评论
    * date: 2022/12/26 20:53
     * 
    * @author: Deng
    * @since JDK 1.8
    */
    int ENTITY_TYPE_COMMENT = 2;
    
    /**
    * Description: 实体类型: 用户
    * date: 2022/12/26 21:02
     * 
    * @author: Deng
    * @since JDK 1.8
    */
    int ENTITY_TYPE_USER = 3;

    /**
     * 主题: 评论
     */
    String TOPIC_COMMENT = "comment";

    /**
     * 主题: 点赞
     */
    String TOPIC_LIKE = "like";

    /**
     * 主题: 关注
     */
    String TOPIC_FOLLOW = "follow";

    /**
     * 主题: 发帖
     */
    String TOPIC_PUBLISH = "publish";

    /**
     * 主题: 删帖
     */
    String TOPIC_DELETE = "delete";

    /**
     * 主题: 分享
     */
    String TOPIC_SHARE = "share";

    /**
     * 系统用户ID
     */
    int SYSTEM_USER_ID = 1;

    /**
     * 权限: 普通用户
     */
    String AUTHORITY_USER = "user";

    /**
     * 权限: 管理员
     */
    String AUTHORITY_ADMIN = "admin";

    /**
     * 权限: 版主
     */
    String AUTHORITY_MODERATOR = "moderator";


}
