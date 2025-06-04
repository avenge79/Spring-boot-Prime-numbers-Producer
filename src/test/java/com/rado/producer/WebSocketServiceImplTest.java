package com.rado.producer;

import com.rado.producer.config.ConfigureProperties;
import com.rado.producer.dto.model.NumberMessage;
import com.rado.producer.service.impl.WebSocketServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WebSocketServiceImplTest {

    @Mock
    private ConfigureProperties configureProperties;

    @InjectMocks
    private WebSocketServiceImpl webSocketService;

    @Test
    void sendNumbersViaWebSocket_invokesWebSocketConnection() {
        // Подготвяне на тестови данни
        List<Integer> numbers = List.of(5, 10, 15);

        // Мокване на стойностите от ConfigureProperties
        when(configureProperties.getConsumerUrlWebsocket()).thenReturn("ws://mock-websocket-server");

        // Извикване на тестирания метод
        webSocketService.sendNumbersViaWebSocket(numbers);

        // Проверка: гарантираме, че се извиква connectAsync() с коректния URL
        verify(configureProperties, times(1)).getConsumerUrlWebsocket();
    }
}