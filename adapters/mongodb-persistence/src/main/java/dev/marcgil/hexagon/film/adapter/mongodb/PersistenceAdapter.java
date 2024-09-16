package dev.marcgil.hexagon.film.adapter.mongodb;

import dev.marcgil.hexagon.film.adapter.mongodb.model.DirectorDocument;
import dev.marcgil.hexagon.film.application.port.spi.DirectorDao;
import dev.marcgil.hexagon.film.application.port.spi.FilmDao;
import dev.marcgil.hexagon.film.domain.Director;
import dev.marcgil.hexagon.film.domain.Film;
import dev.marcgil.hexagon.film.domain.Film.Genre;
import java.time.Year;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PersistenceAdapter implements DirectorDao, FilmDao {

  private final PersistenceMapper mapper;
  private final DirectorRepository repository;

  @Override
  public Optional<Director> findById(String directorId) {
    return repository.findById(directorId).map(mapper::toDirector);
  }

  @Override
  public Director save(Director director) {
    DirectorDocument directorDocumentToBeSaved = mapper.toDirectorDocument(director);
    DirectorDocument savedDirectorDocument = repository.save(directorDocumentToBeSaved);
    return mapper.toDirector(savedDirectorDocument);
  }

  @Override
  public List<Director> findByName(String name) {
    return repository.findByName(name).stream().map(mapper::toDirector).toList();
  }

  @Override
  public List<Film> findBy(String directorId, Genre genre, Year year) {
    return repository.findBy(directorId, genre, year).stream()
        .map(mapper::toDirector)
        .map(Director::getDirectedFilms)
        .flatMap(Collection::stream)
        .toList();
  }
}
