package com.ssafy.piece.global.config;

import io.netty.channel.ChannelOption;
import java.time.Duration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

//@Configuration
//public class WebClientConfig {
//
//    @Bean
//    public ReactorResourceFactory resourceFactory() {
//        ReactorResourceFactory factory = new ReactorResourceFactory();
//        factory.setUseGlobalResources(false);
//        return factory;
//    }
//
//    @Bean
//    public WebClient webClient() {
//        Function<HttpClient, HttpClient> mapper = client -> HttpClient.create()
//            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 1000)
//            .doOnConnected(connection -> connection.addHandlerLast(new ReadTimeoutHandler(10))
//                .addHandlerLast(new WriteTimeoutHandler(10)))
//            .responseTimeout(Duration.ofSeconds(1));
//
//        ClientHttpConnector connector =
//            new ReactorClientHttpConnector(resourceFactory(), mapper);
//        return WebClient.builder()
//            .clientConnector(connector)
//            .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(20 * 1024 * 1024))
//            .build();
//    }
//
//}
@Configuration
public class WebClientConfig {

    DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory();

    HttpClient httpClient = HttpClient.create()
        .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000); // 10초

    @Bean
    public WebClient webClient() {
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.VALUES_ONLY);
        return WebClient.builder()
            .uriBuilderFactory(factory)
            .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(20 * 1024 * 1024))
            .clientConnector(new ReactorClientHttpConnector(httpClient))
            .build();
    }

    @Bean
    public ConnectionProvider connectionProvider() {
        return ConnectionProvider.builder("http-pool")
            .maxConnections(100)
            .pendingAcquireTimeout(Duration.ofMillis(0))
            .pendingAcquireMaxCount(-1)
            .maxIdleTime(Duration.ofMillis(1000L))
            .build();
    }
}