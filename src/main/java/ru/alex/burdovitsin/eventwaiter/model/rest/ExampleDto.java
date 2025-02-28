package ru.alex.burdovitsin.eventwaiter.model.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@ToString
//@NoArgsConstructor
//@AllArgsConstructor
//@Data
public class ExampleDto {

    @JsonProperty("id")
    private Long id;

    @NonNull
    @JsonProperty("message")
    private String message;
}
