package ru.alex.burdovitsin.eventwaiter.service.impl;

import org.springframework.stereotype.Service;
import ru.alex.burdovitsin.eventwaiter.mapper.ExampleMapper;
import ru.alex.burdovitsin.eventwaiter.model.jpa.ExampleRecord;
import ru.alex.burdovitsin.eventwaiter.model.rest.ExampleDto;
import ru.alex.burdovitsin.eventwaiter.repository.ExampleRepository;
import ru.alex.burdovitsin.eventwaiter.service.ExampleService;

import java.util.List;

@Service
public class ExampleServiceImpl implements ExampleService {

    private final ExampleRepository exampleRepository;

    private final ExampleMapper exampleMapper;

    public ExampleServiceImpl(ExampleRepository exampleRepository, ExampleMapper exampleMapper) {
        this.exampleRepository = exampleRepository;
        this.exampleMapper = exampleMapper;
    }

    @Override
    public void addExampleItem(ExampleDto dto) {
        ExampleRecord record = exampleMapper.dtoToRecord(dto);
        exampleRepository.addRecord(record);
    }

    @Override
    public List<ExampleDto> getAllExampleItems() {
        List<ExampleRecord> records = exampleRepository.getAllRecords();
        return exampleMapper.recordsToDtos(records);
    }
}
