package com.javaproject2;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;

public class KafkaKRaftContainer extends GenericContainer<KafkaKRaftContainer> {
    public KafkaKRaftContainer() {
        super(DockerImageName.parse("bitnami/kafka:3.6.1"));

        withEnv("KAFKA_ENABLE_KRAFT", "yes");
        withEnv("KAFKA_CFG_PROCESS_ROLES", "broker,controller");
        withEnv("KAFKA_CFG_NODE_ID", "1");
        withEnv("KAFKA_CFG_CONTROLLER_QUORUM_VOTERS", "1@localhost:9093");
        withEnv("KAFKA_CFG_CONTROLLER_LISTENER_NAMES", "CONTROLLER");
        withEnv("KAFKA_CFG_LISTENERS", "PLAINTEXT://:9092,CONTROLLER://:9093");
        withEnv("KAFKA_CFG_ADVERTISED_LISTENERS", "PLAINTEXT://localhost:9092");
        withEnv("ALLOW_PLAINTEXT_LISTENER", "yes");
        withEnv("KAFKA_CFG_AUTO_CREATE_TOPICS_ENABLE", "true");

        withExposedPorts(9092);
        waitingFor(Wait.forListeningPort().withStartupTimeout(java.time.Duration.ofSeconds(40)));
    }

    public String getBootstrapServers() {
        return "PLAINTEXT://" + getHost() + ":" + getMappedPort(9092);
    }
}
