package ru.alex.burdovitsin.eventwaiter.it;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.rnorth.ducttape.unreliables.Unreliables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.alex.burdovitsin.eventwaiter.it.exception.TimeOutException;
import ru.alex.burdovitsin.eventwaiter.it.exception.UntilWaitException;
import ru.alex.burdovitsin.eventwaiter.model.rest.ExampleDto;
import ru.alex.burdovitsin.eventwaiter.service.ExampleService;

import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@SpringBootTest
@Testcontainers
@DirtiesContext
@ContextConfiguration(initializers = {EventWaiterKafkaTestIT.Initializer.class})
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9192", "port=9192" })
public class EventWaiterKafkaTestIT {
    @Value(value = "${spring.kafka.add-example-record-topic}")
    private String addExampleRecordTopic;

    @Autowired
    private ExampleService exampleService;

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Container
    public static PostgreSQLContainer<?> database = new PostgreSQLContainer<>("postgres")
            .withDatabaseName("example")
            .withUsername("itest_user")
            .withPassword("itest_pswd");

    private static final ExecutorService EXECUTOR_SERVICE = Executors.newSingleThreadExecutor();

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
    public void KafkaTest() {
        String toKafka = "{\"message\" : \"example\"}";
        kafkaTemplate.send(addExampleRecordTopic, toKafka);
        Callable<Boolean> lambda = () -> {
            List<ExampleDto> result = exampleService.getAllExampleItems();
            return !result.isEmpty();
        };
        Unreliables.retryUntilTrue(10, TimeUnit.SECONDS, lambda);
        List<ExampleDto> result = exampleService.getAllExampleItems();
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.size());
    }


    @Test
    public void KafkaTestOnOwnSolution() {
        String toKafka = "{\"message\" : \"example\"}";
        kafkaTemplate.send(addExampleRecordTopic, toKafka);
        Callable<Boolean> lambda = () -> {
            List<ExampleDto> result = exampleService.getAllExampleItems();
            log.info("Result: {}", result);
            return !result.isEmpty();
        };
        waitUntilFalse(10, TimeUnit.SECONDS, lambda);
        List<ExampleDto> result = exampleService.getAllExampleItems();
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.size());
    }

    private static void waitUntilFalse(final int timeout,
                                       @NotNull final TimeUnit timeUnit,
                                       @NotNull final Callable<Boolean> statement
    ) {
        Future<Boolean> future = EXECUTOR_SERVICE.submit(getCallableFunction(statement));
        try {
            if (!future.get(timeout, timeUnit)) {
                throw new TimeOutException(); //параноя???
            }
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new TimeOutException();
        }
    }

    private static @NotNull Callable<Boolean> getCallableFunction(@NotNull Callable<Boolean> statement) {
        return () -> {
            final AtomicBoolean doContinue = new AtomicBoolean(true);
            try {
                while (doContinue.get()) {
                    try {
                        if (statement.call()) {
                            return true;
                        } else {
                            throw new UntilWaitException();
                        }
                    } catch (UntilWaitException e) {
                        log.info("Repeat query status");
                    }
                }
            } finally {
                doContinue.set(false);
            }
            return false;
        };
    }
}
