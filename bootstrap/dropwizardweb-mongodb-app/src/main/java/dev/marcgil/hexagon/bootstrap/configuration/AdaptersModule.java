package dev.marcgil.hexagon.bootstrap.configuration;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.mongodb.client.MongoClient;
import dev.marcgil.hexagon.film.adapter.dropwizard.ApiMapper;
import dev.marcgil.hexagon.film.adapter.dropwizard.ApiMapperImpl;
import dev.marcgil.hexagon.film.adapter.mongodb.DirectorRepository;
import dev.marcgil.hexagon.film.adapter.mongodb.MongoDbDirectorRepository;
import dev.marcgil.hexagon.film.adapter.mongodb.PersistenceAdapter;
import dev.marcgil.hexagon.film.adapter.mongodb.PersistenceMapper;
import dev.marcgil.hexagon.film.adapter.mongodb.PersistenceMapperImpl;
import dev.marcgil.hexagon.film.application.port.spi.DirectorDao;
import dev.marcgil.hexagon.film.application.port.spi.FilmDao;
import dev.marcgil.hexagon.film.application.service.factory.DirectorFactory;
import dev.marcgil.hexagon.film.application.service.DirectorService;
import dev.marcgil.hexagon.film.application.service.factory.FilmFactory;
import dev.marcgil.hexagon.film.application.service.FilmService;
import dev.marcgil.hexagon.film.application.service.factory.IdProvider;
import dev.marcgil.hexagon.film.application.service.factory.RandomUuidIdProvider;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@RequiredArgsConstructor
public class AdaptersModule extends AbstractModule {

  private final ApplicationConfiguration configuration;

  @Override
  @SneakyThrows
  protected void configure() {
    bind(ApplicationConfiguration.class).toInstance(configuration);
    bind(FilmDao.class).to(PersistenceAdapter.class);
    bind(DirectorDao.class).to(PersistenceAdapter.class);
  }

  @Provides
  @Singleton
  FilmService filmService(DirectorDao directorDao, FilmDao filmDao) {
    return new FilmService(directorDao, filmDao);
  }

  @Provides
  @Singleton
  DirectorService directorService(DirectorDao directorDao, IdProvider idProvider) {
    return new DirectorService(directorDao, new FilmFactory(idProvider),
        new DirectorFactory(idProvider));
  }

  @Provides
  @Singleton
  PersistenceAdapter persistenceAdapter(PersistenceMapper mapper, DirectorRepository repository) {
    return new PersistenceAdapter(mapper, repository);
  }

  @Provides
  @Singleton
  PersistenceMapper directorPersistenceMapper() {
    return new PersistenceMapperImpl();
  }

  @Provides
  @Singleton
  DirectorRepository directorRepository(MongoClient mongoClient) {
    return new MongoDbDirectorRepository(mongoClient, configuration.getDb());
  }

  @Provides
  @Singleton
  IdProvider idProvider() {
    return new RandomUuidIdProvider();
  }

  @Provides
  @Singleton
  ApiMapper apiMapper() {
    return new ApiMapperImpl();
  }

}