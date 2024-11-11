package dev.marcgil.hexagon.film.application.port.api;

import dev.marcgil.hexagon.film.domain.Film;
import dev.marcgil.hexagon.film.domain.Film.Genre;
import java.time.Year;
import java.util.List;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public interface GetFilmsUseCase {

  List<Film> getFilms(@Nullable GetFilmsQuery getFilmsQuery);

  record GetFilmsQuery(
      @Nullable
      String directorId,
      @Nullable
      Genre genre,
      @Nullable
      Year yearOfRecording) {

  }

}
