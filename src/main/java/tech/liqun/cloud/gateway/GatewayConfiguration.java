package tech.liqun.cloud.gateway;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import tech.liqun.cloud.gateway.filters.FindRouterFilter;

/**
 * @author DHC
 **/
@Configuration
@EnableConfigurationProperties
public class GatewayConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public WebClient webClient() {
        return WebClient.create();
    }

    @Bean
    public FindRouterFilter findRouterFilter(GatewayProperties properties) {
        return new FindRouterFilter(properties);
    }

    @Bean
    public GatewayProperties gatewayProperties() {
        return new GatewayProperties();
    }

    @Bean
    public GatewayWebHandler gatewayWebHandler(GatewayProperties properties, WebClient webClient) {
        return new GatewayWebHandler(properties, webClient);
    }

    @Bean
    public GatewayHandlerMapping gatewayHandlerMapping(GatewayProperties properties, GatewayWebHandler gatewayWebHandler) {
        return new GatewayHandlerMapping(properties, gatewayWebHandler);
    }
}
