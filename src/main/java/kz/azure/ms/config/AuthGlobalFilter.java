package kz.azure.ms.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class AuthGlobalFilter implements GlobalFilter {

    private final WebClient.Builder webClientBuilder;
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthGlobalFilter.class);


    public AuthGlobalFilter(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String username = exchange.getRequest().getHeaders().getFirst("username");
        String password = exchange.getRequest().getHeaders().getFirst("password");

        if (username == null || password == null) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        Map<String, String> credentials = new HashMap<>();
        credentials.put("username", username);
        credentials.put("password", password);

        return webClientBuilder.build()
                .post()
                .uri("http://localhost:8090/api/v1/auth/login")
                .body(Mono.just(credentials), Map.class)
                .retrieve()
                .toBodilessEntity()
                .flatMap(response -> {
                    if (response.getStatusCode() == HttpStatus.OK) {
                        // Mutate the ServerWebExchange in place
                        exchange.mutate()
                                .request(exchange.getRequest().mutate()
                                        .header("username", username)
                                        .header("password", password)
                                        .build())
                                .build();
                        return chain.filter(exchange);
                    } else {
                        exchange.getResponse().setStatusCode(response.getStatusCode());
                        return exchange.getResponse().setComplete();
                    }
                });
    }
}
