package dev.marcgil.hexagon.film.application.port.api;

import dev.marcgil.hexagon.film.domain.Film;
import dev.marcgil.hexagon.film.domain.Film.Genre;
import java.time.Year;
import java.util.List;

public interface GetFilmsUseCase {

  List<Film> getFilms(GetFilmsQuery getFilmsQuery);

  record GetFilmsQuery(
      String directorId,
      Genre genre,
      Year yearOfRecording) {

  }

}
