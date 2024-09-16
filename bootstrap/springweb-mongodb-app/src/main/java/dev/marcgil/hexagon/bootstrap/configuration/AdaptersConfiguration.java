package dev.marcgil.hexagon.bootstrap.configuration;

import com.mongodb.client.MongoClient;
import dev.marcgil.hexagon.film.adapter.mongodb.PersistenceAdapter;
import dev.marcgil.hexagon.film.adapter.mongodb.DirectorRepository;
import dev.marcgil.hexagon.film.adapter.mongodb.MongoDbDirectorRepository;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("dev.marcgil.hexagon.film.adapter.springweb")
public class AdaptersConfiguration {

  @Value("${spring.data.mongodb.db}")
  private String dbName;

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
  DirectorRepository directorRepository(MongoClient mongoClient) {
    return new MongoDbDirectorRepository(mongoClient, dbName);
  }


}
