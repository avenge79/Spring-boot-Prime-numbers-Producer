package com.rado.producer.service.impl;

import com.rado.producer.config.ConfigureProperties;
import com.rado.producer.dto.ProducerDTO;
import com.rado.producer.scheduler.SendNumbersTaskScheduler;
import com.rado.producer.service.CSVWriterService;
import com.rado.producer.service.NumberSendingService;
import com.rado.producer.service.ProducerService;
import com.rado.producer.service.WebSocketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NumberSendingServiceImpl implements NumberSendingService {
    private final ConfigureProperties configureProperties;
    private final WebSocketService webSocketService;
    private final ProducerService producerService;
    private final CSVWriterService csvWriterService;

    public boolean sendNumbers(List<Integer> numbers) {
        if (configureProperties.isUseWebsocket()) {
            webSocketService.sendNumbersViaWebSocket(numbers);
        } else {
            ProducerDTO response = producerService.sendRandomNumbersToClient(numbers);
            if (response.getError() != null && !response.getError().isEmpty()) {
                log.error("Error from Consumer, stopping scheduler.");
                return false;
            }
            log.info("Returned prime numbers: {}", response.getMessage().getNumbers());
        }

        csvWriterService.writeRandomNumbersToCSV(numbers);
        return true;
    }
}
