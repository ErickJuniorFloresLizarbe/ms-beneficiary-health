package pe.edu.vallegrande.health.webclient;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Configuration
public class WebClientConfig {

    @Value("${core-service.url:https://ms-beneficiary-person.onrender.com}") // URL del microservicio destino
    private String baseUrl;

    @Bean
    public WebClient coreServiceWebClient() {
        return WebClient.builder()
                .baseUrl(baseUrl)
                .filter(authHeaderFilter()) // Propaga el token JWT
                .build();
    }

    private ExchangeFilterFunction authHeaderFilter() {
        return (request, next) -> Mono.deferContextual(ctx -> {
            if (ctx.hasKey("Authorization")) {
                String token = ctx.get("Authorization");
                return next.exchange(
                    ClientRequest.from(request)
                        .headers(headers -> headers.setBearerAuth(token))
                        .build()
                );
            }
            return next.exchange(request);
        });
    }

}
