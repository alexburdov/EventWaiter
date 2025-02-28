package ru.alex.burdovitsin.eventwaiter.repository.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.alex.burdovitsin.eventwaiter.model.jpa.ExampleRecord;
import ru.alex.burdovitsin.eventwaiter.repository.ExampleRepository;

import java.util.List;

@Repository
public class ExampleRepositoryImpl implements ExampleRepository {

    private final String ADD_RECORD_SQL = "INSERT INTO example (message) VALUES (?)";
    private final String GET_ALL_RECORD_SQL = "SELECT * FROM example";

    private final JdbcTemplate jdbcTemplate;

    public ExampleRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addRecord(ExampleRecord record) {
        String message = record.getMessage();
        jdbcTemplate.update(ADD_RECORD_SQL, message);
    }

    @Override
    public List<ExampleRecord> getAllRecords() {
        return jdbcTemplate.query(GET_ALL_RECORD_SQL,
                (resultSet, i) -> new ExampleRecord(
                        resultSet.getLong("id"),
                        resultSet.getString("message"))
        );
    }
}
