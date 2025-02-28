package ru.alex.burdovitsin.eventwaiter.controllers.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.alex.burdovitsin.eventwaiter.model.rest.ExampleDto;
import ru.alex.burdovitsin.eventwaiter.service.ExampleService;

@Slf4j
@Service
public class ExampleKafkaController {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private final ExampleService exampleService;

    public ExampleKafkaController(ExampleService exampleService) {
        this.exampleService = exampleService;
    }

    @KafkaListener(topics = "${spring.kafka.add-example-record-topic}", containerFactory = "stringKafkaListenerContainerFactory")
    public void listenAnswerOne(String input) throws JsonProcessingException {
        log.info("Received: {}", input);
        ExampleDto dto = objectMapper.readValue(input, ExampleDto.class);
        exampleService.addExampleItem(dto);
    }
}
