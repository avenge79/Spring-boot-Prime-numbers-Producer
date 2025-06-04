package com.rado.producer.service.impl;

import com.rado.producer.config.ConfigureProperties;
import com.rado.producer.handler.StompSessionHandler;
import com.rado.producer.dto.model.NumberMessage;
import com.rado.producer.service.WebSocketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class WebSocketServiceImpl implements WebSocketService {
    private final ConfigureProperties configureProperties;

    @Override
    public void sendNumbersViaWebSocket(List<Integer> randomNumbers) {
        WebSocketClient client = new StandardWebSocketClient();
        WebSocketStompClient stompClient = new WebSocketStompClient(client);
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        NumberMessage message = new NumberMessage();
        message.setNumbers(randomNumbers);

        StompSessionHandler sessionHandler = new StompSessionHandler(message);

        stompClient.connectAsync(configureProperties.getConsumerUrlWebsocket(), sessionHandler)
                .thenAccept(session ->
                        log.info("Connected to WebSocket server with session ID: {}", session.getSessionId()))
                .exceptionally(ex -> {
                    log.error("WebSocket connection failed: {}", ex.getMessage());
                    return null;
                });
    }
}
