package tech.liqun.cloud.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.WebFilter;

@SpringBootApplication
public class MyCloudGatewayApplication {

	@Bean
	public WebClient webClient(){
		return WebClient.create();
	}
	@Bean
	@Order(500)
	public WebFilter findRouteFilter(){
		return (exchange, chain) -> {
			exchange.getAttributes().put("requestUrl", "http://httpbin.org/get");
			return chain.filter(exchange);
		};
	}
	// TODO: request only, how to filter response?
	@Bean
	@Order(501)
	public WebFilter modifyResponseFilter() {
		return (exchange, chain) -> {
			exchange.getResponse().getHeaders().add("X-My-Custom", "MyCustomValue");
			return chain.filter(exchange);
		};
	}
	public static void main(String[] args) {
		SpringApplication.run(MyCloudGatewayApplication.class, args);
	}

}
