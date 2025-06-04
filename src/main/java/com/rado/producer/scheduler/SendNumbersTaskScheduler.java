package com.rado.producer.scheduler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.rado.producer.config.ConfigureProperties;
import com.rado.producer.dto.ProducerDTO;
import com.rado.producer.handler.StompSessionHandler;
import com.rado.producer.model.NumberMessage;
import com.rado.producer.service.CSVWriterService;
import com.rado.producer.service.ProducerService;
import com.rado.producer.service.RandomNumbersGeneratorService;
import com.rado.producer.service.WebSocketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class SendNumbersTaskScheduler {
    private final ThreadPoolTaskScheduler taskScheduler;
    private int generatedNumbersCount;
    private final ProducerService producerService;
    private final ConfigureProperties configureProperties;
    private final RandomNumbersGeneratorService randomNumbersGeneratorService;
    private final CSVWriterService csvWriterService;
    private final WebSocketService webSocketService;

    @Scheduled(fixedRateString = "${scheduler.milliseconds}")
    public void generateAndSendNumbers() throws JsonProcessingException {
        if (generatedNumbersCount >= configureProperties.getMaxStreamSize()) {
            log.info("Generation limit reached: {}", generatedNumbersCount);
            stopScheduler();
            return;
        }

        List<Integer> randomNumbers = generateNumbersList();
        log.info("Random numbers: {}, total generated: {}", randomNumbers, generatedNumbersCount);

        if (configureProperties.isUseWebsocket()) {
            webSocketService.sendNumbersViaWebSocket(randomNumbers);
        } else {
            ProducerDTO producerResponse = producerService.sendRandomNumbersToClient(randomNumbers);
            if (producerResponse.getError() != null && !producerResponse.getError().isEmpty()) {
                log.error("Error from Consumer, is it running?");
                stopScheduler();
            }
            log.info("Returned prime numbers: {}", producerResponse.getMessage().getNumbers());
        }

        csvWriterService.writeRandomNumbersToCSV(randomNumbers);
    }


    private void sendWebSocketMessage() {
        List<Integer> randomNumbers = new ArrayList<>();
        WebSocketClient client = new StandardWebSocketClient();
        WebSocketStompClient stompClient = new WebSocketStompClient(client);
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        NumberMessage message = new NumberMessage();
        message.setNumbers(randomNumbers);

        StompSessionHandler sessionHandler = new StompSessionHandler(message);
        if (StompSessionHandler.connectionError) {
            log.error("Error connecting to Consumer Websocket endpoint, is it running?");
            stopScheduler();
        }

        stompClient.connectAsync(configureProperties.getConsumerUrlWebsocket(), sessionHandler)
                .thenAccept(session ->
                        log.info("Connected to WebSocket server with session ID: {}",
                                session.getSessionId()))
                .exceptionally(ex -> {
                    log.error("WebSocket connection failed: {}", ex.getMessage());
                    stopScheduler();
                    return null;
                });
    }

    public List<Integer> generateNumbersList() {
        List<Integer> randomNumbers = new ArrayList<>();
        for (int i = 0; i < configureProperties.getNumbersPerBatch(); i++) {
            if (randomNumbersGeneratorService.generateBoolean()) {
                int randomNumber = randomNumbersGeneratorService.generateRandomNumber();
                randomNumbers.add(randomNumber);
                generatedNumbersCount++;
            }

            if (generatedNumbersCount >= configureProperties.getMaxStreamSize()) {
                break; // Stop generating random numbers if the maximum required numbers reached
            }
        }

        // Make sure there is at least 1 number generated
        if (randomNumbers.isEmpty()) {
            randomNumbers.add(randomNumbersGeneratorService.generateRandomNumber());
        }
        return randomNumbers;
    }

    public void stopScheduler() {
        taskScheduler.shutdown();
        log.info("Scheduler stopped.");
        System.exit(0); // Stop the application
    }
}