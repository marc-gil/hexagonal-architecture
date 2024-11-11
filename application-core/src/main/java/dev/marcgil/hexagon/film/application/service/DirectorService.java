package dev.marcgil.hexagon.film.application.service;

import dev.marcgil.hexagon.film.application.port.api.AddFilmToDirectorUseCase;
import dev.marcgil.hexagon.film.application.port.api.CreateDirectorUseCase;
import dev.marcgil.hexagon.film.application.port.api.GetDirectorsByNameUseCase;
import dev.marcgil.hexagon.film.application.port.spi.DirectorDao;
import dev.marcgil.hexagon.film.application.service.factory.DirectorFactory;
import dev.marcgil.hexagon.film.application.service.factory.FilmFactory;
import dev.marcgil.hexagon.film.domain.Director;
import dev.marcgil.hexagon.film.domain.Film;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;

@NullMarked
@RequiredArgsConstructor
public class DirectorService implements AddFilmToDirectorUseCase, GetDirectorsByNameUseCase,
    CreateDirectorUseCase {

  @NonNull
  private final DirectorDao directorDao;
  @NonNull
  private final FilmFactory filmFactory;
  @NonNull
  private final DirectorFactory directorFactory;

  @Override
  public Director addFilmToDirector(AddFilmToDirectorCommand command) {
    Director director = directorDao.findById(command.directorId())
        .orElseThrow(() -> new IllegalArgumentException(
            "Non existing director with id" + command.directorId()));

    Film film = filmFactory.create(command.filmCharacteristics(), director);
    director.addFilm(film);

    return directorDao.save(director);
  }

  @Override
  public List<Director> getDirectorsByName(String name) {
    return directorDao.findByName(name);
  }

  @Override
  public Director createDirector(CreateDirectorCommand command) {
    Director director = directorFactory.create(command);
    return directorDao.save(director);
  }

}
