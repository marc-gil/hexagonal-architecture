package dev.marcgil.hexagon.film.application.service;

import dev.marcgil.hexagon.film.application.port.api.GetFilmsByDirectorUseCase;
import dev.marcgil.hexagon.film.application.port.api.GetFilmsUseCase;
import dev.marcgil.hexagon.film.application.port.spi.DirectorDao;
import dev.marcgil.hexagon.film.application.port.spi.FilmDao;
import dev.marcgil.hexagon.film.domain.Director;
import dev.marcgil.hexagon.film.domain.Film;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FilmService implements GetFilmsByDirectorUseCase, GetFilmsUseCase {

  private final DirectorDao directorDao;
  private final FilmDao filmDao;

  @Override
  public List<Film> getFilmsByDirector(String directorId) {
    return directorDao.findById(directorId)
        .map(Director::getDirectedFilms)
        .orElseThrow(
            () -> new IllegalArgumentException("Non existing director with id " + directorId));
  }

  @Override
  public List<Film> getFilms(GetFilmsQuery query) {
    if (query == null || query.directorId() == null && query.yearOfRecording() == null && query.genre() == null) {
      throw new IllegalArgumentException("At least one criteria must be specified to retrieve the films");
    }
    return filmDao.findBy(query.directorId(), query.genre(), query.yearOfRecording());
  }

}
