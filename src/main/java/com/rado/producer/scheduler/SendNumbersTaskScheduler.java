package com.rado.producer.scheduler;

import com.rado.producer.config.ConfigureProperties;
import com.rado.producer.handler.StompSessionHandler;
import com.rado.producer.service.implementation.CSVWriterServiceImpl;
import com.rado.producer.service.implementation.ProducerServiceImpl;
import com.rado.producer.service.implementation.RandomNumbersGeneratorServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
public class SendNumbersTaskScheduler {
    private final ThreadPoolTaskScheduler taskScheduler;
    private int generatedNumbersCount = 0;
    private final ProducerServiceImpl producerServiceImpl;
    private final ConfigureProperties configureProperties;
    private final RandomNumbersGeneratorServiceImpl randomNumbersGeneratorServiceImpl;
    private final CSVWriterServiceImpl csvWriterServiceImpl;

    @Autowired
    public SendNumbersTaskScheduler(ThreadPoolTaskScheduler taskScheduler, ProducerServiceImpl producerServiceImpl, ConfigureProperties configureProperties, RandomNumbersGeneratorServiceImpl randomNumbersGeneratorServiceImpl, CSVWriterServiceImpl csvWriterServiceImpl) {
        this.taskScheduler = taskScheduler;
        this.producerServiceImpl = producerServiceImpl;
        this.configureProperties = configureProperties;
        this.randomNumbersGeneratorServiceImpl = randomNumbersGeneratorServiceImpl;
        this.csvWriterServiceImpl = csvWriterServiceImpl;
    }

    @Scheduled(fixedRateString = "${scheduler.milliseconds}")
    public void generateAndSendNumbers() {
        if (generatedNumbersCount >= configureProperties.getMaxStreamSize()) {
            log.info("Generation limit reached: {}", generatedNumbersCount);
            stopScheduler();
            return;
        }

        List<Integer> randomNumbers = new ArrayList<>();

        generateNumbersList(randomNumbers);
        log.info("Random numbers: {}, total generated: {}", randomNumbers, generatedNumbersCount);

        if (configureProperties.isUseWebsocket()) {
            // Send numbers through WebSocket
            WebSocketClient client = new StandardWebSocketClient();
            WebSocketStompClient stompClient = new WebSocketStompClient(client);
            stompClient.setMessageConverter(new MappingJackson2MessageConverter());
            org.springframework.messaging.simp.stomp.StompSessionHandler sessionHandler = new StompSessionHandler(randomNumbers);
            stompClient.connect(configureProperties.getConsumerUrlWebsocket(), sessionHandler);
        } else {
            // Send numbers through REST
            List<Integer> response = producerServiceImpl.sendRandomNumbersToClient(randomNumbers);
            log.info("Returned prime numbers: {}", response);
        }
        // Write numbers to CSV file
        csvWriterServiceImpl.writeRandomNumbersToCSV(randomNumbers);
    }

    public void generateNumbersList(List<Integer> randomNumbers) {
        for (int i = 0; i < configureProperties.getNumbersPerBatch(); i++) {
            if (randomNumbersGeneratorServiceImpl.generateBoolean()) {
                int randomNumber = randomNumbersGeneratorServiceImpl.generateRandomNumber();
                randomNumbers.add(randomNumber);
                generatedNumbersCount++;
            }

            if (generatedNumbersCount >= configureProperties.getMaxStreamSize()) {
                break; // Stop generating random numbers if the maximum required numbers reached
            }
        }

        // Make sure there is at least 1 number generated
        if (randomNumbers.isEmpty()) {
            randomNumbers.add(randomNumbersGeneratorServiceImpl.generateRandomNumber());
        }
    }

    public void stopScheduler() {
        taskScheduler.shutdown();
        log.info("Scheduler stopped.");
        System.exit(0); // Stop the application
    }
}