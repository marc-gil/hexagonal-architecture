package dev.marcgil.hexagon.film.application.port.api;

import dev.marcgil.hexagon.film.domain.Director;
import dev.marcgil.hexagon.film.domain.Film.Genre;
import java.time.Duration;
import java.time.Year;
import java.util.List;
import org.jspecify.annotations.NonNull;

public interface AddFilmToDirectorUseCase {

  @NonNull
  Director addFilmToDirector(@NonNull AddFilmToDirectorCommand command);

  record AddFilmToDirectorCommand(
      @lombok.NonNull
      String directorId,
      @lombok.NonNull
      FilmCharacteristics filmCharacteristics) {

  }

  record FilmCharacteristics(
      @lombok.NonNull
      Year yearOfRecording,
      @lombok.NonNull
      String originalTitle,
      @lombok.NonNull
      Duration duration,
      @lombok.NonNull
      List<Genre> genres,
      List<String> cast) {

  }

}
