package ru.alex.burdovitsin.eventwaiter.service;

import ru.alex.burdovitsin.eventwaiter.model.rest.ExampleDto;

import java.util.List;

public interface ExampleService {

    void addExampleItem(ExampleDto dto);

    List<ExampleDto> getAllExampleItems();
}
