package com.guilherme.faqiaapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

// RestClient e a forma moderna do Spring de fazer chamadas HTTP para
// APIs externas (substitui o antigo RestTemplate). Configuramos aqui
// a URL base da Anthropic uma unica vez.
@Configuration
public class RestClientConfig {

    @Bean
    public RestClient anthropicRestClient() {
        return RestClient.builder()
                .baseUrl("https://api.anthropic.com/v1")
                .build();
    }
}
