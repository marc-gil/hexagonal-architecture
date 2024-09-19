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
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.utility.DockerImageName;

public class EnableMongoDbTestContainerContextCustomerFactory implements ContextCustomizerFactory {

  @Inherited
  @Documented
  @Target(ElementType.TYPE)
  @Retention(RetentionPolicy.RUNTIME)
  public @interface EnabledMongoDbTestContainer {

  }

  public ContextCustomizer createContextCustomizer(@NonNull Class<?> testClass,
      @NonNull List<ContextConfigurationAttributes> configAttributes) {
    if (!TestContextAnnotationUtils.hasAnnotation(testClass, EnabledMongoDbTestContainer.class)) {
      return null;
    }
    return new MongoDbTestContainerContextCustomizer();
  }

  @EqualsAndHashCode
  private static class MongoDbTestContainerContextCustomizer implements ContextCustomizer {

    private static final DockerImageName MONGO_DB_IMAGE = DockerImageName.parse("mongo")
        .withTag("latest");

    @Override
    public void customizeContext(@NonNull ConfigurableApplicationContext context,
        @NonNull MergedContextConfiguration mergedConfig) {
      MongoDBContainer mongoDBContainer = new MongoDBContainer(MONGO_DB_IMAGE)
          .withExposedPorts(27017);
      mongoDBContainer.start();
      Map<String, Object> properties = Map.of("mongodb.container.port",
          mongoDBContainer.getMappedPort(27017));

      MapPropertySource propertySource = new MapPropertySource("MongoDbContainer test properties",
          properties);
      context.getEnvironment().getPropertySources().addFirst(propertySource);
    }
  }

}
