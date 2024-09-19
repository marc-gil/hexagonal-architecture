package dev.marcgil.hexagon.film.adapter.postgres;

import dev.marcgil.hexagon.film.adapter.postgres.model.DirectorDbo;
import dev.marcgil.hexagon.film.application.port.spi.ActorDao;
import dev.marcgil.hexagon.film.application.port.spi.DirectorDao;
import dev.marcgil.hexagon.film.application.port.spi.FilmDao;
import dev.marcgil.hexagon.film.domain.Director;
import dev.marcgil.hexagon.film.domain.Film;
import dev.marcgil.hexagon.film.domain.Film.Genre;
import dev.marcgil.hexagon.film.domain.Person;
import java.time.Year;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;

@RequiredArgsConstructor
public class PersistenceAdapter implements DirectorDao, FilmDao, ActorDao {

  @NonNull
  private final PersistenceMapper mapper;
  @NonNull
  private final DirectorRepository directorRepository;
  @NonNull
  private final ActorRepository actorRepository;

  @Override
  public Optional<Director> findById(String directorId) {
    return directorRepository.findById(directorId).map(mapper::toDirector);
  }

  @Override
  public Director save(Director director) {
    DirectorDbo directorToBeSaved = mapper.toDirectorDbo(director);
    DirectorDbo savedDirector = directorRepository.save(directorToBeSaved);
    return mapper.toDirector(savedDirector);
  }

  @Override
  public List<Director> findByName(String name) {
    return directorRepository.findByName(name).stream().map(mapper::toDirector).toList();
  }

  @Override
  public List<Film> findBy(String directorId, Genre genre, Year year) {
    return directorRepository.findBy(directorId, genre, year).stream()
        .map(mapper::toDirector)
        .map(Director::getDirectedFilms)
        .flatMap(Collection::stream)
        .toList();
  }

  @Override
  public Optional<Person> findActorByName(String actorName) {
    return actorRepository.findByName(actorName).map(mapper::toPerson);
  }
}
