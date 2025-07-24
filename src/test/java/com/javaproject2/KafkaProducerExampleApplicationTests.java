package com.javaproject2;

import com.javaproject2.dto.Customer;
import com.javaproject2.service.KafkaMessagePublisher;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistrar;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.kafka.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static org.testcontainers.shaded.org.awaitility.Awaitility.await;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class KafkaProducerExampleApplicationTests {

//    @Container
//    static KafkaContainer kafka = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.4.0")
//            .asCompatibleSubstituteFor("apache/kafka")
//    );

    @Container
    static KafkaKRaftContainer kafka = new KafkaKRaftContainer();

    @DynamicPropertySource
    static void overrideKafkaProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
    }

    @Autowired
    private KafkaMessagePublisher publisher;

    @BeforeAll
    static void startContainer() {
        kafka.start();
        System.setProperty("spring.kafka.bootstrap-servers", kafka.getBootstrapServers());
        System.out.println("Kafka bootstrap servers: " + kafka.getBootstrapServers());
    }

    @AfterAll
    static void stopContainer() {
        kafka.stop();
    }

    @Test
    public void testSendEventToTopic() {
        Customer c = new Customer(1001, "Tarun Pugal", "tarunpugal@gmail.com", "776065505");
        System.out.println("About to send: " + c);

        publisher.sendEventsToTopic(c);

        await().pollInterval(Duration.ofSeconds(3))
                .atMost(10, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    // assert statement
                });
    }


}
