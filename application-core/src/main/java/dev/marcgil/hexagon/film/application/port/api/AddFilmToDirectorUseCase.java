package dev.marcgil.hexagon.film.application.port.api;

import dev.marcgil.hexagon.film.domain.Director;
import dev.marcgil.hexagon.film.domain.Film.Genre;
import java.time.Duration;
import java.time.Year;
import java.util.List;
import lombok.NonNull;

public interface AddFilmToDirectorUseCase {

  Director addFilmToDirector(AddFilmToDirectorCommand command);

  record AddFilmToDirectorCommand(
      @NonNull
      String directorId,
      @NonNull
      FilmCharacteristics filmCharacteristics) {

  }

  record FilmCharacteristics(
      @NonNull
      Year yearOfRecording,
      @NonNull
      String originalTitle,
      @NonNull
      Duration duration,
      @NonNull
      List<Genre> genres,
      List<String> cast) {

  }

}
