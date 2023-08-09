package com.rado.producer.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

import java.lang.reflect.Type;
import java.util.List;

import static com.rado.producer.handler.MappingConstants.MESSAGE_MAPPING;

@Slf4j
public class StompSessionHandler extends StompSessionHandlerAdapter {
    private final List<Integer> message;
    public static boolean connectionError = false;

    public StompSessionHandler(List<Integer> message) {
        this.message = message;
    }

    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        log.info("New STOMP session : {}", session.getSessionId());

        session.send(MESSAGE_MAPPING, message);
        log.info("Message sent to websocket server");
    }

    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
        log.error("Exception: {}", exception.getMessage());
        connectionError = true;
    }

    @Override
    public void handleTransportError(StompSession session, Throwable exception) {
        log.error("Error on websocket session {}, {}", session.getSessionId(), exception.getMessage());
        connectionError = true;
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return String.class;
    }
}