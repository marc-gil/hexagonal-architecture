package dev.marcgil.hexagon.bootstrap;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;
import java.util.Map;
import lombok.EqualsAndHashCode;
import org.jspecify.annotations.NonNull;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.MapPropertySource;
import org.springframework.test.context.ContextConfigurationAttributes;
import org.springframework.test.context.ContextCustomizer;
import org.springframework.test.context.ContextCustomizerFactory;
import org.springframework.test.context.MergedContextConfiguration;
import org.springframework.test.context.TestContextAnnotationUtils;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

public class EnablePostgresTestContainerContextCustomerFactory implements ContextCustomizerFactory {

  @Inherited
  @Documented
  @Target(ElementType.TYPE)
  @Retention(RetentionPolicy.RUNTIME)
  public @interface EnabledPostgresTestContainer {

  }

  public ContextCustomizer createContextCustomizer(@NonNull Class<?> testClass,
      @NonNull List<ContextConfigurationAttributes> configAttributes) {
    if (!TestContextAnnotationUtils.hasAnnotation(testClass, EnabledPostgresTestContainer.class)) {
      return null;
    }
    return new PostgresTestContainerContextCustomizer();
  }

  @EqualsAndHashCode
  private static class PostgresTestContainerContextCustomizer implements ContextCustomizer {

    private static final DockerImageName POSTGRES_IMAGE = DockerImageName.parse("postgres")
        .withTag("latest");

    @Override
    public void customizeContext(@NonNull ConfigurableApplicationContext context,
        @NonNull MergedContextConfiguration mergedConfig) {
      PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>(POSTGRES_IMAGE)
          .withDatabaseName("hexagonal-architecture")
          .withUsername("root")
          .withPassword("example");

      postgresContainer.start();

      Map<String, Object> properties = Map.of("postgres.container.port",
          postgresContainer.getMappedPort(5432));

      MapPropertySource propertySource = new MapPropertySource("PostgresContainer test properties",
          properties);

      context.getEnvironment().getPropertySources().addFirst(propertySource);
    }
  }

}
