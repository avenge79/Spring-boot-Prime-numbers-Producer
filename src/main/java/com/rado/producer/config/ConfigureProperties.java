package com.rado.producer.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties
@Getter
@Setter
public class ConfigureProperties {
    @Value("${csv.file.folder}")
    private String csvFileFolder;

    @Value("${csv.file.name}")
    private String csvFileName;

    @Value("${consumer.url.rest}")
    private String consumerUrlRest;

    @Value("${consumer.url.websocket}")
    private String consumerUrlWebsocket;

    @Value("${max.number}")
    private int randomNumberMax;

    @Value("${max.stream.size}")
    private int maxStreamSize;

    @Value("${numbers.per.batch}")
    private int numbersPerBatch;

    @Value("${websocket.use}")
    private boolean useWebsocket;
}