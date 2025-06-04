package com.rado.producer.service;

import java.util.List;

public interface WebSocketService {
    void sendNumbersViaWebSocket(List<Integer> randomNumbers);
}
