package com.nowcoder.community.Config;

import org.springframework.boot.actuate.autoconfigure.metrics.export.prometheus.PrometheusMetricsExportAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;
/**
* Description: 配置一个格式日期类，@Bean让其加载到IOC容器中
* date: 2022/12/22 10:47
 *
* @author: Deng
* @since JDK 1.8
*/
@Configuration
public class AlphaConfig {
    @Bean
    public SimpleDateFormat simpleDateFormat(){
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }
}
