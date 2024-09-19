package dev.marcgil.hexagon.bootstrap.configuration;

import dev.marcgil.hexagon.film.adapter.postgres.ActorRepository;
import dev.marcgil.hexagon.film.adapter.postgres.DirectorRepository;
import dev.marcgil.hexagon.film.adapter.postgres.PersistenceAdapter;
import dev.marcgil.hexagon.film.adapter.postgres.PersistenceMapper;
import dev.marcgil.hexagon.film.adapter.postgres.PersistenceMapperImpl;
import dev.marcgil.hexagon.film.adapter.postgres.PostgresActorRepository;
import dev.marcgil.hexagon.film.adapter.postgres.PostgresDirectorRepository;
import dev.marcgil.hexagon.film.application.port.spi.ActorDao;
import dev.marcgil.hexagon.film.application.port.spi.DirectorDao;
import dev.marcgil.hexagon.film.application.port.spi.FilmDao;
import dev.marcgil.hexagon.film.application.service.factory.DirectorFactory;
import dev.marcgil.hexagon.film.application.service.DirectorService;
import dev.marcgil.hexagon.film.application.service.factory.FilmFactory;
import dev.marcgil.hexagon.film.application.service.FilmService;
import dev.marcgil.hexagon.film.application.service.factory.IdProvider;
import dev.marcgil.hexagon.film.application.service.factory.RandomUuidIdProvider;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("dev.marcgil.hexagon.film.adapter.springweb")
public class AdaptersConfiguration {

  @Bean
  FilmService filmService(DirectorDao directorDao, FilmDao filmDao) {
    return new FilmService(directorDao, filmDao);
  }

  @Bean
  DirectorService directorService(DirectorDao directorDao, IdProvider idProvider,
      ActorDao actorDao) {
    return new DirectorService(directorDao, new FilmFactory(idProvider, actorDao),
        new DirectorFactory(idProvider));
  }

  @Bean
  IdProvider idProvider() {
    return new RandomUuidIdProvider();
  }

  @Bean
  DirectorDao directorDao(PersistenceMapper mapper, DirectorRepository repository,
      ActorRepository actorRepository) {
    return new PersistenceAdapter(mapper, repository, actorRepository);
  }

  @Bean
  PersistenceMapper directorPersistenceMapper() {
    return new PersistenceMapperImpl();
  }

  @Bean
  DirectorRepository directorRepository(EntityManagerFactory entityManagerFactory) {
    return new PostgresDirectorRepository(entityManagerFactory);
  }

  @Bean
  ActorRepository actorRepository(EntityManagerFactory entityManagerFactory) {
    return new PostgresActorRepository(entityManagerFactory);
  }

}
