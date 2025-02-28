package ru.alex.burdovitsin.eventwaiter.model.jpa;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;

@Getter
@ToString
public class ExampleRecord {

    private final long id;

    @Setter
    private String message;

    public ExampleRecord(long id, String message) {
        this.id = id;
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ExampleRecord that)) return false;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
