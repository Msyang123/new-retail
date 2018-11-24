package com.lhiot.newretail;

import com.leon.microx.util.SnowflakeId;
import lombok.Data;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * Spring Boot 2.x 新零售服务
 *
 * @author yijun (290828414@qq.com) created in 12:11 18.11.08
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@Data
@ConfigurationProperties(prefix = "new-retail-service.snowflake-id")
public class NewRetailServer {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    private  long workerId;
    private long dataCenterId;

    @Bean
    public SnowflakeId snowflakeId(){
        return new SnowflakeId(workerId,dataCenterId);
    }

    public static void main(String[] args) {
        SpringApplication.run(NewRetailServer.class, args);
    }
}
