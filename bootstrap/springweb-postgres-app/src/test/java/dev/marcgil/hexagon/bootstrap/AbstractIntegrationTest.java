package dev.marcgil.hexagon.bootstrap;

import dev.marcgil.hexagon.bootstrap.AbstractIntegrationTest.TestConfig;
import dev.marcgil.hexagon.bootstrap.EnablePostgresTestContainerContextCustomerFactory.EnabledPostgresTestContainer;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.Map;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@AutoConfigureMockMvc
@EnabledPostgresTestContainer
@TestInstance(Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT,
    properties = "spring.main.allow-bean-definition-overriding=true",
    classes = {FilmSpringBootApplication.class, TestConfig.class})
public class AbstractIntegrationTest {

  @TestConfiguration
  static class TestConfig {

    @Value("${postgres.container.port}")
    public String postgresPort;

    @Bean
    @Primary
    EntityManagerFactory entityManagerFactoryProvider() {
      Map<String, Object> properties = Map.of("hibernate.connection.url",
          String.format("jdbc:postgresql://localhost:%s/hexagonal-architecture", postgresPort));
      return Persistence.createEntityManagerFactory("dev.marcgil.hexagon.film", properties);
    }

  }

}
