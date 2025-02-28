package ru.alex.burdovitsin.eventwaiter.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.alex.burdovitsin.eventwaiter.model.jpa.ExampleRecord;
import ru.alex.burdovitsin.eventwaiter.model.rest.ExampleDto;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ExampleMapper {

    ExampleRecord dtoToRecord(ExampleDto exampleDto);

    ExampleDto recordToDto(ExampleRecord exampleRecord);

    List<ExampleDto> recordsToDtos(List<ExampleRecord> exampleRecords);
}
