package ru.alex.burdovitsin.eventwaiter.it;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.alex.burdovitsin.eventwaiter.model.rest.ExampleDto;
import ru.alex.burdovitsin.eventwaiter.service.ExampleService;

import java.util.List;

@SpringBootTest
@ContextConfiguration(initializers = {EventWaiterTestIT.Initializer.class})
@Testcontainers
public class EventWaiterTestIT {

    @Autowired
    private ExampleService exampleService;

    @Container
    public static PostgreSQLContainer<?> database = new PostgreSQLContainer<>("postgres")
            .withDatabaseName("example")
            .withUsername("itest_user")
            .withPassword("itest_pswd");

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of(
                    "spring.datasource.url=" + database.getJdbcUrl(),
                    "spring.datasource.username=" + database.getUsername(),
                    "spring.datasource.password=" + database.getPassword(),
                    "spring.liquibase.enabled=true"
            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }

    @Test
    public void SimpleTest() {
        ExampleDto dto = new ExampleDto();
        dto.setMessage("Hello World");
        exampleService.addExampleItem(dto);
        List<ExampleDto> result = exampleService.getAllExampleItems();
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(dto.getMessage(), result.get(0).getMessage());
    }
}
