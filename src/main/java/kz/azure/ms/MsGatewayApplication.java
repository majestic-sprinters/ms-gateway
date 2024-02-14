package kz.azure.ms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class MsGatewayApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(MsGatewayApplication.class)
                .web(WebApplicationType.REACTIVE)
                .run(args);
    }

}
