package ru.alex.burdovitsin.eventwaiter.repository;

import ru.alex.burdovitsin.eventwaiter.model.jpa.ExampleRecord;

import java.util.List;

public interface ExampleRepository {

    void addRecord(ExampleRecord record);

    List<ExampleRecord> getAllRecords();
}
