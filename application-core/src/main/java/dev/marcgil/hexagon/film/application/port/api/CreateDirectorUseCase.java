package dev.marcgil.hexagon.film.application.port.api;

import dev.marcgil.hexagon.film.domain.Director;
import java.time.LocalDate;
import lombok.NonNull;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public interface CreateDirectorUseCase {

  Director createDirector(CreateDirectorCommand command);

  record CreateDirectorCommand(
      @NonNull
      String directorName,
      @Nullable
      LocalDate directorBirthDate
  ) {

  }
}
