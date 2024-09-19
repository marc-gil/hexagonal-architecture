package dev.marcgil.hexagon.film.application.port.api;

import dev.marcgil.hexagon.film.domain.Director;
import java.time.LocalDate;
import org.jspecify.annotations.NonNull;

public interface CreateDirectorUseCase {

  @NonNull
  Director createDirector(@NonNull CreateDirectorCommand command);

  record CreateDirectorCommand(
      @lombok.NonNull
      String directorName,
      LocalDate directorBirthDate
  ) {

  }
}
