package com.nowcoder.community.actuator;

import com.nowcoder.community.util.CommunityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
* Description: 自定义端点  监控数据库连接是否正常 可以通过输入路径查看返回的json格式的信息得知
* date: 2023/2/13 12:36
 * 路径：../actuator/database  端点的路径访问，要做权限管理，默认管理员才能访问m,在security中管理员那里添加路径即可
* @author: Deng
* @since JDK 1.8
*/
@Slf4j
@Component
@Endpoint(id = "database")
public class DatabaseEndpoint {
    @Autowired
    private DataSource dataSource;

    @ReadOperation
    //get请求 该端点只能通过get请求来访问到

    public String checkConnection() {
        try (
                Connection conn = dataSource.getConnection();//获取数据库连接，放在这可以关闭资源
                ) {
            return CommunityUtil.getJSONString(0, "获取连接成功！");
        } catch (SQLException e) {
            log.error("获取连接失败:" + e.getMessage());
            return CommunityUtil.getJSONString(1, "获取连接失败！");
        }
    }
}
