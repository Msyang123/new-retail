package com.lhiot.newretail.config;

import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import static com.lhiot.newretail.config.NewRetailFreegoConfig.PREFIX;


@Data
@ToString
@RefreshScope
@ConfigurationProperties(prefix = PREFIX)
public class NewRetailFreegoConfig{
    static final String PREFIX = "freego";

    private OrderConfig order;
    @Data
    @ToString
    public static class OrderConfig {
        private String baseUrl;
    }

    private DeliverConfig deliver;

    @Data
    @ToString
    public static class DeliverConfig {
        private String type;
        private String backUrl;
    }
}

