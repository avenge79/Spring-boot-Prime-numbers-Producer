package com.rado.producer.service.implementation;

import com.rado.producer.config.ConfigureProperties;
import com.rado.producer.service.CSVWriterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
@Slf4j
public class CSVWriterServiceImpl implements CSVWriterService {
    private final ConfigureProperties configureProperties;

    String errorWriting = "Error writing random numbers to CSV file: {}";

    @Autowired
    public CSVWriterServiceImpl(ConfigureProperties configureProperties) {
        this.configureProperties = configureProperties;
    }

    public void writeRandomNumbersToCSV(List<Integer> randomNumbers) {
        if (randomNumbers == null || randomNumbers.isEmpty()) {
            return;
        }

        try {
            Path fileName = Path.of(configureProperties.getCsvFileFolder());
            String fullFilePath = Paths.get(String.valueOf(fileName), configureProperties.getCsvFileName()).toString();

            // Create folder if it doesn't exist
            Files.createDirectories(fileName);
            boolean hasData = false;

            writeCsvFile(randomNumbers, fullFilePath, hasData);
        } catch (IOException e) {
            log.error(errorWriting, e.getMessage());
        }
    }

    public void writeCsvFile(List<Integer> randomNumbers, String fullFilePath, boolean hasData) {
        try (FileWriter writer = new FileWriter(fullFilePath, true)) {
            File file = new File(fullFilePath);
            if (file.exists() && file.isFile() && file.length() > 0) {
                hasData = true;
            }
            String appendString = StringUtils.collectionToCommaDelimitedString(randomNumbers);
            if (hasData) {
                writer.append(",");
            }
            writer.append(appendString);
            writer.flush();

            log.info("Random numbers written to CSV file successfully.");
        } catch (Exception e) {
            log.error(errorWriting, e.getMessage());
        }
    }
}