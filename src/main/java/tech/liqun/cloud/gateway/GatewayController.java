package tech.liqun.cloud.gateway;

import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;

/**
 * @Author dinghaocheng
 * @Date 2020/12/17 10:53 上午
 * @Version 1.0
 **/
@RestController
public class GatewayController {
    private final WebClient webClient;
    private final DataBufferFactory bufferFactory;

    public GatewayController() {
        webClient = WebClient.builder().build();
        bufferFactory = new DefaultDataBufferFactory();
    }

    @GetMapping("/")
    public Mono<Void> home(ServerWebExchange exchange) {
        return this.webClient.get().uri(URI.create("http://httpbin.org/get")).accept(MediaType.APPLICATION_JSON)
                .exchange().flatMap(clientResponse -> {
                    ServerHttpResponse response = exchange.getResponse();
                    response.getHeaders().setContentType(clientResponse.headers().contentType().get());
                    response.setStatusCode(clientResponse.statusCode());
                    return response.writeWith(clientResponse.body((inputMessage, context) -> inputMessage.getBody()));
                });
    }


}
