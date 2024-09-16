package dev.marcgil.hexagon.film.application.port.api;

import dev.marcgil.hexagon.film.domain.Director;
import java.time.LocalDate;
import lombok.NonNull;

public interface CreateDirectorUseCase {

  Director createDirector(CreateDirectorCommand command);

  record CreateDirectorCommand(
      @NonNull
      String directorName,
      LocalDate directorBirthDate
  ) {

  }
}
