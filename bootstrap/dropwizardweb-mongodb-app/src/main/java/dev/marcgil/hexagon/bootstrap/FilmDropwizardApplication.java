package dev.marcgil.hexagon.bootstrap;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.inject.Guice;
import com.google.inject.Injector;
import dev.marcgil.hexagon.bootstrap.configuration.AdaptersModule;
import dev.marcgil.hexagon.bootstrap.configuration.ApplicationConfiguration;
import dev.marcgil.hexagon.bootstrap.configuration.MongoDbModule;
import dev.marcgil.hexagon.bootstrap.health.BasicHealthCheck;
import dev.marcgil.hexagon.film.adapter.dropwizard.ApiMapper;
import dev.marcgil.hexagon.film.adapter.dropwizard.DirectorResource;
import dev.marcgil.hexagon.film.adapter.dropwizard.FilmResource;
import dev.marcgil.hexagon.film.application.service.DirectorService;
import dev.marcgil.hexagon.film.application.service.FilmService;
import io.dropwizard.configuration.ResourceConfigurationSourceProvider;
import io.dropwizard.core.Application;
import io.dropwizard.core.setup.Bootstrap;
import io.dropwizard.core.setup.Environment;
import io.swagger.v3.jaxrs2.integration.resources.OpenApiResource;
import io.swagger.v3.oas.integration.SwaggerConfiguration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import java.util.Set;

public class FilmDropwizardApplication extends Application<ApplicationConfiguration> {

  public static void main(String[] args) throws Exception {
    new FilmDropwizardApplication().run(args);
  }

  @Override
  public String getName() {
    return "application";
  }

  @Override
  public void initialize(Bootstrap<ApplicationConfiguration> bootstrap) {
    bootstrap.setConfigurationSourceProvider(new ResourceConfigurationSourceProvider());
  }

  @Override
  public void run(ApplicationConfiguration configuration, Environment environment) {
    Injector injector = Guice.createInjector(new MongoDbModule(configuration),
        new AdaptersModule(configuration));
    FilmService filmService = injector.getInstance(FilmService.class);
    DirectorService directorService = injector.getInstance(DirectorService.class);
    ApiMapper apiMapper = injector.getInstance(ApiMapper.class);

    FilmResource filmResource = new FilmResource(
        directorService,
        filmService,
        apiMapper
    );
    environment.jersey().register(filmResource);
    DirectorResource directorResource = new DirectorResource(
        filmService,
        directorService,
        directorService,
        apiMapper
    );
    environment.jersey().register(directorResource);
    BasicHealthCheck healthCheck = new BasicHealthCheck();
    environment.healthChecks().register("template", healthCheck);

    OpenAPI oas = new OpenAPI();
    Info info = new Info()
        .title("Film Swagger")
        .description("This is a sample Film Server based on the OpenAPI 3.0 specification.")
        .version("1.0.0");

    oas.info(info);
    SwaggerConfiguration oasConfig = new SwaggerConfiguration()
        .openAPI(oas)
        .prettyPrint(true)
        .resourcePackages(Set.of("dev.marcgil.hexagon.film.adapter.dropwizard"));

    environment.getObjectMapper().setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL);

    environment.jersey().register(new OpenApiResource().openApiConfiguration(oasConfig));
  }

}