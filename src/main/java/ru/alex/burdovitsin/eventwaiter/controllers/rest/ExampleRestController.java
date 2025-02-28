package ru.alex.burdovitsin.eventwaiter.controllers.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.alex.burdovitsin.eventwaiter.model.rest.ExampleDto;
import ru.alex.burdovitsin.eventwaiter.service.ExampleService;

import java.util.List;

@RestController
public class ExampleRestController {

    private final ExampleService exampleService;

    public ExampleRestController(ExampleService exampleService) {
        this.exampleService = exampleService;
    }

    @GetMapping("/echo")
    public String echo() {
        return "ECHO";
    }

    public void addExampleItem(ExampleDto dto) {
        exampleService.addExampleItem(dto);
    }

    public List<ExampleDto> getAllExampleItems() {
        return exampleService.getAllExampleItems();
    }
}
