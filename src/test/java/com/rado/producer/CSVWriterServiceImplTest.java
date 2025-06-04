package com.rado.producer;

import com.rado.producer.config.ConfigureProperties;
import com.rado.producer.service.impl.CSVWriterServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CSVWriterServiceImplTest {

    @Mock
    private ConfigureProperties configureProperties;

    @InjectMocks
    private CSVWriterServiceImpl csvWriterService;

    private Path tempFolder;
    private String testCsvFileName = "test.csv";

    @BeforeEach
    void setUp() throws IOException {
        tempFolder = Files.createTempDirectory("csv-test");
        lenient().when(configureProperties.getCsvFileFolder()).thenReturn(tempFolder.toString());
        lenient().when(configureProperties.getCsvFileName()).thenReturn(testCsvFileName);
    }

    @Test
    void writeRandomNumbersToCSV_createsFileAndWritesData() throws IOException {
        List<Integer> numbers = List.of(10, 20, 30);

        csvWriterService.writeRandomNumbersToCSV(numbers);

        Path csvFilePath = tempFolder.resolve(testCsvFileName);
        assertTrue(Files.exists(csvFilePath));

        String content = Files.readString(csvFilePath);
        assertEquals(StringUtils.collectionToCommaDelimitedString(numbers), content);
    }

    @Test
    void writeRandomNumbersToCSV_handlesEmptyList() throws IOException {
        csvWriterService.writeRandomNumbersToCSV(List.of());

        Path csvFilePath = tempFolder.resolve(testCsvFileName);
        assertFalse(Files.exists(csvFilePath)); // Файлът не трябва да се създава
    }
}
