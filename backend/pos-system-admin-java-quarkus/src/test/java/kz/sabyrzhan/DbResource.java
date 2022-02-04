package kz.sabyrzhan;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.Map;

public class DbResource implements QuarkusTestResourceLifecycleManager {
    public static final String DB_NAME = "test";
    public static final String DB_USERNAME = "test";
    public static final String DB_PASSWORD = "test";

    private PostgreSQLContainer dbContainer = (PostgreSQLContainer) new PostgreSQLContainer("postgres")
            .withDatabaseName(DB_NAME)
            .withPassword(DB_PASSWORD)
            .withUsername(DB_USERNAME)
            .withInitScript("ddl.sql");

    @Override
    public Map<String, String> start() {
        if (true) return Map.of();
        dbContainer.start();
        String url = "vertx-reactive:postgresql://localhost:" + dbContainer.getMappedPort(5432) + "/" + DB_NAME;
        return Map.of(
                "%test.quarkus.devservices.enabled", "false",
                "%test.quarkus.datasource.db-kind", "postgresql",
                "%test.quarkus.datasource.username", DB_USERNAME,
                "%test.quarkus.datasource.password", DB_PASSWORD,
                "%test.quarkus.datasource.reactive.url", url
        );
    }

    @Override
    public void stop() {
        if (true) return;
        dbContainer.stop();
    }
}
