package kz.azure.ms.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {


    //Programmatically creating a route
    //Simple routes in the application.yml
    //Flexible and complex routes in the customRouteLocator method
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
//                .route("ms-backend-route", r -> r.path("/ms-backend/**")
//                        .filters(f -> f.rewritePath("/ms-backend/(?<remaining>.*)", "/${remaining}"))
//                        .uri("http://localhost:8080"))
                .build();
    }

}
