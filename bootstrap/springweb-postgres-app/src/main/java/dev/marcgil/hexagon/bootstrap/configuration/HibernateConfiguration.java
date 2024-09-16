package dev.marcgil.hexagon.bootstrap.configuration;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HibernateConfiguration {

  @Bean
  EntityManagerFactory entityManagerFactoryProvider() {
    return Persistence.createEntityManagerFactory("dev.marcgil.hexagon.film");
  }

}
