package com.rado.producer.service;

import java.util.List;

public interface CSVWriterService {
    void writeRandomNumbersToCSV(List<Integer> randomNumbers);
    void writeCsvFile(List<Integer> randomNumbers, String fullFilePath, boolean hasData);
}
