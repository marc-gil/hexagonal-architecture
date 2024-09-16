package dev.marcgil.hexagon.bootstrap.configuration;

import dev.marcgil.hexagon.film.adapter.postgress.DirectorRepository;
import dev.marcgil.hexagon.film.adapter.postgress.PersistenceAdapter;
import dev.marcgil.hexagon.film.adapter.postgress.PersistenceMapper;
import dev.marcgil.hexagon.film.adapter.postgress.PersistenceMapperImpl;
import dev.marcgil.hexagon.film.adapter.postgress.PostgressDirectorRepository;
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
  DirectorService directorService(DirectorDao directorDao, IdProvider idProvider) {
    return new DirectorService(directorDao, new FilmFactory(idProvider),
        new DirectorFactory(idProvider));
  }

  @Bean
  IdProvider idProvider() {
    return new RandomUuidIdProvider();
  }

  @Bean
  DirectorDao directorDao(PersistenceMapper mapper, DirectorRepository repository) {
    return new PersistenceAdapter(mapper, repository);
  }

  @Bean
  PersistenceMapper directorPersistenceMapper() {
    return new PersistenceMapperImpl();
  }

  @Bean
  DirectorRepository directorRepository(EntityManagerFactory entityManagerFactory) {
    return new PostgressDirectorRepository(entityManagerFactory);
  }

}
